CC=gcc
CFLAGS=-Wall -g

all : client

client : client.o manager.o

client.o : client.c h_files/manager.h

manager.o : manager.c h_files/manager.h

clean :
	rm -rf *.o client
