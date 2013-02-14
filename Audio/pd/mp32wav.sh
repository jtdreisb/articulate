#!/bin/sh
# usage: ./mp32wav.sh /path/to/song.mp3
# result: ./song.mp3.wav


CMD="afconvert -d LEI16 -f 'WAVE' \"$1\" -o \"`basename $1`.wav\""
echo $CMD
eval $CMD