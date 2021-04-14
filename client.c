#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <netinet/in.h>
#include "h_files/manager.h"
#include "h_files/mess.h"
#include "h_files/last.h"
#include "h_files/list.h"
#include <signal.h>


int connexion_tcp(int port, char * request, char * ip, int cmd) {
    struct sockaddr_in adress_sock;
    memset( & adress_sock, 0, sizeof(struct sockaddr_in));
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(port);

    struct addrinfo * first_info = malloc(sizeof(struct addrinfo));
    int v = getaddrinfo(strtok(ip,"#"), NULL, NULL, &first_info);
    if (v != 0) {
        printf("Erreur avec addrinfo\n");
        exit(-1);
    }

    struct addrinfo * current_info = first_info;
    int done = 0;
    while (!done && current_info != NULL) {
        if (current_info -> ai_family == AF_INET) {
            struct sockaddr_in * addr_in = (struct sockaddr_in * ) current_info -> ai_addr;
            adress_sock.sin_addr = addr_in -> sin_addr;
            done = 1;
        }
        current_info = current_info -> ai_next;
    }
    if (done == 0) {
        printf("localhost pas trouvee");
        exit(-1);
    }

    int descr = socket(PF_INET, SOCK_STREAM, 0);
    int r = connect(descr, (struct sockaddr * ) & adress_sock, sizeof(struct sockaddr_in));
    if (r != -1) {
        send(descr, request, strlen(request), 0);
        switch (cmd) {
        case 0:
            recv_for_list(descr);
            break;
        case 1:
            recv_for_last(descr);
            break;
        case 2:
            recv_for_mess(descr);
            break;
        }
        close(descr);
    }
    return 0;
}

sig_atomic_t volatile g_running = 1;

void sig_handler(int signum) {
    if(signum == SIGINT) {
        g_running = 0;
    }
}

int connexion_udp(int port, char * ip) {
    int sock=socket(PF_INET,SOCK_DGRAM,0);
    int ok=1;
    int r=setsockopt(sock,SOL_SOCKET,SO_REUSEPORT,&ok,sizeof(ok));
    // salle de TP a la fac !
    // int r = setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, &ok, sizeof(ok));
    struct sockaddr_in address_sock;
    address_sock.sin_family=AF_INET;
    address_sock.sin_port=htons(port);
    address_sock.sin_addr.s_addr=htonl(INADDR_ANY);
    r=bind(sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
    struct ip_mreq mreq;
    mreq.imr_multiaddr.s_addr=inet_addr(ip);
    mreq.imr_interface.s_addr=htonl(INADDR_ANY);
    r=setsockopt(sock,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq));
    char tampon[100];
    signal(SIGINT, &sig_handler); // catch CTR+C signal
    g_running = 1;
    while(g_running) {// CTR+C break the loop
        int rec=recv(sock,tampon,100,0);
        tampon[rec]='\0';
        printf("Message recu : %s\n",tampon);
    }
    close(sock);
    return 0;
}

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
    int nb_arg_line;
    char * arg_line[3]; // argument of line
    memset(arg_line, '\0', 3 * sizeof(arg_line[0]));
    char str_last[10];
    int port;
    char ip[16];
    char str_mess[156];
    /* END OF INITIALISATION VARIABLES */

    char * id_client = fill_hashtag_or_zero(argv[1], 8, "#");
    printf("identifiant : %s\n", id_client);
    char * begin = "Type HELP : print all commands available !\n";
    write(1, begin, strlen(begin));


    while (1) {
        print_prompt();
        nb_arg_line = 0;
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
                which_ip_id_message(ip, "ip adress : ", IPSIZE);
                connexion_tcp(port, str_last, ip, 1);
            }

            else if (!strcmp(line, "LIST\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "ip adress : ", IPSIZE);
                connexion_tcp(port, "LIST\n", ip, 0);

            }

            else if (!strcmp(line, "MESS\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "ip adress : ", IPSIZE);
                type_mess(str_mess, id_client);
                connexion_tcp(port, str_mess, ip, 2);
            }

            else if (!strcmp(line, "LISTEN\n")) {
                memset(line, '\0', sizeof(char) * BUFFSIZE);
                port = which_port();
                which_ip_id_message(ip, "ip adress of multicast: ", IPSIZE);
                connexion_udp(9998, ip);
            }
            else {
                print_error("didn't find your cmd, try again\n");

            }
            memset(line, '\0', BUFFSIZE);
        }
    }
    return 0;
}