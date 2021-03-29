#ifndef MANAGER_H
#define MANAGER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

#define BUFFSIZE 512

char * completeHashtagOrZero(char * id, int idOrMess, char * symbol);

int askIP_ID_Message(char * str, char * phrase);

int printPrompt();

int printMenu();

int printError(char * mess);

int askPort();


// int recvLIST(int descr);

#endif