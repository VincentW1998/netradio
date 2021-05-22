#include "h_files/file.h"
#include "h_files/manager.h"
#define FILESIZE 1024
#define NAMESIZE 100

/* send file to register */
int send_file(int sock) {
    int n;
    int size_rec;
    char content[FILESIZE];
    FILE * file;
    char name[NAMESIZE];
    memset(name, '\0', sizeof(char) * NAMESIZE);
    char tmp[100];
    char * cancel = "-CANCELED-\n";
    char * end = "-ENDFILE-\n";

    char * askName = "Enter path file : ";
    write(1, askName, strlen(askName)); // ask path file

    n = read(0, name, NAMESIZE); // read path file
    name[n-1] = '\0';

    if((file = fopen(name, "r")) == NULL) { // open file
        send(sock, cancel, sizeof(char) * strlen(cancel), 0); // send "-CANCELED-" to register
        printf("Error open file\n");
        return -1;
    }
    strcat(name, "\n");
    send(sock, name, sizeof(char) * strlen(name), 0); // send filename to register
    
    while(fgets(content, FILESIZE, file) != NULL) { // send char by char to register
        if ((n = send(sock, content, sizeof(char) * strlen(content), 0)) == -1) {
            printf("Error send file\n");
            return -1;
        }
        memset(content, '\0', FILESIZE);
    }
    send(sock, "\n", sizeof(char), 0);
    send(sock, end, sizeof(char) * strlen(end), 0); // send -ENDFILE-
    size_rec = recv(sock, tmp, 30 * sizeof(char), 0); // wait for answer
    tmp[size_rec] = '\0';
    puts(tmp);
    fclose(file);
    return 0;
}

/* write file for DOWNLOAD command */
int write_file(int sock) {
    int size_rec;
    char  name[31];
    char content[201];
    char tmp[100];
    int fd;

    char * path = malloc(sizeof(char) * 100);
    which_ip_id_message(path, "location : ", 100); // ask where to put the file
    
    // receive file name
    size_rec = recv(sock, name, 30 * sizeof(char), 0);
    name[size_rec] = '\0';
    char * newName = remove_hashtag(name); // remove #
    if (!strcmp(newName, "-CANCELED-")) {
        printf("Error id !\n");
        return -1;
    }
    memset(name, '\0', sizeof(char) * 31);
    strcpy(tmp, path);
    strcat(tmp, newName);
    
    if (size_rec > 30) {
        printf("the file name is too long !\n");
        return -1;
    }

    if((fd = open(tmp, O_WRONLY | O_CREAT | O_TRUNC, 0644)) < 0 ) { // open file write mode
        printf("Error open file\n");
        return -1;
    }

    int total_ecrit = 0;
    int copyDup = dup(1);
    dup2(fd, 1);

    while(1) {
        memset(content, '\0', sizeof(char) * 201);
        size_rec = recv(sock, content, sizeof(char) * 200, 0);
        content[size_rec] = '\0';
        char * contentGood = remove_hashtag(content);
        if(size_rec == 0) {
            close(fd);
            dup2(copyDup, 1);
            close(copyDup);
            printf("Download success !\n");
            return 0;
        }
        total_ecrit += strlen(contentGood) + 1;
        printf("%s\n", contentGood);
        write(STDOUT_FILENO, contentGood + total_ecrit, strlen(contentGood) - total_ecrit);
        
    }
    free(path);
    return 0;
}