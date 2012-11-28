// Main file for articulate audio engine
#include <stdio.h>
#include "engine.h"


int main(int argc, char **argv)
{
    int status =  mp3_to_wav("snapitup.mp3", "test.wav");
    printf("%d\n", status);
    return 0;
}
