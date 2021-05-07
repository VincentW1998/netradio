#include "h_files/tcp.h"
#include "h_files/manager.h"
#include "h_files/mess.h"
#include "h_files/last.h"
#include "h_files/list.h"
#include "h_files/file.h"

int connexion_tcp(int port, char * request, char * ip, int cmd) {
    struct sockaddr_in adress_sock;
    memset( & adress_sock, 0, sizeof(struct sockaddr_in));
    adress_sock.sin_family = AF_INET;
    adress_sock.sin_port = htons(port);

    struct addrinfo * first_info = malloc(sizeof(struct addrinfo));
    int v = getaddrinfo(strtok(ip,"#"), NULL, NULL, &first_info);
    if (v != 0) {
        printf("Erreur avec addrinfo\n");
        return -1;
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
        return -1;
    }

    int descr = socket(PF_INET, SOCK_STREAM, 0);
    int r = connect(descr, (struct sockaddr * ) & adress_sock, sizeof(struct sockaddr_in));
    if (r != -1) {
        send(descr, request, strlen(request), 0);
        switch (cmd) {
        case 0:
            recv_for_list(descr, 57);
            break;
        case 1:
            recv_for_last(descr);
            break;
        case 2:
            recv_for_mess(descr);
            break;
        case 3:
            send_file(descr);
            break;
        case 4:
            recv_for_list(descr, 26);
            break;
        }
        close(descr);
    }
    else {
        puts(error_ipport);
        close(descr);
    }
    return 0;
}