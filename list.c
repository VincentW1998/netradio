#include "h_files/list.h"


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
        printf("%s", tmp);
    }
    return 0;
}