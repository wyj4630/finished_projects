#include "client_thread.h"
#include <stdlib.h>
#include <netinet/in.h>
#include <netdb.h>

#include <strings.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>

#include <sys/types.h>
#include <sys/poll.h>
#include <sys/socket.h>

int
main (int argc, char *argv[argc + 1])
{
  if (argc < 5)
  {
    fprintf (stderr, "Usage: %s <port-nb> <nb-clients> <nb-requests> <resources>...\n",
        argv[0]);
    exit (1);
  }

  port_number = atoi (argv[1]);
  int num_clients = atoi (argv[2])-2;
  num_request_per_client = atoi (argv[3]);
  num_resources = argc - 4;

  provisioned_resources = malloc (num_resources * sizeof (int));
  
  for (unsigned int i = 0; i < num_resources; i++)
    provisioned_resources[i] = atoi (argv[i + 4]);

  client_thread *client_threads
    = malloc (num_clients * sizeof (client_thread));
  for (unsigned int i = 0; i < num_clients; i++)
    ct_init (&(client_threads[i]));

  for (unsigned int i = 0; i < num_clients; i++)
  {
    ct_create_and_start (&(client_threads[i]));
  }

  ct_wait_server (client_threads);

  free(provisioned_resources);

  // Affiche le journal.
  st_print_results (stdout, true);
  FILE *fp = fopen("client.log", "w");
  if (fp == NULL)
  {
    fprintf(stderr, "Could not print log");
    return EXIT_FAILURE;
  }
  st_print_results (fp, false);
  fclose(fp);

  return EXIT_SUCCESS;
}
