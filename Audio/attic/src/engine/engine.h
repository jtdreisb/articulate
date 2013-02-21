// Header for the engine utilites

typedef enum {
	SIN_WAVE_MODE,
	SQUARE_WAVE_MODE,
	SAWTOOTH_WAV_MODE,
	MP3_MODE
} GLOBAL_MODE;

int mp3_to_wav(char *mp3_file_path, char *wav_file_path);
int serial_init(const char* serialport, int baud);
void handleInput(int fd);


void mp3_init(char *mp3_file_0, char *mp3_file_1);
void mp3_play();
void mp3_handle_input(unsigned char *buffer, size_t buf_size);

void sin_init();
void sin_play();
void sin_handle_input(unsigned char *buffer, size_t buf_size);