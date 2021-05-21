#ifndef MANAGER_H
#define MANAGER_H

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>

#define BUFFSIZE 512
#define IDSIZE 8
#define MESSSIZE 140
#define IPSIZE 15
#define SIZENBMESS 3
#define GOODLEN 161

char * error_len;
char * error_ipport;
char tty[20];

char * fill_hashtag_or_zero(char * id, int idOrMess, char * symbol);

char * remove_hashtag(char * str);

int which_ip_id_message(char * str, char * phrase, int max_length);

int print_prompt();

int print_menu();

int print_error(char * mess);

int which_port(char * m);
#endif