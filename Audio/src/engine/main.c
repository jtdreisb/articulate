// Main file for articulate audio engine
#include <ao/ao.h>
#include <math.h>
#include <string.h>
#include <sys/time.h>
#include <signal.h>
#include <unistd.h>
#include <sys/select.h>
#include "engine.h"


// -- Globals -- //
GLOBAL_MODE global_mode = SIN_WAVE_MODE;


int main(int argc, char **argv)
{
    fd_set read_fd_set;

    struct timeval tv;
    tv.tv_sec = 0;
    tv.tv_usec = 0;

    ao_device *device;
    ao_sample_format format;
    int default_driver;
    char *buffer;
    int buf_size;

    if (argc < 2) {
        // fprintf(stderr, "usage: %s <input> [output.wav]\n", argv[0]);
    }
    else if (argc == 3) {
        // status =  mp3_to_wav(argv[1], argv[2]);
        // Error check
        // argv[1] = argv[2];
    }

    // int input_fd = serial_init("/dev/stdin", 9600);
    int input_fd = STDIN_FILENO;

    // -- Initialize -- //
    ao_initialize();

    // -- Setup for default driver -- //
    default_driver = ao_default_driver_id();

    memset(&format, 0, sizeof(format));
    format.bits = 16;
    format.channels = 2;
    format.rate = 44100;

    buf_size = (size_t)format.bits/8 * format.channels * format.rate / 50;
    buffer = malloc(buf_size * sizeof(char));

    // -- Open driver -- //
    device = ao_open_live(default_driver, &format, NULL); // no options
    if (device == NULL) {
        fprintf(stderr, "Error opening device.\n");
        return 1;
    }

    for (;;) {
        
        FD_ZERO(&read_fd_set);
        FD_SET(input_fd, &read_fd_set);

        if (select(FD_SETSIZE, &read_fd_set, NULL, NULL, &tv) < 0) {
            perror("select");
            exit(1);
        }
        if (FD_ISSET(input_fd, &read_fd_set)) {
            handleInput(input_fd);
        }

        switch (global_mode) {
            case SQUARE_WAVE_MODE:
            case SAWTOOTH_WAV_MODE:
            case MP3_MODE:

                break;
            case SIN_WAVE_MODE:
            default:
                sin_generate(format, buffer, buf_size);
                break;
        }
        ao_play(device, buffer, buf_size);
    }
    // -- Close and shutdown -- 
    ao_close(device);

    ao_shutdown();

    return 0;
}

void handleInput(int fd)
{
    size_t buf_size = 1024;
    unsigned char *buffer = malloc(sizeof(char) * buf_size);
    
    ssize_t nread = read(fd, buffer, buf_size);
    if (nread < 0) {
        perror("handleInput: read");
        return;
    }


    switch (global_mode) {
        case SQUARE_WAVE_MODE:
        case SAWTOOTH_WAV_MODE:
        case MP3_MODE:
        case SIN_WAVE_MODE:
        default:
            sin_handle_input(buffer, nread);
            break;
    }
}

// Convert mp3
    // int status =  mp3_to_wav("snapitup.mp3", "test.wav");
    // printf("%d\n", status );

