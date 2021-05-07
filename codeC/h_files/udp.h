#ifndef UDP_H
#define UDP_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <netinet/in.h>
#include <poll.h>
#include <fcntl.h>

sig_atomic_t volatile g_running;

void sig_handler(int signum);

int connexion_udp(int port, char * ip);
#endif