# Test sine wave

TARG=mp3_2_wav
CC=clang
CFLAGS=-Wall -g
LDFLAGS=-lao -lmpg123
SRC=$(wildcard *.c)
HFILES=$(wildcard *.h)
OBJS=$(patsubst %.c, %.o, $(SRC))

all: $(TARG)

clean:	
	rm -f $(OBJS) *~

%.o : %.m
	$(CC) $(CFLAGS) -c -o $@ $^

$(TARG): $(OBJS) $(HFILES)
	$(CC) $(LDFLAGS) -o $(TARG) $(OBJS) 
	