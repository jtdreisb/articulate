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
GLOBAL_MODE global_mode = MP3_MODE;

int main(int argc, char **argv)
{
    fd_set read_fd_set;

    struct timeval tv;
    tv.tv_sec = 0;
    tv.tv_usec = 0;

    int input_fd = serial_init("/dev/tty.usbserial-A4013GAY", 9600);

    // int input_fd = STDIN_FILENO;

    // -- Initialize -- //
    ao_initialize();

    sin_init();
    mp3_init("hey.mp3", "ho.mp3");

    write(input_fd, "s", 1);

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
                mp3_play();
                break;
            case SIN_WAVE_MODE:
            default:
                sin_play();
                break;
        }
    }

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
            mp3_handle_input(buffer, nread);
            break;
        case SIN_WAVE_MODE:
        default:
            sin_handle_input(buffer, nread);
            break;
    }
}

// Convert mp3
    // int status =  mp3_to_wav("snapitup.mp3", "test.wav");
    // printf("%d\n", status );

