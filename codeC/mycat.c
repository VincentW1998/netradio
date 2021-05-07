#include "mycat.h"

char mycat_buf[BUFSIZE];

int mycat() {
  int lus, ecrits, total_ecrits;
  while ((lus = read(STDIN_FILENO, mycat_buf, BUFSIZE)) > 0) {
    total_ecrits = 0;
    while ((ecrits = write(STDOUT_FILENO, mycat_buf + total_ecrits, lus - total_ecrits)) > 0)
      total_ecrits += ecrits;
    if (ecrits  == -1) return -1;
  }
  return lus;
}
