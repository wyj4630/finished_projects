//#define _XOPEN_SOURCE 700   /* So as to allow use of `fdopen` and `getline`.  */

#include "common.h"
#include "server_thread.h"

#include <netinet/in.h>
#include <netdb.h>

#include <strings.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/poll.h>
#include <sys/socket.h>

#include <time.h>

enum { NUL = '\0' };

enum {
  /* Configuration constants.  */
  max_wait_time = 30,
  server_backlog_size = 5
};

unsigned int server_socket_fd;

// Nombre de client enregistré.
int nb_registered_clients;

// Variable du journal.
// Nombre de requêtes acceptées immédiatement (ACK envoyé en réponse à REQ).
unsigned int count_accepted = 0;

// Nombre de requêtes acceptées après un délai (ACK après REQ, mais retardé).
unsigned int count_wait = 0;

// Nombre de requête erronées (ERR envoyé en réponse à REQ).
unsigned int count_invalid = 0;

// Nombre de clients qui se sont terminés correctement
// (ACK envoyé en réponse à CLO).
unsigned int count_dispatched = 0;

// Nombre total de requête (REQ) traités.
unsigned int request_processed = 0;

// Nombre de clients ayant envoyé le message CLO.
unsigned int clients_ended = 0;

// TODO: Ajouter vos structures de données partagées, ici.
int *available ;
int **client_max_resources;
int **client_allocated_resources;
int **clients_needs;
int ressurces_nb;

pthread_mutex_t nb_registered_clients_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t clients_ended_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_dispatched_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t banquier_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t init_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t request_processed_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_wait_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_invalid_mut = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t count_accepted_mut = PTHREAD_MUTEX_INITIALIZER;


char request_error[256];
bool init = false;
int thread_socket_fd = -1;
char buffer[256];
void
st_init ()
{
  // Initialise le nombre de clients connecté.
  nb_registered_clients = 0;
  client_allocated_resources = malloc (server_backlog_size * sizeof (int*));
  client_max_resources = malloc (server_backlog_size * sizeof (int*));
  clients_needs = malloc (server_backlog_size * sizeof (int*));

}

void
st_process_requests (server_thread * st, int socket_fd)
{

  struct pollfd fds[1];
  fds->fd = socket_fd;
  fds->events = POLLIN;
  fds->revents = 0;
  char buffer[256];

  for (;;) {
    struct cmd_t cmd = { .nb_args = 0 };

    bzero(buffer, sizeof(buffer));
    int len = read_socket(socket_fd, buffer, sizeof(buffer), max_wait_time * 1000);
    printf("thread = %d : a reçu le Requete:  %s\n",st->id ,buffer);

    if (len > 0) {

      split_string(buffer, cmd.args ," "); //Spliter la commande en un tableau d'arguments
      pthread_mutex_lock(&request_processed_mut);
      request_processed ++;
      pthread_mutex_unlock(&request_processed_mut);


      switch(atoi(cmd.args[0]))
      {
          case BEGIN:   //Debut serveur
          {
            nb_registered_clients = atoi(cmd.args[2]);
            build_command(buffer , ACK, 5 , NULL, 1);
            send(socket_fd, buffer, sizeof(buffer), 0);

            pthread_mutex_lock(&count_accepted_mut);
            count_accepted ++;
            pthread_mutex_unlock(&count_accepted_mut);

          }
          break;
          case CONF:    //Configuration du serveur
          {
            ressurces_nb = atoi(cmd.args[1]);
            available = malloc (ressurces_nb * sizeof (int));
            for(unsigned int i = 0; i < ressurces_nb  ; i++)
                available[i] = atoi(cmd.args[i+2]);
            pthread_mutex_lock(&count_accepted_mut);
            count_accepted ++;
            pthread_mutex_unlock(&count_accepted_mut);
          }
          break;
          case INIT: //Initialisation des structures en fonction du nombre de ressources
          {
              pthread_mutex_lock(&init_mut);
                 if(!init)
                 {

                    for (int j = 0; j < nb_registered_clients; j++)
                    {
                      client_max_resources[j] = malloc (ressurces_nb * sizeof (int));
                      client_allocated_resources[j] = malloc (ressurces_nb * sizeof (int));
                      clients_needs[j]  = malloc (ressurces_nb * sizeof (int));
                        for (int i = 0; i < ressurces_nb; ++i)
                        {
                          client_allocated_resources[j][i] = 0;
                          client_max_resources[j][i] = 0;
                          clients_needs[j][i] = 0;
                        }
                    }
                    init = true;
                 }
                 pthread_mutex_unlock(&init_mut);

                 for(unsigned int i = 0; i < ressurces_nb; i++)
                      client_max_resources[atoi(cmd.args[2])][i] = atoi(cmd.args[i+3]);

                  pthread_mutex_lock(&count_accepted_mut);
                  count_accepted ++;
                  pthread_mutex_unlock(&count_accepted_mut);
          }
          break;
          case REQ: //traitement des demandes de ressources
          {          //avec l'ALGORITHME DU BANQUIER

            int ressources_demande[ressurces_nb];
            bzero(ressources_demande, sizeof(ressources_demande));
            printf("%s\n", cmd.args[2]);
            get_ressources_vector(cmd.args, ressources_demande);
            for(unsigned int i = 0; i < ressurces_nb; i++)
              clients_needs[atoi(cmd.args[2])][i] = atoi(cmd.args[i+3]) -
                                  client_allocated_resources[atoi(cmd.args[2])][i] ;






            //si ressources demandées sup au max initiale
            if(!vector_superior_equal(client_max_resources[atoi(cmd.args[2])], clients_needs[atoi(cmd.args[2])],
              ressurces_nb))
            {
              pthread_mutex_lock(&count_invalid_mut);
              count_invalid ++;
              pthread_mutex_unlock(&count_invalid_mut);

              char * msg = "Exceder demene maximale!";
              build_command_error(buffer , ERR, msg );
              send(socket_fd, buffer, sizeof(buffer), 0 );

            }

            //si pas assez de ressourcdes demmander au client d'attendre
            else if(!vector_inferieur_egale(ressources_demande, available, ressurces_nb))
            {
              pthread_mutex_lock(&count_wait_mut);
              count_wait ++;
              pthread_mutex_unlock(&count_wait_mut);

               build_command(buffer , WAIT, 0.01, NULL ,1);
              send(socket_fd, buffer, sizeof(buffer), 0 );
            }
            else
            {
            //TESTER SI ETAT SAIN avec le banquier
              pthread_mutex_lock(&banquier_mut);
              bool safe_state = detecte_safe_state(available, clients_needs, client_allocated_resources,
                                ressurces_nb,  nb_registered_clients);
              pthread_mutex_unlock(&banquier_mut);


              if(safe_state)// Etat sain c'est ack
              {

                  for(unsigned int i = 0; i < ressurces_nb; i++)
                    client_allocated_resources[atoi(cmd.args[2])][i] += clients_needs[atoi(cmd.args[2])][i];
                  build_command(buffer , ACK, 0, NULL ,1);
                  pthread_mutex_lock(&count_accepted_mut);
                  count_accepted ++;
                  pthread_mutex_unlock(&count_accepted_mut);

              }else // sinon un blacage détecté
                {
                  pthread_mutex_lock(&count_invalid_mut);
                  count_invalid ++;
                  pthread_mutex_unlock(&count_invalid_mut);
                  char *msg = "NON ALLOCATED DEADLOCK DETECTED..!";
                  build_command_error(buffer , ERR, msg);
                }

              send(socket_fd, buffer, sizeof(buffer), 0 );
            }
          }
          break;
          case CLO: //Fin d'un client
          {
            pthread_mutex_lock(&clients_ended_mut);
            clients_ended ++;
            pthread_mutex_unlock(&clients_ended_mut);

             pthread_mutex_lock(&count_accepted_mut);
             count_accepted ++;
             pthread_mutex_unlock(&count_accepted_mut);
          }
          break;
          case END://fin serveur
          {
            pthread_mutex_lock(&count_accepted_mut);
            count_accepted ++;
            pthread_mutex_unlock(&count_accepted_mut);
            pthread_mutex_lock(&count_dispatched_mut);
            count_dispatched ++;
            pthread_mutex_unlock(&count_dispatched_mut);
            build_command(buffer , ACK, 0, NULL ,1);
            send(socket_fd, buffer, sizeof(buffer), 0 );
            goto out;

          }
          break;
          default:
          {
            char *msg = "INVALID COMMAND NAME";
            build_command_error(buffer , ERR, msg);
            send(socket_fd, buffer,sizeof(buffer), 0 );
           }
          break;
      }

    } else {
      if (len == 0) {
        fprintf(stderr, "Thread %d, connection timeout\n", st->id);
      }
      break;
    }
    bzero(buffer, sizeof(buffer));
  }
  out:
  //arreter la connexion si tous les client sont fermés
  if(count_dispatched == 3)
  {
    accepting_connections = false;
    nb_registered_clients = 0;
  }


     close(socket_fd);
}


void
st_signal ()
{
  // Liberation des ressources après que tous les client sont déconnectés


for (int i = 0; i < ressurces_nb; ++i)
{
  free(clients_needs[i]);
  free(client_allocated_resources[i]);
  free(client_max_resources[i]);
}
  free(available);
  free(client_allocated_resources);
  free(client_max_resources);

}

void *
st_code (void *param)
{
  server_thread *st = (server_thread *) param;

  struct sockaddr_in thread_addr;
  socklen_t socket_len = sizeof (thread_addr);
  int thread_socket_fd = -1;
  int end_time = time (NULL) + max_wait_time;

  // Boucle jusqu'à ce que `accept` reçoive la première connection.
  while (thread_socket_fd < 0)
  {
    thread_socket_fd =
      accept (server_socket_fd, (struct sockaddr *) &thread_addr,
          &socket_len);

    if (time (NULL) >= end_time)
    {
      break;
    }
  }

  // Boucle de traitement des requêtes.
  while (accepting_connections)
  {
    if (!nb_registered_clients && time (NULL) >= end_time)
    {
      fprintf (stderr, "Time out on thread %d.\n", st->id);
      pthread_exit (NULL);
    }
    if (thread_socket_fd > 0)
    {
      st_process_requests (st, thread_socket_fd);
      close (thread_socket_fd);
      end_time = time (NULL) + max_wait_time;
    }
    thread_socket_fd =
      accept (server_socket_fd, (struct sockaddr *) &thread_addr,
          &socket_len);
  }
  close(thread_socket_fd);
  return NULL;
}


//
// Ouvre un socket pour le serveur.
//
void
st_open_socket (int port_number)
{
  server_socket_fd = socket (AF_INET, SOCK_STREAM | SOCK_NONBLOCK, 0);
  if (server_socket_fd < 0)
    perror ("ERROR opening socket");

  if (setsockopt(server_socket_fd, SOL_SOCKET, SO_REUSEPORT, &(int){ 1 }, sizeof(int)) < 0) {
    perror("setsockopt()");
    exit(1);
  }

  struct sockaddr_in serv_addr;
  memset (&serv_addr, 0, sizeof (serv_addr));
  serv_addr.sin_family = AF_INET;
  serv_addr.sin_addr.s_addr = INADDR_ANY;
  serv_addr.sin_port = htons (port_number);

  if (bind
      (server_socket_fd, (struct sockaddr *) &serv_addr,
       sizeof (serv_addr)) < 0)
    perror ("ERROR on binding");

  listen (server_socket_fd, server_backlog_size);
}


//
// Affiche les données recueillies lors de l'exécution du
// serveur.
// La branche else ne doit PAS être modifiée.
//
void
st_print_results (FILE * fd, bool verbose)
{
  if (fd == NULL) fd = stdout;
  if (verbose)
  {
    fprintf (fd, "\n---- Résultat du serveur ----\n");
    fprintf (fd, "Requêtes acceptées: %d\n", count_accepted);
    fprintf (fd, "Requêtes : %d\n", count_wait);
    fprintf (fd, "Requêtes invalides: %d\n", count_invalid);
    fprintf (fd, "Clients : %d\n", count_dispatched);
    fprintf (fd, "Requêtes traitées: %d\n", request_processed);
  }
  else
  {
    fprintf (fd, "%d %d %d %d %d\n", count_accepted, count_wait,
        count_invalid, count_dispatched, request_processed);
  }
}



//Etat sûr du banquier
bool
detecte_safe_state(int *disponible, int **besoins_client, int **resources_allouees,
                        int nb_ressources,  int nb_process)
{


  int table_travail[nb_ressources];
  int fini[nb_process];

  for(int i = 0; i< nb_ressources; i++)
   table_travail[i] = disponible[i];

  for(int i = 0; i< nb_process; i++)
      fini[i] =  0;


  for(int j = 0; j< nb_process; j++)
  {
     for(int i = 0; i< nb_process; i++)
     {
        if(fini[i] == 0 && vector_inferieur_egale(besoins_client[i], table_travail, nb_ressources))
        {
          additionner_vectors(table_travail, besoins_client[i], nb_ressources);
          fini[i] = 1;

        }

     }
  }
  return (si_vector_zero(fini, nb_process)) ? true : false;

}

/// Fonctions utilitaires
bool
si_vector_zero(int *vector, int len)
{
  for(int i = 0; i< len; i++)
   if(vector[i] != 1)
      return false;
  return true;
}

bool
vector_inferieur_egale(int *vector1, int* vector2, int len){

  for(int i = 0; i< len; i++)
   {
   	if(vector1[i] > vector2[i])
      return false;
   }
  return true;
}

bool
vector_superior_equal(int *vector1, int* vector2, int len){
  for (int i = 0; i <len; i++){
    if(vector1[i]<vector2[i])
       return false;
  }
  return true;
}
