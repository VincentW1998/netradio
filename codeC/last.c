#include "h_files/last.h"
#include "h_files/manager.h"

/* return message [LAST nb_mess] */
char * type_last(char * request) {
    char * nb_mess = malloc(sizeof(char) * 4);
    char * ask = "how many message do you want ? (0, 999) : ";
    which_ip_id_message(nb_mess, ask, SIZENBMESS);
    nb_mess = fill_hashtag_or_zero(nb_mess, SIZENBMESS, "0");
    char * tmp = malloc(sizeof(char) * 10);
    memset(tmp, '\0', sizeof(char) * 10);
    strcat(tmp, "LAST ");
    strcat(tmp, nb_mess);
    strcat(tmp, "\r\n");
    strcpy(request, tmp);
    free(nb_mess);
    free(tmp);
    return "";
}

/* receive messages [OLDM ...] for LAST request */
int recv_for_last(int descr) {
    char buff[162];
    int size_rec = GOODLEN;
    while(1) {
        memset(buff, '\0', sizeof(char) * size_rec);
        size_rec = recv(descr, buff, GOODLEN* sizeof(char), 0);
        buff[size_rec] = '\0';
        if(!strcmp(buff, "ENDM\r\n")) {
            printf("ENDM received !\n");
            return 0;
        }
        if(size_rec != GOODLEN) {
            printf(error_len, size_rec);
            return -1;
        }
        buff[size_rec-2] = '\0'; // remove \r\n
        char * str = remove_hashtag(buff); // remove #
        printf("Received : %s\r\n", str);
    }
    return 0;
}