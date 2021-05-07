#include "mycat.h"
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>








int main(int argc, char ** argv){
    if (argc != 2) {
        perror("Oublie du pathname");
    }

    int fd = open(argv[1], O_WRONLY|O_CREAT|O_TRUNC, 0644);
    

    dup2(fd, 1);
    mycat();
    close(fd);
    
}
