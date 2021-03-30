#include "h_files/last.h"
#include "h_files/manager.h"

/* return message [LAST nb_mess] */
char * type_last(char * request) {
    char * nb_mess = malloc(sizeof(char) * 4);
    char * ask = "how many message do you want ? (0, 999) : ";
    which_ip_id_message(nb_mess, ask);
    nb_mess = fill_hashtag_or_zero(nb_mess, 3, "0");
    char * tmp = malloc(sizeof(char) * 10);
    memset(tmp, '\0', sizeof(char) * 10);
    strcat(tmp, "LAST ");
    strcat(tmp, nb_mess);
    strcat(tmp, "\n");
    strcpy(request, tmp);
    printf("LAST : %s", request);
    return tmp;
}

/* par of client code for LAST request */
int recv_for_last(int descr) {
    char buff[100];
    int size_rec = -1;
    while (strcmp(buff, "ENDM") != 0 && size_rec != 0) {
        size_rec = recv(descr, buff, 99 * sizeof(char), 0);
        buff[size_rec] = '\0';
        printf("%s\n", buff);
    }
    printf("%s\n", buff);
    return 0;
}