#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "h_files/manager.h"
#include "h_files/mess.h"
#include "h_files/last.h"
#include "h_files/list.h"
#include "h_files/file.h"
#include "h_files/udp.h"
#include "h_files/tcp.h"
#include <signal.h>


int main(int argc, char ** argv) {
    if (argc != 2) {
        printf("need an identifiant (length 8 max)\n");
        return 0;
    }
    if (strlen(argv[1]) > 8) {
        printf("max length of id is 8, please choose a shorter id\n");
        return 0;
    }
    /* INTIALISATION VARIABLES */
    int n;
    char line[BUFFSIZE];
    char * arg_line[3]; // argument of line
    memset(arg_line, '\0', 3 * sizeof(arg_line[0]));
    char str_last[10];
    int port;
    char ip[16];
    char str_mess[156];
    /* END OF INITIALISATION VARIABLES */
    
    printf("WELCOME : %s\n", argv[1]);
    char * id_client = fill_hashtag_or_zero(argv[1], 8, "#");
    char * begin = "Type HELP : print all commands available !\n";
    write(1, begin, strlen(begin));


    while (1) {
        print_prompt();
        memset(line, '\0', sizeof(line));
        if ((n = read(0, line, BUFFSIZE) > 0)) {
            if (!strcmp(line, "QUIT\n")) {
                break;
            }
            else if (!strcmp(line, "HELP\n")) {
                print_menu();
            }

            else if (!strcmp(line, "LAST\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                type_last(str_last);
                which_ip_id_message(ip, "diffuser's ip adress : ", IPSIZE);
                connexion_tcp(port, str_last, ip, 1);
            }

            else if (!strcmp(line, "LIST\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "register's ip address : ", IPSIZE);
                connexion_tcp(port, "LIST\r\n", ip, 0);

            }

            else if (!strcmp(line, "MESS\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "diffuser's ip adress : ", IPSIZE);
                type_mess(str_mess, id_client);
                connexion_tcp(port, str_mess, ip, 2);
            }

            else if (!strcmp(line, "LISTEN\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "ip adress of multicast: ", IPSIZE);
                connexion_udp(port, ip);
            }
            else if (!strcmp(line, "FILE\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "register's ip address : ", IPSIZE);
                connexion_tcp(port, "FILE\r\n", ip, 3);
            }
            else if (!strcmp(line, "LISTFILES\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "diffuser's ip adress : ", IPSIZE);
                connexion_tcp(port, "LISTFILES\r\n", ip, 4);
            }
            else {
                print_error("didn't find your cmd, try again\n");

            }
            memset(line, '\0', BUFFSIZE);
        }
    }
    return 0;
}
