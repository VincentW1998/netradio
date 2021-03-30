#ifndef MESS_H
#define MESS_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

char * type_mess(char * request);

int recv_for_mess(int descr);

#endif