#include "h_files/manager.h"

// Complete by "#" or "0"
char * completeHashtagOrZero(char * id, int idOrMess, char * symbol) {
    char * tmp = malloc(sizeof(char) * idOrMess);
    int len = strlen(id);
    int completeLen = idOrMess - len;

    if (completeLen > 0) {
        strcat(tmp, id);
        for (int i = 0; i < completeLen; i++) {
            strcat(tmp, symbol); 
        }
        return tmp;
    }
    return id;
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

int askIP_ID_Message(char * str, char * phrase) {
   write(1, phrase, strlen(phrase));
   int n = read(0, str, BUFFSIZE); 
   str[n-1] = '\0';
   return 0;
}


int printPrompt() {
    write(1, "> ", 2);
    return 0;
}

/* print error message */
int printError(char * mess) {
    write(2, mess, strlen(mess));
    return 0;
}

int printMenu() {
    printf("[+] LAST for ...\n");
    printf("[+] LIST for ...\n");
    printf("[+] MESS for ...\n");
    printf("[+] HELP for ...\n");
    printf("[+] QUIT for ...\n");
    printf("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111\n");
    return 0;
}
