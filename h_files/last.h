#ifndef LAST_H
#define LAST_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

char * typeLAST(char * request);

int recvLAST(int descr);

#endif