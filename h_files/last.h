#ifndef LAST_H
#define LAST_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

char * type_last(char * request);

int recv_for_last(int descr);

#endif