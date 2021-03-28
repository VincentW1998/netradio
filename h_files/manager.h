#ifndef MANAGER_H
#define MANAGER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

#define BUFFSIZE 512

char * completeHashtag(char * id, int idOrMess);

int printPrompt();

int typeMessage(char * type);

int printMenu();

char * typeLAST();

int printError(char * mess);

int askPort();

int askIp(char * ip);

int recvLAST(int descr);

int recvLIST(int descr);

#endif