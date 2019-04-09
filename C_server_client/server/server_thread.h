#ifndef SERVER_THREAD_H
#define SERVER_THREAD_H

#include "common.h"

extern bool accepting_connections;

typedef struct server_thread server_thread;
struct server_thread
{
  unsigned int id;
  pthread_t pt_tid;
  pthread_attr_t pt_attr;
};

void st_open_socket (int port_number);
void st_init (void);
void st_process_request (server_thread *, int);
void st_signal (void);
void *st_code (void *);
void initialize_resourses();//char **command, int *client_resources);
void st_print_results (FILE *, bool);
bool vector_inferieur_egale(int *vector1, int* vector2, int len);
bool vector_superior_equal(int *vector1, int* vector2, int len);
bool detecte_safe_state(int *disponible, int **besoins_client, int **resources_allouees,
						int nb_ressources,  int nb_process);
bool si_vector_zero(int *vector, int len);
void banquier_algo();
#endif
