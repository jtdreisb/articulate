# Test sine wave

TARG=mod_mp3
CC=clang
CFLAGS=-Wall -g
LDFLAGS=-lao -lmpg123
SRC=$(wildcard *.c)
HFILES=$(wildcard *.h)
OBJS=$(patsubst %.c, %.o, $(SRC))

all: $(TARG)

clean:	
	rm -f $(OBJS) *~

%.o : %.c
	$(CC) $(CFLAGS) -c -o $@ $^

$(TARG): $(OBJS) $(HFILES)
	$(CC) $(LDFLAGS) -o $(TARG) $(OBJS) 
	