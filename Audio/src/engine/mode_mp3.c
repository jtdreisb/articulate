// mode_mp3.c

#include <ao/ao.h>
#include <mpg123.h>
#include <string.h>
#include <stdio.h>

mpg123_handle *mh_0;
mpg123_handle *mh_1;

ao_device *mp3_device_0;
ao_device *mp3_device_1;
ao_sample_format mp3_format;

int mp3_index = 0x00;

char *mp3_buffer;
size_t mp3_buffer_size;

void mp3_init(char *mp3_file_0, char *mp3_file_1)
{
	int err;
	int channels, encoding;
    long rate;

	mpg123_init();
	mh_0 = mpg123_new(NULL, &err);
	mp3_buffer_size = mpg123_outblock(mh_0);
    mp3_buffer = malloc(mp3_buffer_size * sizeof(unsigned char));

	mpg123_open(mh_0, mp3_file_0);
    mpg123_getformat(mh_0, &rate, &channels, &encoding);

    printf("0: %s: rate: %ld channels: %d encoding: %d\n", mp3_file_0, rate, channels, encoding);

	memset(&mp3_format, 0, sizeof(mp3_format));
    mp3_format.bits = mpg123_encsize(encoding) * 8;
    mp3_format.channels = channels;
    mp3_format.rate = rate;

    // -- Open driver -- //
    mp3_device_0 = ao_open_live(ao_default_driver_id(), &mp3_format, NULL); // no options
    if (mp3_device_0 == NULL) {
        fprintf(stderr, "Error opening device 0 .\n");
    }

	mh_1 = mpg123_new(NULL, &err);
    mpg123_open(mh_1, mp3_file_1);
    mpg123_getformat(mh_1, &rate, &channels, &encoding);

    printf("1: %s: rate: %ld channels: %d encoding: %d\n", mp3_file_1, rate, channels, encoding);

	memset(&mp3_format, 0, sizeof(mp3_format));
    mp3_format.bits = mpg123_encsize(encoding) * 8;
    mp3_format.channels = channels;
    mp3_format.rate = rate;

    // -- Open driver -- //
    mp3_device_1 = ao_open_live(ao_default_driver_id(), &mp3_format, NULL); // no options
    if (mp3_device_1 == NULL) {
        fprintf(stderr, "Error opening device 1.\n");
    }
}

void mp3_play()
{
	ao_device *device;
	mpg123_handle *mh;
	size_t done;
	if (mp3_index == 0) {
		device = mp3_device_0;
		mh = mh_0;
	}
	else {
		device = mp3_device_1;
		mh = mh_1;
	}
	if (mpg123_read(mh, (unsigned char *) mp3_buffer, mp3_buffer_size, &done) == MPG123_OK) {
		ao_play(device, mp3_buffer, done);
	}
}

void mp3_handle_input(unsigned char *buffer, size_t buf_size)
{
	if (buf_size == 3) {
		for (int i = 0; i < buf_size; i++) {
			switch (buffer[i]) {
				case 0xFF:
					// button
					if (buffer[i + 1] == 2) {
						printf("button_press[%d]: %d\n", buffer[i+1], buffer[i+2]);
						if (buffer[i + 2] == 0) {
							mp3_index ^= 0x01;
							mpg123_seek	(mh_0, 0, SEEK_SET);
							mpg123_seek	(mh_1, 0, SEEK_SET);
						}
					}
					i += 2;
					break;
				case 0xFE:
					printf("accelerometer x: %d y: %d z: %d\n", buffer[i+1], buffer[i+2], buffer[i+3]);
					i += 3;
				default:
					break;
			}
		}
	}
}
