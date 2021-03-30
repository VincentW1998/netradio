#include "h_files/mess.h"
#include "h_files/manager.h"


char * type_mess(char * request) {
    char ip[16];
    which_ip_id_message(ip, "ip adress : ");
    char * tmp = malloc(sizeof(char) * 156);
    strcat(tmp, "MESS ");
    char * id = malloc(sizeof(char) * 9);
    which_ip_id_message(id, "id : ");
    id = fill_hashtag_or_zero(id, 8, "#");
    strcat(tmp, id);
    strcat(tmp, " ");
    char message[140];
    which_ip_id_message(message, "message : ");
    strcat(tmp, message);
    strcat(tmp, "\n");
    printf("tmp : %s", tmp);
    return tmp;
}

int recv_for_mess(int descr) {
    char tmp[5];
    int size_rec;
    size_rec = recv(descr, tmp, 4 * sizeof(char), 0);
    tmp[size_rec] = '\0';
    printf("%s\n", tmp);
    return 0;
}