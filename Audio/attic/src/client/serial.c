//serial.c
#include <termios.h>
#include <stdio.h>
#include <fcntl.h>

int serial_init(const char* serialport, int baud)
{
    struct termios toptions;
    int fd;
	
    fd = open(serialport, O_RDWR | O_NOCTTY | O_NDELAY);
    if (fd == -1)  {
        perror("serial_init: Unable to open port ");
        return -1;
    }
    
    if (tcgetattr(fd, &toptions) < 0) {
        perror("serial_init: Couldn't get term attributes");
        return -1;
    }
    speed_t brate = baud; 
    switch(baud) {
		case 4800:   brate=B4800;   break;
		case 9600:   brate=B9600;   break;
		case 38400:  brate=B38400;  break;
		case 57600:  brate=B57600;  break;
		case 115200: brate=B115200; break;
    }
    cfsetispeed(&toptions, brate);
    cfsetospeed(&toptions, brate);
	
    toptions.c_cflag &= PARENB;
    toptions.c_cflag &= CSTOPB;
    toptions.c_cflag &= CSIZE;
    toptions.c_cflag |= CS8;
	
    toptions.c_cflag &= CRTSCTS;
	
    toptions.c_cflag |= CREAD | CLOCAL;  
    toptions.c_iflag &= (IXON | IXOFF | IXANY); 
	
    toptions.c_lflag &= (ICANON | ECHO | ECHOE | ISIG); 
    toptions.c_oflag &= OPOST; 
	
    // see: http://unixwiz.net/techtips/termios-vmin-vtime.html
    toptions.c_cc[VMIN]  = 0;
    toptions.c_cc[VTIME] = 20;
    
    if( tcsetattr(fd, TCSANOW, &toptions) < 0) {
        perror("serial_init: Couldn't set term attributes");
        return -1;
    }
	
    return fd;
}
