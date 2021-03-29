#include "h_files/last.h"
#include "h_files/manager.h"

/* return message [LAST nb_mess] */
char * typeLAST(char * request) {
    char  * nb_mess = malloc(sizeof(char) * 4);
    char * ask = "how many message do you want ? (0, 999) : ";
    askIP_ID_Message(nb_mess, ask);
    char * tmp = malloc(sizeof(char) * 10);
    memset(tmp, '\0', sizeof(char) * 10);
    strcat(tmp, "LAST ");
    strcat(tmp, nb_mess);
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