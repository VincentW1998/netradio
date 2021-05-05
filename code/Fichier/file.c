#include "h_files/file.h"
#include "h_files/manager.h"
#define FILESIZE 1024
#define NAMESIZE 25

int send_file(int sock) {
    int n;
    int size_rec;
    char content[FILESIZE];
    FILE * file;
    char name[NAMESIZE];
    char tmp[30];
    char cancel = "Canceled\n";

    char * askName = "Enter path file : ";
    memset(name, '\0', sizeof(char) * NAMESIZE);
    write(1, askName, strlen(askName));
    n = read(0, name, NAMESIZE);
    name[n-1] = '\0';

    if((file = fopen(name, "r")) == NULL) {
        send(sock, cancel, sizeof(char) * strlen(cancel), 0);
        printf("Error open file\n");
        return -1;
    }
    strcat(name, "\n");
    send(sock, name, sizeof(char) * strlen(name), 0);
    
    while((n = fgets(content, FILESIZE, file)) != NULL) {
        if ((n = send(sock, content, sizeof(char) * strlen(content), 0)) == -1) {
            printf("Error send file\n");
            return -1;
        }
        memset(content, '\0', FILESIZE);
    }
    // size_rec = recv(sock, tmp, 30 * sizeof(char), 0);
    // tmp[size_rec] = '\0';
    // puts(tmp);
    return 0;
}
