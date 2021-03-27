#ifndef MANAGER_H
#define MANAGER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#define BUFFSIZE 512

char * completeHashtag(char * id);

int printPrompt();

int typeMessage(char * type);

int sliceLine(char * line, char ** argLine);
#endif