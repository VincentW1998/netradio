#include "h_files/manager.h"

// Complete by "#" or "0"
char * fill_hashtag_or_zero(char * id, int idOrMess, char * symbol) {
    char * tmp = malloc(sizeof(char) * idOrMess);
    int len = strlen(id);
    int complete_len = idOrMess - len;
    if (complete_len > 0) {
        if(!strcmp(symbol, "#")) {
            strcat(tmp, id);
        } /* if valid */
        for (int i = 0; i < complete_len; i++) {
            strcat(tmp, symbol); 
        }
        if(!strcmp(symbol, "0")) {
            strcat(tmp, id);
        } /* if valid */
        return tmp;
    } /* if valid */
    return id;
}


/* ask port between 0 and 9999 */
int which_port() {
    char * m = "Port (0, 9998): ";
    int p = -1;
    while(p <0 || p > 9998) {
        write(1, m, strlen(m));
        scanf("%d",&p);
        while(getchar() != '\n');
        if(p < 0 || p > 9998) {
            print_error("error : enter port between 0 and 9998\n");
        } /* if valid */
    }
    return p; 
}



/* function for asking id or ip or message, depends of the situation */
int which_ip_id_message(char * str, char * phrase, int max_length) {
    int n = max_length + 2;
    while(n > max_length + 1) { // until we got the good format or length
        memset(str, '\0', sizeof(char) * max_length);
        write(1, phrase, strlen(phrase));
        n = read(0, str, BUFFSIZE);
        str[n-1] = '\0';
    } 
    return 0;
}

int print_prompt() {
    write(1, "> ", 2);
    return 0;
}

/* print error message */
int print_error(char * mess) {
    write(2, mess, strlen(mess));
    return 0;
}

int print_menu() {
    printf("[+] FILE send file to register\n");
    printf("[+] LAST give nb-mess last messages\n");
    printf("[+] LIST give all broadcaster registered by handler\n");
    printf("[+] LISTEN receive message from broadcaster\n");
    printf("[+] MESS send a message to a broadcaster\n");
    printf("[+] HELP show all commands available\n");
    printf("[+] QUIT\n");
    return 0;
}
