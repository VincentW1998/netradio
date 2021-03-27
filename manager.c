#include "h_files/manager.h"

// int typesMessage(char ** types) {
//     int nbType = 3;
//     char * allType[nbType] = {"LAST", "LAST", "MESS"};
//     int idType = -1;
    
//     for (int i = 0; i < nbType; i ++) {
//         if (!strcmp(allType[i], types[0]))
//             idType = i;
//     }
//     return idType;
// }

// Complete id by "#" if the lenght is inferior than 8
char * completeHashtag(char * id) {
    char * tmp = malloc(sizeof(char) * 8);
    int len = strlen(id);
    int completeLen = 8 - len;

    if (completeLen > 0) {
        strcat(tmp, id);
        for (int i = 0; i < completeLen; i++) {
            strcat(tmp, "#");
        }
        return tmp;
    }
    return id;
}

