#include "h_files/list.h"


/* part of client code for LIST request */
int recv_for_list(int descr, int len) {
    char buff[100];
    int size_rec;
    size_rec = recv(descr, buff, 99 * sizeof(char), 0);
    buff[size_rec] = '\0';
    printf("%s\n", buff);
    char str_num_diff[3];
    memcpy(str_num_diff, & buff[5], 2);
    str_num_diff[2] = '\0';
    int num_diff = atoi(str_num_diff);
    if(len != 27)
        printf("list of stored files \n");
    for (int i = 0; i < num_diff; i++) {
        char tmp[len];
        size_rec = recv(descr, tmp, (len-1) * sizeof(char), 0);
        tmp[size_rec] = '\0';
        printf("%s", tmp);
    }
    return 0;
}