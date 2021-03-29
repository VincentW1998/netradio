#include "h_files/last.h"

/* return message [LAST nb_mess] */
char * typeLAST(char * request) {
    int n;
    char * ask = "how many message do you want ? (0, 999) : ";
    write(1, ask, strlen(ask));
    char * tmp = malloc(sizeof(char) * 10);
    memset(tmp, '\0', sizeof(char) * 10);
    strcat(tmp, "LAST ");
    char  nb_mess[4];
    if ((n = read(0, nb_mess, 4) > 0)) {
        strcat(tmp, nb_mess);
    }
    strcpy(request, tmp);
    return tmp;
}

/* par of client code for LAST request */
int recvLAST(int descr) {
    char buff[100];
    int size_rec;
    while(strcmp(buff, "ENDM") != 0) {
        size_rec = recv(descr, buff, 99 * sizeof(char), 0);
        buff[size_rec] = '\0';
        printf("%s\n", buff);
    }
    printf("%s\n", buff);
    return 0;
}