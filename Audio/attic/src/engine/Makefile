# Articulate Audio Engine

TARGET=articulate_engine

CC=clang
CFLAGS=-Wall -g
LDFLAGS=-lao -lmpg123
SRC=$(wildcard *.c)
HEADERS=$(wildcard *.h)
OBJECTS=$(patsubst %.c, %.o, $(SRC))

all: $(TARGET)

clean:	
	rm -f $(OBJECTS) *~

%.o : %.c
	$(CC) $(CFLAGS) -c -o $@ $^

$(TARGET): $(OBJECTS) $(HEADERS)
	$(CC) $(LDFLAGS) -o $(TARGET) $(OBJECTS) 
