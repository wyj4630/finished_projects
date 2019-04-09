/* This `define` tells unistd to define usleep and random.  */
#define _XOPEN_SOURCE 500

#include "client_thread.h"
#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <strings.h>
#include <pthread.h>
#include <netinet/in.h>
#include <netdb.h>

int port_number = -1;
int num_request_per_client = -1;
int num_resources = -1;
int *provisioned_resources = NULL;

// Variable d'initialisation des threads clients.
unsigned int count = 0;


// Variable du journal.
// Nombre de requête acceptée (ACK reçus en réponse à REQ)
unsigned int count_accepted = 0;

// Nombre de requête en attente (WAIT reçus en réponse à REQ)
unsigned int count_on_wait = 0;

// Nombre de requête refusée (REFUSE reçus en réponse à REQ)
unsigned int count_invalid = 0;

// Nombre de client qui se sont terminés correctement (ACC reçu en réponse à END)
unsigned int count_dispatched = 0;

// Nombre total de requêtes envoyées.
unsigned int request_sent = 0;

//signaler l'initialisation du serveur
int initialized = false;


pthread_mutex_t request_sent_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mut_count_accepted= PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t mut_cond_init = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond_init = PTHREAD_COND_INITIALIZER;
pthread_mutex_t count_dispatched_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_on_wait_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_invalid_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t configuration_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_mut = PTHREAD_MUTEX_INITIALIZER;

// Vous devez modifier cette fonction pour faire l'envoie des requêtes
// Les ressources demandées par la requête doivent être choisies aléatoirement
// (sans dépasser le maximum pour le client). Elles peuvent être positives
// ou négatives.
// Assurez-vous que la dernière requête d'un client libère toute les ressources
// qu'il a jusqu'alors accumulées.
void
send_request (int client_id, int request_id, int socket_fd, char *command, int com_size)
{

  // TP2 TODO

  char buf_recv[256];
  char *commad_array[10];

  while (send(socket_fd, command, com_size, 0)<0);

  bzero(commad_array, sizeof(commad_array));
  bzero(buf_recv, sizeof(buf_recv));

  if((recv(socket_fd, buf_recv , sizeof(buf_recv), 0)> 0))
  {
    printf("reponse reçu:  %s\n",buf_recv );

    split_string(buf_recv, commad_array, " ");

    switch(atoi(commad_array[0]))
    {
      case ERR:
      {
        pthread_mutex_lock(&count_invalid_mut);
        count_invalid ++;
        pthread_mutex_unlock(&count_invalid_mut);
      }
      break;
      case ACK:
      {
        int temp[num_resources];
        bzero(temp, sizeof(temp));
        split_string(command, commad_array, " ");
        get_ressources_vector(commad_array, temp);
        set_negative_value(temp, num_resources);
        build_command(command , REQ, client_id , temp, num_resources);
        //printf(" libre %s\n", command );
        send(socket_fd, command, com_size, 0);

        pthread_mutex_lock(&mut_count_accepted);
        count_accepted ++;
        pthread_mutex_unlock(&mut_count_accepted);

      }
      break;
      case WAIT:
      {
        pthread_mutex_lock(&count_on_wait_mut);
        count_on_wait++;
        pthread_mutex_unlock(&count_on_wait_mut);
        sleep(atoi(commad_array[2]));
        //send_request (client_id, request_id, socket_fd, command, sizeof(command));
      }
      break;
    }

  }
  // TP2 TODO:END
}


void *
ct_code (void *param)
{
  int socket_fd = -1;
  client_thread *ct = (client_thread *) param;
  int *client_ressources_needs = malloc (num_resources * sizeof (int));
  int *client_max_need = malloc (num_resources * sizeof (int));
  for (int i = 0; i < num_resources; i++)
  {
    client_ressources_needs[i]=0;
    client_max_need[i] = 0;

  }


    socket_fd = socket (AF_INET, SOCK_STREAM, 0);
    char command[256] ;
    struct sockaddr_in serv_addr;
    memset (&serv_addr, 0, sizeof (serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons (port_number);


    int c = connect(socket_fd, (struct sockaddr *) &serv_addr, sizeof(serv_addr));
    if(c < 0){
      perror ("ERROR connexion...");
    }


    pthread_mutex_lock(&mut_cond_init);
    if(!initialized)
    {
      initialized = true;
      build_command(command , BEGIN, 5 , NULL, 1);
      send(socket_fd, command , sizeof(command), 0);
      bzero(command, sizeof(command));

      recv(socket_fd, command , sizeof(command), 0);
      build_command(command , CONF, 0, provisioned_resources, num_resources);
      send(socket_fd, command , sizeof(command), 0);
      pthread_mutex_lock(&request_sent_mut);
      request_sent ++;
      pthread_mutex_unlock(&request_sent_mut);
     }
    pthread_mutex_unlock(&mut_cond_init);


    //boucle des requete vers le serveur
  for (unsigned int request_id = 0; request_id < num_request_per_client;
      request_id++)
  {
    bzero(command, sizeof(command));

    //initialiser le besoin max pour chaque client
    if(request_id == 0){

      generate_new_resources_request(provisioned_resources,  client_ressources_needs);
      additionner_vectors(client_max_need, client_ressources_needs, num_resources);
      build_command(command , INIT, ct->id , client_ressources_needs, num_resources);
      pthread_mutex_lock(&request_sent_mut);
      request_sent ++;
      pthread_mutex_unlock(&request_sent_mut);
      send(socket_fd, command , sizeof(command), 0) ;
    }

    //envoi de requette
     if(request_id < num_request_per_client - 1 && request_id > 0)
    {

      generate_new_resources_request(client_max_need,  client_ressources_needs);
      build_command(command , REQ, ct->id , client_ressources_needs, num_resources );
      send_request (ct->id, request_id, socket_fd, command, sizeof(command));
    }


    //envoi de la dernière requete
    if (request_id == num_request_per_client -1)
    {
       build_command(command , CLO, ct->id , NULL, 1);
       send(socket_fd, command , sizeof(command), 0);


      pthread_mutex_lock(&count_dispatched_mut);
      count_dispatched ++;
      pthread_mutex_unlock(&count_dispatched_mut);
    }

    pthread_mutex_lock(&request_sent_mut);
    request_sent ++;
    pthread_mutex_unlock(&request_sent_mut);
    /* Attendre un petit peu (0s-0.1s) pour simuler le calcul.  */
    usleep (random () % (100 * 1000));
  }

    //envoi de end
     build_command(command , END, 0 , NULL, 0);
     send(socket_fd, command , sizeof(command), 0);
     pthread_mutex_lock(&request_sent_mut);
      request_sent ++;
      pthread_mutex_unlock(&request_sent_mut);
      if(!(recv(socket_fd, command , sizeof(command), 0) < 0))
      {
        pthread_mutex_lock(&mut_count_accepted);
        count_accepted ++;
        pthread_mutex_unlock(&mut_count_accepted);
        free(client_ressources_needs);
        close(socket_fd);
      }
  pthread_exit (NULL);
}


//
// Vous devez changer le contenu de cette fonction afin de régler le
// problème de synchronisation de la terminaison.
// Le client doit attendre que le serveur termine le traitement de chacune
// de ses requêtes avant de terminer l'exécution.
//
void
ct_wait_server (client_thread **ct)
{

  // TP2 TODO

  sleep (4);
//for (unsigned int i = 0; i < count; i++)
 //   pthread_join (ct[i]->id, NULL);
  // TP2 TODO:END

}


void
ct_init (client_thread * ct)
{
  ct->id = count++;
}

void
ct_create_and_start (client_thread * ct)
{
  pthread_attr_init (&(ct->pt_attr));
  pthread_attr_setdetachstate(&(ct->pt_attr), PTHREAD_CREATE_DETACHED);
  pthread_create (&(ct->pt_tid), &(ct->pt_attr), &ct_code, ct);
}

//
// Affiche les données recueillies lors de l'exécution du
// serveur.
// La branche else ne doit PAS être modifiée.
//
void
st_print_results (FILE * fd, bool verbose)
{
  if (fd == NULL)
    fd = stdout;
  if (verbose)
  {
    fprintf (fd, "\n---- Résultat du client ----\n");
    fprintf (fd, "Requêtes acceptées: %d\n", count_accepted);
    fprintf (fd, "Requêtes : %d\n", count_on_wait);
    fprintf (fd, "Requêtes invalides: %d\n", count_invalid);
    fprintf (fd, "Clients : %d\n", count_dispatched);
    fprintf (fd, "Requêtes envoyées: %d\n", request_sent);
  }
  else
  {
    fprintf (fd, "%d %d %d %d %d\n", count_accepted, count_on_wait,
        count_invalid, count_dispatched, request_sent);
  }
}
void generate_new_resources_request(int *max_needed,  int *new_need){

  for (int i = 0; i < num_resources; i++)
    new_need[i] = generate_Random_number( max_needed[i]);
}

void set_negative_value(int *vector, int len){
  for (int i = 0; i < len; i++)
      vector[i] = -vector[i] ;
}
