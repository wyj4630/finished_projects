#include "common.h"
#include <strings.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
ssize_t read_socket(int sockfd, char *buf, size_t obj_sz, int timeout) {
  int ret;
  int len = 0;

  struct pollfd fds[1];
  fds->fd = sockfd;
  fds->events = POLLIN;
  fds->revents = 0;

  //do {
    // wait for data or timeout
    ret = poll(fds, 1, timeout);

    if (ret > 0) {
      if (fds->revents & POLLIN) {
          ret = recv(sockfd, buf + len, obj_sz - len, 0);
          if (ret < 0) {
            // abort connection
            perror("recv()");
            return -1;
          }
          len += ret;
      }
    } else {
      // TCP error or timeout
      if (ret < 0) {
        perror("poll()");
      }
     // break;
    }
 // } while (ret != 0 && len < obj_sz);
  return ret;
}

int split_string(char *inputString, char **tokens ,char *tok)
{
    tokens[0] = strtok(inputString, tok);

    int counter = 0;
    while(tokens[counter]!= NULL){
        counter++;
        tokens[counter] =  strtok(NULL, tok);
    }  
    return counter;  
}


int generate_Random_number(int upper)  
{ 
    int i = (rand() % (upper + 1)) ; 
    return i;
} 

void build_command(char *cmd_str ,enum cmd_type tc, int c_id, int *args, int num_resources)
{

  switch(tc){

    case BEGIN:
    case WAIT:
    case CLO:
        sprintf(cmd_str, "%d %d %d%c", tc, num_resources, c_id, '\0');
    break;
    case CONF:
    case REQ:
    case INIT:
    {
      if(tc != CONF)
        sprintf(cmd_str, "%d %d %d%c", tc, num_resources, c_id, '\0');
      else
        sprintf(cmd_str, "%d %d%c", tc, num_resources, '\0');
      
      for (int i = 0; i < num_resources; i++)
      {
     
      int length = snprintf(NULL, 0, "%d%c",args[i], '\0');
      char *arg = malloc(length +1);
      strcat(cmd_str, " ");       
      snprintf(arg,length + 1, "%d%c", args[i], '\0');

      strcat(cmd_str, arg); 

      free(arg);
      } 
    }
    break;
    case ACK:
    case END:
        if(num_resources == 0)
          sprintf(cmd_str, "%d %d%c", tc, num_resources, '\0');
        else
          sprintf(cmd_str, "%d %d %d%c", tc, num_resources, c_id, '\0');
    break;
    default :
    break;
  }
  
}  
  
void build_command_error(char *cmd_str ,enum cmd_type tc, char *args)
{
  sprintf(cmd_str, "%d %d %s", tc, strlen(args), args);

}
void get_ressources_vector(char **commande, int *ressources_demande)
{
  for(unsigned int i = 0; i < atoi(commande[1]) -1 ; i++)
    ressources_demande[i] = atoi(commande[i+3]);

}
void  additionner_vectors(int *vector1, int* vector2, int len)
{
  for(int i = 0; i< len; i++)
   vector1[i] +=  vector2[i];
}