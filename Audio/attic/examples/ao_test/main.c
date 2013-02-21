// *
// * ao_example.c
// *
// *     Written by Stan Seibert - July 2001
// *
// * Legal Terms:
// *
// *     This source file is released into the public domain.  It is
// *     distributed without any warranty; without even the implied
// *     warranty * of merchantability or fitness for a particular
// *     purpose.
// *
// * Function:
// *
// *     This program opens the default driver and plays a 440 Hz tone for
// *     one second.
// *
// * Compilation command line (for Linux systems):
// *
// *     gcc -lao -ldl -lm -o ao_example ao_example.c
// *

#include <stdio.h>
#include <ao/ao.h>
#include <math.h>
#include <string.h>

#define BUF_SIZE 4096

int main(int argc, char **argv)
{
	ao_device *device;
	ao_sample_format format;
	int default_driver;
	char *buffer;
	int buf_size;
	float freq = 800.0;
	int i;

	 // -- Initialize -- 

	fprintf(stderr, "libao example program\n");

	ao_initialize();

	 // -- Setup for default driver -- 

	default_driver = ao_default_driver_id();

    memset(&format, 0, sizeof(format));
	format.bits = 8;
	format.channels = 2;
	format.rate = 44100;
	format.byte_format = AO_FMT_NATIVE;

	 // -- Open driver -- 
	device = ao_open_live(default_driver, &format, NULL); // no options
	if (device == NULL) {
		fprintf(stderr, "Error opening device.\n");
		return 1;
	}

	 
	buf_size = format.bits/8 * format.channels * format.rate;
	buffer = calloc(buf_size,
			sizeof(char));

	// -- Play some stuff -- 


	// 440 square wave
	freq = 440;
	for (i = 0; i < format.rate; i++) {
		if (((int)(freq * ((float)i/format.rate))) % 2) {
			buffer[2*i+0] = 127;
			buffer[2*i+1] = 127;
		}
		else {
			buffer[2*i+0] = 0;
			buffer[2*i+1] = 0;
		}

		// right
		// buffer[2*i+0] = (char)(0.25 * 255.0 * sin(2 * M_PI * freq * ((float) i/format.rate)));
		// freq = 800;
		// buffer[2*i+1] = (char)(0.65 * 127.0 * sin(2 * M_PI * freq * ((float) i/format.rate)));

	}
	ao_play(device, buffer, buf_size);

// 440 sawtooth wave
	freq = 440;
	int samplesInPeriod = format.rate/freq;
	float stepSize = 255.0/samplesInPeriod;
	float sample = 0;
	for (i = 0; i < format.rate; i++) {


		if (((int)(freq * ((float)i/format.rate))) % 2) {
			if (sample < (255 - stepSize*2)) {
				// rising
				sample += stepSize;
			}
		}
		else {
			if (sample > (stepSize*2)) {
				// falling
				sample -= stepSize;
			}
		}
		buffer[2*i+0] = buffer[2*i+1] = sample;

	}
	ao_play(device, buffer, buf_size);
	 // -- Close and shutdown -- 
	ao_close(device);

	ao_shutdown();

  return (0);
}