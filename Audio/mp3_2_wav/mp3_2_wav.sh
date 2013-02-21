#!/bin/ksh
# usage: ./mp32wav.sh /path/to/song.mp3
# result: ./song.mp3.wav

if [ ! -d "./samples" ]; then
	mkdir "samples"
fi

CMD="afconvert -d LEI16 -f 'WAVE' \"$1\" -o \"samples/`basename $1`.wav\""
echo $CMD
eval $CMD