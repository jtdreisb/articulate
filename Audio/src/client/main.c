// Serial reader and decoder

#include <stdio.h>

int serial_init(const char* serialport, int baud);

int main(int argc, char **argv)
{

    int input = serial_init("/dev/tty.usbserial !!!", 9600);


    

    return 0;
}
