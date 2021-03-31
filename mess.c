#include "h_files/mess.h"
#include "h_files/manager.h"

/* ask ip, id, message for [MESS id message] */
char * type_mess(char * request, char * id) {
    memset(request, '\0', sizeof(char) * 156);
    char * ip = malloc(sizeof(char) * IPSIZE);
    memset(ip, '\0', sizeof(char) * IPSIZE);
    which_ip_id_message(ip, "ip adress : ", IPSIZE);
    char * tmp = malloc(sizeof(char) * 156);
    strcat(tmp, "MESS ");
    strcat(tmp, id);
    strcat(tmp, " ");
    char * message = malloc(sizeof(char) * MESSSIZE);
    memset(message, '\0', sizeof(char) * MESSSIZE);
    which_ip_id_message(message, "message : ", MESSSIZE);
    message = fill_hashtag_or_zero(message, MESSSIZE, "#");
    strcat(tmp, message);
    strcat(tmp, "\n");
    strcpy(request, tmp);
    free(tmp);
    free(ip);
    free(id);
    free(message);
    return request;
}

/* Wait [ACKM] message from broadcaster */
int recv_for_mess(int descr) {
    char tmp[5];
    int size_rec;
    size_rec = recv(descr, tmp, 4 * sizeof(char), 0);
    tmp[size_rec] = '\0';
    printf("%s\n", tmp);
    return 0;
}