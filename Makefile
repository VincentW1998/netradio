CC=gcc
CFLAGS=-Wall -g

all : client

client : client.o manager.o mess.o list.o last.o

client.o : client.c h_files/manager.h h_files/mess.h

manager.o : manager.c h_files/manager.h

mess.o : mess.c h_files/mess.h h_files/manager.h

list.o : list.c h_files/list.h

last.o : last.c h_files/last.h

clean :
	rm -rf *.o client
