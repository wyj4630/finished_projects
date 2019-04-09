#ifndef TP2_COMMON_H
#define TP2_COMMON_H

#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>

//POSIX library for threads
#include <pthread.h>
#include <unistd.h>

#include <sys/types.h>
#include <poll.h>
#include <sys/socket.h>

enum cmd_type {
  BEGIN,
  CONF,
  INIT,
  REQ,
  ACK,// Mars Attack
  WAIT,
  END,
  CLO,
  ERR,
  NB_COMMANDS
};

typedef struct cmd_t {
  enum cmd_type cmd;
  int nb_args;
  char *args[10];
} cmd_t;

ssize_t read_socket(int sockfd, char *buffer, size_t obj_sz, int timeout);
int split_string(char *inputString, char **tokens ,char *tok);
int is_command(char **argm, char *args);
int generate_Random_number(int upper);
void build_command(char *cmd_str ,enum cmd_type tc, int c_id, int *args, int num_resources);
void build_command_error(char *cmd_str ,enum cmd_type tc, char *args);
void get_ressources_vector(char **commande, int *ressources_demande);
void additionner_vectors(int *vector1, int* vector2, int len);

#endif
//BEGIN 1 7382479
//ACK 1 7382479

//ACK 0

