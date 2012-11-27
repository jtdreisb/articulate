// Modify the sound from a mp3 file

#include <ao/ao.h>
#include <mpg123.h>
#include <math.h>
#include <stdio.h>

#define BITS 8
float freq = 400.0;

void sinewave(unsigned char *buf, size_t len, double rate)
{
	double volumeLeft;//, volumeRight;
	// double radianScale =  M_PI_2 / (len / 4);
	
	for (int i = 0; i < (len/4); i++) {
		volumeLeft = (int)(0.75 * 32768.0 * sin(2 * M_PI * freq * ((float) i/rate)));
		// left sin()

		// printf("%f\n", volumeLeft);
		int leftValue = buf[4*i+0] + (buf[4*i+1] << 8);
		leftValue += volumeLeft;
		buf[4*i+0] = leftValue & 0xff;
		buf[4*i+1] = (leftValue >> 8) & 0xff;

		// right cos()
		// buf[4*i+2] = volumeLeft;
		// buf[4*i+3] *= volumeLeft;
	}
}

int main(int argc, char **argv)
{
	// Declarations
	mpg123_handle *mh;
	unsigned char *buffer;
	size_t buffer_size;
	size_t done;
	int err;

	int driver;
	ao_device *dev;
	ao_sample_format format;
	int channels, encoding;
	long rate;
        
	// initializations 
	ao_initialize();
	driver = ao_default_driver_id();
	mpg123_init();
	mh = mpg123_new(NULL, &err);
	buffer_size = mpg123_outblock(mh);
	buffer = (unsigned char*) malloc(buffer_size * sizeof(unsigned char));


	// open the file and get the decoding format 
	mpg123_open(mh, argv[1]);
	mpg123_getformat(mh, &rate, &channels, &encoding);

	// set the output format and open the output device 
	format.bits = mpg123_encsize(encoding) * BITS;
	format.rate = rate;
	format.channels = channels;
	format.byte_format = AO_FMT_NATIVE;
	format.matrix = 0;
	dev = ao_open_live(driver, &format, NULL);

	// decode and play
	while (mpg123_read(mh, buffer, buffer_size, &done) == MPG123_OK) {
	    	sinewave(buffer, buffer_size, format.rate);
	        ao_play(dev, (char *)buffer, done);
	}


	// clean up 
    free(buffer);
    ao_close(dev);
    mpg123_close(mh);
    mpg123_delete(mh);
    mpg123_exit();
    ao_shutdown();

    return 0;
}
