//mode_sin.c
#include <ao/ao.h>
#include <math.h>
#include <stdio.h>

int sin_volume = 4000;
float sin_freq = 400.0;
float fade = 0.0;
int sin_index = 0;


void sin_init()
{
	
}

void sin_generate(ao_sample_format format, char *buffer, size_t buf_size)
{
	int sample;
    for (int i = 0; i < (buf_size / format.channels / (format.bits/8)); i++) {
        sample = (int)(sin_volume * sin(2 * M_PI * sin_freq * ((float) sin_index++/format.rate)));

        // left //
        buffer[4*i] = sample & 0xff;
        buffer[4*i+1] = (sample >> 8) & 0xff;

        // right //
        buffer[4*i+2] = sample & 0xff;
        buffer[4*i+3] = (sample >> 8) & 0xff;
    }
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