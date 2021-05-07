CC=gcc
CFLAGS=-Wall -g

all : client mytalk

client : client.o manager.o mess.o list.o last.o file.o udp.o tcp.o

mytalk : mytalk.o mycat.o

mytalk.o : mytalk.c mycat.h

mycat.o : mycat.c mycat.h

client.o : client.c h_files/manager.h h_files/mess.h

manager.o : manager.c h_files/manager.h

mess.o : mess.c h_files/mess.h h_files/manager.h

list.o : list.c h_files/list.h

last.o : last.c h_files/last.h

file.o : file.c h_files/file.h

udp.o : udp.c h_files/udp.h

tcp.o : tcp.c h_files/tcp.h

clean :
	rm -rf *.o client
