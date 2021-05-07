#include "h_files/udp.h"
#include "h_files/manager.h"


sig_atomic_t volatile g_running = 1;


void sig_handler(int signum) {
    if(signum == SIGINT) {
        g_running = 0;
    }
}

int connexion_udp(int port, char * ip, char * tty) {
    int sock1=socket(PF_INET,SOCK_DGRAM,0);
    int ok = 1;
    setsockopt(sock1,SOL_SOCKET,SO_REUSEADDR,&ok,sizeof(ok));
    struct sockaddr_in address_sock1;
    address_sock1.sin_family=AF_INET;
    address_sock1.sin_port=htons(port);
    address_sock1.sin_addr.s_addr = inet_addr(ip);
    int r=bind(sock1,(struct sockaddr *)&address_sock1,sizeof(struct sockaddr_in));
    if (r == -1) {
        printf("%s", error_ipport);
        return -1;
    }
    struct ip_mreq mreq;
    mreq.imr_multiaddr.s_addr=inet_addr(ip);
    mreq.imr_interface.s_addr=htonl(INADDR_ANY);
    if((r=setsockopt(sock1,IPPROTO_IP,IP_ADD_MEMBERSHIP,&mreq,sizeof(mreq))) == -1) {
      printf("Error setsockopt !\n");
      return -1;
    }

    if(r==0){
        fcntl( sock1, F_SETFL, O_NONBLOCK);
        struct pollfd p[1];
        p[0].fd=sock1;
        p[0].events=POLLIN;
        char tampon[GOODLEN+1];
        int rec=0;
        signal(SIGINT, &sig_handler); // catch CTR+C signal
        g_running = 1;
        int fd = open(tty, O_WRONLY);
        if(fd < 0) {
            printf("wrong tty !\n");
            return -1;
        }
        int total_ecrit = 0;
        int copyDup = dup(1);
        dup2(fd, 1);
        while(g_running){
            int ret=poll(p,1,0);
            if(ret>0){
                if(p[0].revents==POLLIN) {
                    rec = recv(sock1, tampon, GOODLEN* sizeof(char),0);
                    tampon[rec-2]='\0';
                    if(rec != GOODLEN) {
                        printf(error_len, rec);
                        continue;
                    }
                    char * str = remove_hashtag(tampon);
                    total_ecrit += strlen(str) + 1;
                    printf("%s\n", str);
                    write(STDOUT_FILENO, str + total_ecrit, strlen(str) - total_ecrit);
                }
            }
        }
        close(fd);
        dup2(copyDup, 1);
        close(copyDup);
    }
    signal(SIGINT, SIG_DFL);
    close(sock1);
    return 0;
}