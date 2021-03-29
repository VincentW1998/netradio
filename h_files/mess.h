#ifndef MESS_H
#define MESS_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

char * typeMESS(char * request);

int recvMESS(int descr);

#endif