#include "h_files/udp.h"
#include "h_files/manager.h"

/* Display DIFF from broadcast to another terminal */
void* communication(void *arg){
    int so=*((int *) arg);
    int rec;
    int total_ecrit = 0;
    FILE* outStream;
    FILE* out = fopen(tty, "w");
    if(out){
        outStream = out;
    } else {
        outStream = stdout;
    }
    while(1) {
        char tampon[GOODLEN+1];
        rec = recv(so, tampon, GOODLEN,0);
        tampon[rec-2]='\0';
        if(rec != GOODLEN) {
            printf(error_len, rec);
            continue;
        }
        char * str = remove_hashtag(tampon);
        total_ecrit += strlen(str) + 1;
        fprintf(outStream, "%s\n", str);
    }
    return NULL;
}

/* UDP for listening broadcast using pthread*/
int connexion_udp(int port, char * ip) {
    int* sock = malloc(sizeof(int)*1);
    *sock = socket(PF_INET,SOCK_DGRAM,0);
    int ok = 1;
    setsockopt(*sock, SOL_SOCKET, SO_REUSEPORT, &ok, sizeof(ok));

    struct sockaddr_in address_sock;
    address_sock.sin_family = AF_INET;
    address_sock.sin_port = htons(port);
    address_sock.sin_addr.s_addr = inet_addr(ip);
    int r = bind(*sock,(struct sockaddr *)&address_sock,sizeof(struct sockaddr_in));
    if (r == -1) {
        printf("%s", error_ipport);
        return -1;
    } 
    struct ip_mreq mreq;
    mreq.imr_multiaddr.s_addr = inet_addr(ip);
    mreq.imr_interface.s_addr = htonl(INADDR_ANY);
    if(setsockopt(*sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof(mreq)) != 0){
        printf("Error setsockopt !\n");
        return -1;
    }
    if(*sock != 0) {
        pthread_t th;
        int r1 = pthread_create(&th, NULL, communication, sock);
        if(r1 != 0) {
            exit(0);
        }
    }
    return 0;
}