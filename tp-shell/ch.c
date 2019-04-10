
/*
 ============================================================================
 Name        : ch.c
 Author      :Samir Hachemi &&  Wang Yi Jing
 Version     :
 Description : Impelemtation shell in C
 ============================================================================
 */
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <ctype.h>
#include <errno.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdbool.h>
extern int errno;

typedef void (*sighandler_t)(int);
volatile bool terminate = false;



/**
 * Une fonction pour gérer le Ctrl Z
 */
void handle_signal(int signo)
{
	printf("\n[NOTRE_SHELL ] ");
	fflush(stdout);
}

/**
 * Une fonction pour spliter commande selon une sous chaine donnée
 */
 int split_string(char *inputString,char **tokens ,char *tok)
 {
    char last_char[1];
    last_char[0] = inputString[strlen(inputString) -1]; //récupération du dernier caractère
                                                        //pour ne pas le predre lors d'une sous chaine "&&"
	tokens[0] = strtok(inputString, tok);

    int counter = 0;
    while(tokens[counter]!= NULL){
        counter++;
        tokens[counter] =  strtok(NULL, tok);
    }
    if(last_char[0] == '&')
        strncat(inputString, last_char, 1); //Ajouter le caractère dans le cas d'un background

    return counter;
}

/**
 *fonction pour skiper les espacesautour de la commande
 */
void  parse_command(char *ligne, char **_argv)
{
        while (*ligne != '\0') {                                // si pas fin de ligne
              while (*ligne == ' ' || *ligne == '\t' || *ligne == '\n')
                *ligne++ = '\0';
                                                                //remplacer les espaces blancs par 0
            *_argv++ = (strcmp(ligne, " ") <= 0)? NULL : ligne; //sauvegarder la position de l'argument

              while (*ligne != '\0' && *ligne != ' ' &&
                     *ligne != '\t' && *ligne != '\n')
                ligne++;                                         // skiper les charactères inutiles

     }

     *_argv = '\0';                 // marquer la fin de l'argument
}

/**
 * Fonction pour checher la rediraction et
 * renvoie l'index du symbole >
 */

 int check_redirectin(char **_args){

    int i = 0;
    bool is_rederaction = false;

    while(_args[i] != NULL && !is_rederaction )
    {
        if(strstr(_args[i], ">") != NULL)
            is_rederaction = true;

        i++;
    }
    return (is_rederaction == true)? i : -1;
 }

/**
 * Fonction pour checher la rediraction et
 * renvoie l'index du symbole &
 */
 int check_background_execution(char **_args){

    int i = 0;
    bool on_back = false;
    while(_args[i] != NULL && !on_back )
    {
        if(strstr(_args[i], "&") != NULL)
            on_back = true;
        i++;
    }
    return (on_back)?i:-1;
 }


/**
 *Fonction pour l'execution d'une seule commande
 *et retourne l'état de l'execution
 */
int execute_command(char *_command){

    pid_t  pid;
    int    stat;
    int name_file_index;
    int on_background = -1;
    int file_disc;
    char *_args[64];                    //tableau pour sotcker les arguments d'une commande


    parse_command(_command, _args);                      //skiper les espaces
    if((name_file_index = check_redirectin(_args)) != -1)       //récupérer l'index du nom du fichier
        _args[name_file_index-1] = NULL;                        //skiper l'argument >

    if((on_background = check_background_execution(_args)) != -1)  // verivier l'execution en background
    _args[on_background-1] = NULL;

    if ((pid = fork()) < 0)
    {
        perror("* ERROR: child prosses not created\n");         //Forck pas réussi
        exit(1);
    }
    else if (pid == 0)                                          //zone enfant
    {

        if(name_file_index >= 0)                                //gerer la redirection
        {

            file_disc = creat(_args[name_file_index], 0644);
            close(1);

            dup(file_disc);
            close(file_disc);

        }
        if ((stat = execvp(*_args, _args))< 0) {                //executer la commende
            printf("ERROR: %s\n", strerror(errno));
            exit(1);
        }

    }
    else                                                        //zone parent
    {
        if(on_background == -1)                                 //pas d'attente pour une exction background
        {
             while (wait(&stat) != pid);
        }

    }
    return WEXITSTATUS(stat);                                   //retourner l'état de l'execution
}



/**
 * Fonction pour gerer une commande if
 * le code ne prend pas en charge des structures complexes dans le bock if
 */
 int execute_if_command(char *if_commands )
 {
    char *condition_blocks[64];     //tableau pour sotcker les arguments d'une if commande
    int stat ;
    int i = 0;
    split_string(if_commands, condition_blocks, ";");               //spliter le block if
    while(condition_blocks[i++] != NULL)

        stat = prepare_commands(strstr(condition_blocks[0], "if") + 2);   //executer la condition
        if(stat != 1 )
        {
            stat = execute_command(strstr(condition_blocks[1], "do") + 2);   //si l'execution de la condition est réussi

        }else                                                                 // si condition pas execuer
        {
            if(condition_blocks[2] != NULL)                             //verifier s'il ya deuxième block pour
            {
                stat = execute_command(condition_blocks[2]);            //Executer le deuxième nlock
            }
        }
        return stat;                                                    //returner l'etat de l'execution de la commande
 }

/**
 * Une fonction pour délimiter les blocks de commendes
 */
int prepare_commands(char *input_string){

    char *or_commands[64];              //tableau pour sotcker les commandes indépendantes
    char *and_commands[64];             //tableau pour sotcker les commandes dépendantes
    int statut;
    int i = 0;
    int tokens_counter = 0;             //conter le nombre de commandes

    split_string(input_string, or_commands ,"||");          //spliter en commandes indépendantes

    while(or_commands[i] != NULL)                           //pour chaque commande indépendante
    {
        tokens_counter = split_string(or_commands[i], and_commands, "&&");  //spliter à des commandes dépendantes

        if(tokens_counter > 1)                          //s'il y ades commandes dépendante
        {
            int j=0;
            while(and_commands[j] != NULL)              //pour chaque commande dépendante
            {

                char *srch = strchr(and_commands[j],';'); // chercher le char 'B;'
                if(srch != NULL)
                {
                    if((statut = execute_if_command(and_commands[j])) == 1) break;
                }
                else
                {


                    if((statut = execute_command(and_commands[j])) == 1) break;

                }
                j++;
            }
        }else
        {

                char *srch = strchr(or_commands[i],';');
                if(srch != NULL)
                {
                    if((statut = execute_if_command(or_commands[i])) == 1) break;
                }
                else
                {
                    statut = execute_command(or_commands[i]);
                }
        }
        i++;
    }
    return statut;
}



/**
 * fonction principale
 */
int main(int argc, char *argv[], char *envp[]) {

    char *input_string = (char *)malloc(sizeof(char) * 100);    //allocation d'emplacement d'une taille 100 char
    char c;
    signal(SIGINT, SIG_IGN);
    signal(SIGTSTP, handle_signal);
    printf("[NOTRE_SHELL ] ");
    fflush(stdout);
    while(c != EOF) {                                           //boucle du shell
        c = getchar();                                          //lire les chars en entrée
        switch(c)
        {
            case '\n':

                if(input_string[0] == '\0')                 //si la saisie est vide
                {
                    printf("[NOTRE_SHELL ] ");              //retourner au shell e
                } else
                {
                    prepare_commands(input_string);         //executer la commande
                    //if(terminate){
                     //   printf("\n");
                     //   terminate=false;
                   // }
                    printf("[NOTRE_SHELL ] ");
                    bzero(input_string, 100);              //mettre à zero l'espace alloué
                }
                break;

            default: strncat(input_string, &c, 1);          //stocker les chars dans input_string
                break;
        }
    }

    free(input_string);             //liberer l'espace alloué
    printf("\n");
	return EXIT_SUCCESS;
}
