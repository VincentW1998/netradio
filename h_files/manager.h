#ifndef MANAGER_H
#define MANAGER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

#define BUFFSIZE 512

char * fill_hashtag_or_zero(char * id, int idOrMess, char * symbol);

int which_ip_id_message(char * str, char * phrase);

int print_prompt();

int print_menu();

int print_error(char * mess);

int which_port();
#endif