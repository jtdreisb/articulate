// mp3_to_wav.c
// Contains all of the  


#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <mpg123.h>

#define INBUFF  16384 * 2 * 2
#define WAVE_FORMAT_PCM 0x0001
#define WAVE_FORMAT_IEEE_FLOAT 0x0003

struct mp3_conversion_ctx {
    mpg123_handle *mh;
    FILE *outputFile;
    size_t totaloffset;
    size_t dataoffset;
};

// write wav header
struct mp3_conversion_ctx *initConversionContext(FILE *out, mpg123_handle *m)
{
    struct mp3_conversion_ctx *context;
    unsigned int tmp32 = 0;
    unsigned short tmp16 = 0;
    unsigned short bitspersample;
    unsigned short wavformat;
    long rate;
    int channels;
    int enc;

    context = malloc(sizeof(struct mp3_conversion_ctx));
    if (context == NULL) {
        fprintf(stderr, "mallocing mp3 context: %s\n", strerror(errno));
        return NULL;
    }

    context->mh = m;
    context->outputFile = out;

    mpg123_getformat(m, &rate, &channels, &enc);

    if(enc & MPG123_ENC_FLOAT_64) {
        bitspersample = 64;
        wavformat = WAVE_FORMAT_IEEE_FLOAT;
    }
    else if(enc & MPG123_ENC_FLOAT_32) {
        bitspersample = 32;
        wavformat = WAVE_FORMAT_IEEE_FLOAT;
    }
    else if(enc & MPG123_ENC_16) {
        bitspersample = 16;
        wavformat = WAVE_FORMAT_PCM;
    }
    else {
        bitspersample = 8;
        wavformat = WAVE_FORMAT_PCM;
    }

    fwrite("RIFF", 1, 4, out);
    context->totaloffset = ftell(out);

    fwrite(&tmp32, 1, 4, out); // total size
    fwrite("WAVE", 1, 4, out);
    fwrite("fmt ", 1, 4, out);
    tmp32 = 16;
    fwrite(&tmp32, 1, 4, out); // format length
    tmp16 = wavformat;
    fwrite(&tmp16, 1, 2, out); // format
    tmp16 = channels;
    fwrite(&tmp16, 1, 2, out); // channels
    tmp32 = rate;
    fwrite(&tmp32, 1, 4, out); // sample rate
    tmp32 = rate * bitspersample/8 * channels;
    fwrite(&tmp32, 1, 4, out); // bytes / second
    tmp16 = bitspersample/8 * channels; // float 16 or signed int 16
    fwrite(&tmp16, 1, 2, out); // block align
    tmp16 = bitspersample;
    fwrite(&tmp16, 1, 2, out); // bits per sample
    fwrite("data ", 1, 4, out);
    tmp32 = 0;
    context->dataoffset = ftell(out);
    fwrite(&tmp32, 1, 4, out); // data length

    return context;
}

// rewrite wav header with final length infos
void completeConversion(struct mp3_conversion_ctx *context)
{
    unsigned int tmp32 = 0;

    long total = ftell(context->outputFile);
    fseek(context->outputFile, context->totaloffset, SEEK_SET);

    tmp32 = total - (context->totaloffset + 4);
    fwrite(&tmp32, 1, 4, context->outputFile);

    fseek(context->outputFile, context->dataoffset, SEEK_SET);
    tmp32 = total - (context->dataoffset + 4);

    fwrite(&tmp32, 1, 4, context->outputFile);
    
}


int mp3_to_wav(char *mp3_file_path, char *wav_file_path)
{
    mpg123_handle *m;
    int status;

    off_t num;
    unsigned char buf[INBUFF];
    unsigned char *audio;
    size_t bytes;
    off_t len;
    FILE *wav_file = fopen(wav_file_path, "w");
    if (wav_file == NULL) {
        fprintf(stderr, "Unable to open output file %s: %s\n", wav_file_path, strerror(errno));
        return -1;
    }

    FILE *mp3_file = fopen(mp3_file_path, "r");
    if(mp3_file == NULL) {
        fprintf(stderr,"Unable to open input file %s: %s\n", mp3_file_path, strerror(errno));
        return -2;
    }

    mpg123_init();

    m = mpg123_new(NULL, &status);
    if(m == NULL) {
        fprintf(stderr,"Unable to create mpg123 handle: %s\n", mpg123_plain_strerror(status));
        return -3;
    }

    mpg123_param(m, MPG123_VERBOSE, 2, 0);

    status = mpg123_param(m, MPG123_FLAGS, MPG123_FUZZY | MPG123_SEEKBUFFER | MPG123_GAPLESS, 0);
    if(status != MPG123_OK) {
        fprintf(stderr,"Unable to set library options: %s\n", mpg123_plain_strerror(status));
        return -4;
    }

    // Let the seek index auto-grow and contain an entry for every frame
    status = mpg123_param(m, MPG123_INDEX_SIZE, -1, 0);
    if(status != MPG123_OK) {
        fprintf(stderr,"Unable to set index size: %s\n", mpg123_plain_strerror(status));
        return -5;
    }

    status = mpg123_format_none(m);
    if(status != MPG123_OK) {
        fprintf(stderr,"Unable to disable all output formats: %s\n", mpg123_plain_strerror(status));
        return -6;
    }
    
    // Use float output
    status = mpg123_format(m, 44100, MPG123_MONO | MPG123_STEREO,  MPG123_ENC_FLOAT_32);
    if(status != MPG123_OK) {
        fprintf(stderr,"Unable to set float output formats: %s\n", mpg123_plain_strerror(status));
        return -7;
    }

    status = mpg123_open_feed(m);
    if(status != MPG123_OK) {
        fprintf(stderr,"Unable open feed: %s\n", mpg123_plain_strerror(status));
        return -8;
    }

    struct mp3_conversion_ctx *context;

    for (;;) {
        len = fread(buf, sizeof(unsigned char), INBUFF, mp3_file);
        if(len <= 0)
            break;
        status = mpg123_feed(m, buf, len);

        while(status != MPG123_ERR && status != MPG123_NEED_MORE) {
            status = mpg123_decode_frame(m, &num, &audio, &bytes);
            if(status == MPG123_NEW_FORMAT) {
                context = initConversionContext(wav_file, m);
            }
            fwrite(audio, sizeof(unsigned char), bytes, wav_file);
        }

        if(status == MPG123_ERR) {
            fprintf(stderr, "Error: %s", mpg123_strerror(m));
            break; 
        }
    }

    completeConversion(context);

    free(context);
    fclose(wav_file);
    mpg123_close(m);
    mpg123_delete(m);
    mpg123_exit();

    return 0;
}

