CC=gcc
CFLAGS=-Wall -g

all : client

client.o : client.c

clean :
	rm -rf *.o client
