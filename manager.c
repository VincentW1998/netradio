#include "h_files/manager.h"

// int typesMessage(char * types) {
//     int nbType = 3;
//     char * allType[nbType] = {"LAST", "LIST", "MESS"};
//     int idType = -1;
    
//     for (int i = 0; i < nbType; i ++) {
//         if (!strcmp(allType[i], types))
//             idType = i;
//     }

//     switch(idType) {
//         case -1 :
//             return -1;
//     }
// }

// Complete id by "#" if the lenght is inferior than 8
char * completeHashtag(char * id, int idOrMess) {
    char * tmp = malloc(sizeof(char) * idOrMess);
    int len = strlen(id);
    int completeLen = idOrMess - len;

    if (completeLen > 0) {
        strcat(tmp, id);
        for (int i = 0; i < completeLen; i++) {
            strcat(tmp, "#");
        }
        return tmp;
    }
    return id;
}



int printPrompt() {
    write(1, "> ", 2);
    return 0;
}

// slice line with space delimeter
// int sliceLine(char * line, char ** argLine){
//     char * tmp = malloc(strlen(line) + 1);
//     strcpy(tmp, line);

//     char * token = NULL;
//     int nbOption = 0;
//     //   int l = strlen(line);

//     while((token = strtok_r(tmp, " \n", &tmp)) != NULL) {
//         argLine[nbOption] = malloc(strlen(token) + 1);
//         if (argLine[nbOption] == NULL) {
//             printf("failed to allocated memory for argLine");
//             return -1;
//         }

//         strcat(argLine[nbOption], token);
//         nbOption ++;
//     }
//     //   if (nbOption == 0) {
//         // argLine[nbOption] = malloc(1);
//         // strcpy(argLine[nbOption], "");
//         // nbOption ++;
//     //   }
//     //   memset(line, '\0', sizeof(line));
//     //   argLine[nbOption] = NULL;
//     return nbOption;
// }


