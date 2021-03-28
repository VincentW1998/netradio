#include "h_files/manager.h"

// Complete id by "#" if the lenght is inferior than 8
char * completeHashtag(char * id, int idOrMess) {
    char * tmp = malloc(sizeof(char) * idOrMess);
    int len = strlen(id);
    int completeLen = idOrMess - len;

    if (completeLen > 0) {
        strcat(tmp, id);
        for (int i = 0; i < completeLen; i++) {
            strcat(tmp, "#");
        }
        return tmp;
    }
    return id;
}

/* complete num_mess, nb_mess, num_diff by "0" */
char * completeZero(char * nb, int len) {
    char * tmp = malloc(sizeof(char) * len);
    int l = strlen(nb);
    int completeLen = len - l;
    if (completeLen > 0) {
        for(int i = 0; i < completeLen; i++) {
            strcat(tmp, "0");
        }
        strcat(tmp, nb);
        return tmp;
    }
    return nb;
}



int printPrompt() {
    write(1, "> ", 2);
    return 0;
}

int printMenu() {
    printf("LAST for ...\n");
    printf("LIST for ...\n");
    printf("MESS for ...\n");
    printf("HELP for ...\n");
    printf("QUIT for ...\n");
    return 0;
}

/* return message [LAST nb_mess] */
char * typeLAST(char * request) {
    int n;
    char * ask = "how many message do you want ? (0, 999) : ";
    write(1, ask, strlen(ask));
    char * tmp = malloc(sizeof(char) * 8 + 1);
    memset(tmp, '\0', sizeof(char) * 8 + 1);
    strcat(tmp, "LAST ");
    char  nb_mess[4];
    if ((n = read(0, nb_mess, 4) > 0)) {
        strcat(tmp, nb_mess);
    }
    strcpy(request, tmp);
    return tmp;
}

/* ask port between 0 and 9999 */
int askPort() {
    char * m = "Port (0, 9999): ";
    int p = -1;
    while(p <0 || p > 9999) {
        write(1, m, strlen(m));
        scanf("%d",&p);
        if(p < 0 || p > 9999) {
            printError("error : enter port between 0 and 9999\n");
        }
    }
    
    return p; 
}

/* ask ip adress */
int askIp(char * ip) {
    char * m = "ip adress : ";
    write(1, m, strlen(m));
    int n = read(0, ip, BUFFSIZE);
    ip[n - 1] = '\0';
    return 0;
}

/* print error message */
int printError(char * mess) {
    write(2, mess, strlen(mess));
    return 0;
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

/* part of client code for LIST request */
int recvLIST(int descr) {
    char buff[100];
    int size_rec;
    size_rec = recv(descr, buff, 99 * sizeof(char), 0);
    buff[size_rec] = '\0';
    printf("%s\n", buff);
    char str_num_diff[3];
    memcpy(str_num_diff, &buff[5], 2);
    str_num_diff[2] = '\0';
    int num_diff = atoi(str_num_diff);
    
    for(int i = 0; i < num_diff; i++) {
        char tmp[57];
        size_rec = recv(descr, tmp, 56 * sizeof(char), 0);
        tmp[size_rec] = '\0';
        printf("%s\n", tmp);
    }
    return 0;
}

