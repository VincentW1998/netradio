#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <netinet/in.h>
#include "h_files/manager.h"

int connexion(int port, int nbOption, char ** argLine) {
    struct sockaddr_in adress_sock;
    memset(&adress_sock, 0, sizeof(struct sockaddr_in));
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(port);

    struct addrinfo * first_info;
    int v = getaddrinfo("localhost", NULL, NULL, &first_info);
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
        // char *pseudo= "WEIVincent";
        // send(descr, pseudo, strlen(pseudo), 0);
        char buff[100];
        int size_rec = recv(descr, buff, 99*sizeof(char), 0);
        buff[size_rec] = '\0';
        printf("%s", buff);
        // for(int i = 0; i < 5; i++) {
        //     char * request = "PUT Ceci est un message qui fait bientot quatre-vingt caracteres, on y est 80 pile.";
        //     send(descr, request, strlen(request), 0);
        //     sleep(2);
        //     memset(buff, 0, strlen(buff));
        //     size_rec = recv(descr, buff, 99*sizeof(char), 0);
        //     buff[size_rec] = '\0';
        //     printf("%s\n", buff);
        // }


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
    char * id = completeHashtag(argv[1]);
    printf("identifiant : %s\n", id);
    int n;
    char line[BUFFSIZE];
    int nbArgLine;
    char * argLine[3]; // argument of line
    memset(argLine, '\0', 3 * sizeof(argLine[0]));

    while(1) {
        printPrompt();
        nbArgLine = 0;
        memset(line, '\0', sizeof(buff));
        if((n = read(0, line, BUFFSIZE) > 0)) {
            if(!strcmp(line, "quit\n")) {
                break;
            }

            nbOption = sliceLine(line, argLine);
            memset(line, '\0', BUFFSIZE);
        }
        

    }
    return 0;
}

