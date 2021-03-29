#include "h_files/mess.h"
#include "h_files/manager.h"


char * typeMESS(char * request) {
    // int port;
    char ip[16];
    // port = askPort();
    askIP_ID_Message(ip, "ip adress : ");
    char * tmp = malloc(sizeof(char) * 156);
    strcat(tmp, "MESS ");
    char * id = malloc(sizeof(char) * 9);
    askIP_ID_Message(id, "id : ");
    id = completeHashtagOrZero(id, 8, "#");
    strcat(tmp, id);
    strcat(tmp, " ");
    char message[140];
    askIP_ID_Message(message, "message : ");
    strcat(tmp, message);
    strcat(tmp, "\n");
    printf("tmp : %s", tmp);
    return tmp;
}


int recvMESS(int descr) {
    char tmp [5];
    int size_rec;
    size_rec = recv(descr, tmp, 4 * sizeof(char), 0);
    tmp[size_rec] = '\0';
    printf("%s\n", tmp);
    return 0;
}