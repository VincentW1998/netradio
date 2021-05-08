#ifndef FILE_H
#define FILE_H 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <fcntl.h>

int send_file(int sock);

int write_file(int sock);
#endif