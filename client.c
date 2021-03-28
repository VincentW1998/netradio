#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <netinet/in.h>
#include "h_files/manager.h"

int connexion(int port, char * request, char * ip) {
    struct sockaddr_in adress_sock;
    memset(&adress_sock, 0, sizeof(struct sockaddr_in));
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(port);

    struct addrinfo * first_info;
    int v = getaddrinfo(ip, NULL, NULL, &first_info);
    if (v != 0) {
        printf("Erreur avec addrinfo\n");
        exit(-1);
    }

    struct addrinfo * current_info = first_info;
    int done = 0; // pour savoir si on a trouve la bonne adresse IP
    while(!done && current_info != NULL) {
        if(current_info -> ai_family == AF_INET) {
            struct sockaddr_in * addr_in = (struct sockaddr_in*) current_info -> ai_addr;
            adress_sock.sin_addr = addr_in -> sin_addr;
            done = 1;
        }
        current_info = current_info -> ai_next;
    }
    if(done == 0) {
        printf("localhost pas trouvee");
        exit(-1);
    }

    int descr = socket(PF_INET, SOCK_STREAM, 0);
    int r = connect(descr, (struct sockaddr *) &adress_sock, sizeof(struct sockaddr_in));

    if (r != -1) {
        
        send(descr, request, strlen(request), 0);
        // char buff[100];
        // int size_rec;
        /* for LAST REQUEST*/
        // while(strcmp(buff, "ENDM") != 0) {
        //     size_rec = recv(descr, buff, 99 * sizeof(char), 0);
        //     buff[size_rec] = '\0';
        //     printf("%s\n", buff);
        // }
        // printf("%s\n", buff);
        // recvLAST(descr);
        recvLIST(descr);

        close(descr);
    }
    return 0;
}

int main(int argc, char ** argv) {
    if (argc!=2) {
        printf("need an identifiant (length 8 max)\n");
        return 0;
    }
    if (strlen(argv[1]) > 8) {
        printf("max length of id is 8, please choose a shorter id\n");
        return 0;
    }
    char * id = completeHashtag(argv[1], 8);
    printf("identifiant : %s\n", id);
    int n;
    char line[BUFFSIZE];
    int nbArgLine;
    char * argLine[3]; // argument of line
    memset(argLine, '\0', 3 * sizeof(argLine[0]));
    char newString[BUFFSIZE]; 
    int port;
    char ip[16];

    while(1) {
        printPrompt();
        nbArgLine = 0;
        memset(line, '\0', sizeof(line));
        if((n = read(0, line, BUFFSIZE) > 0)) {
            if(!strcmp(line, "QUIT\n")) {
                break;
            }
            if(!strcmp(line, "HELP\n")) {
                printMenu();
            }

            if(!strcmp(line, "LAST\n")) {
                memset(line, '\0', sizeof(char) * strlen(line));
                port = askPort();
                typeLAST(newString);
                askIp(ip);
                connexion(port, newString, ip);
            }

            if (!strcmp(line, "LIST\n")) {
                memset(line, '\0', sizeof(char) * strlen(line));
                port = askPort();
                askIp(ip);
                connexion(port, "LIST\n", ip);

            }

            memset(line, '\0', BUFFSIZE);
        }
    }
    return 0;
}

