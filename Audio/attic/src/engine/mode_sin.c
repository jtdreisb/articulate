//mode_sin.c
#include <ao/ao.h>
#include <math.h>
#include <stdio.h>
#include <string.h>

ao_device *sin_device;
ao_sample_format sin_format;

int sin_volume = 4000;
float sin_freq = 400.0;
float fade = 0.0;
int sin_index = 0;

char *sin_buffer;
size_t sin_buf_size;


void sin_init()
{
	memset(&sin_format, 0, sizeof(sin_format));
    sin_format.bits = 16;
    sin_format.channels = 2;
    sin_format.rate = 44100;

    sin_buf_size = (size_t)sin_format.bits/8 * sin_format.channels * sin_format.rate / 50;
    sin_buffer = malloc(sin_buf_size * sizeof(char));

    // -- Open driver -- //
    sin_device = ao_open_live(ao_default_driver_id(), &sin_format, NULL); // no options
    if (sin_device == NULL) {
        fprintf(stderr, "Error opening device.\n");
    }
}


void sin_play()
{
	int sample;
    for (int i = 0; i < (sin_buf_size / sin_format.channels / (sin_format.bits/8)); i++) {
        sample = (int)(sin_volume * sin(2 * M_PI * sin_freq * ((float) sin_index++/sin_format.rate)));

        // left //
        sin_buffer[4*i] = sample & 0xff;
        sin_buffer[4*i+1] = (sample >> 8) & 0xff;

        // right //
        sin_buffer[4*i+2] = sample & 0xff;
        sin_buffer[4*i+3] = (sample >> 8) & 0xff;
    }
    ao_play(sin_device, sin_buffer, sin_buf_size);
}


void sin_handle_input(unsigned char *buffer, size_t buf_size)
{
	for (int i = 0; i < buf_size; i++) {
		switch (buffer[i]) {
			case '+':
				sin_volume += 1000;
				printf("vol: %5d freq: %5.0f\n", sin_volume, sin_freq);
				break;
			case '-':
				sin_volume -= 1000;
				printf("vol: %5d freq: %5.0f\n", sin_volume, sin_freq);
				break;
			case 'p':
				sin_freq += 50;
				printf("vol: %5d freq: %5.0f\n", sin_volume, sin_freq);
				break;
			case 'o':
				sin_freq -= 50;
				printf("vol: %5d freq: %5.0f\n", sin_volume, sin_freq);
				break;
			default:
				break;
		}
		
	}
}