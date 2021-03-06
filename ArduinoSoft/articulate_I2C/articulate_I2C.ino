#include <SoftwareSerial.h>
#include <Wire.h>
#include <ADXL345.h>
#include <HMC5843.h>
#include <ITG3200.h>
#include <I2Cdev.h>
#include <HMC5843.h>


SoftwareSerial xbee(4,5); //Rx, Tx
ADXL345 adxl_L;
ADXL345 adxl_R;
ITG3200 gyro;
HMC5843 mag;

#define DEV_ACCEL_L "ACC L"
#define DEV_ACCEL_R "ACC R"

#define DEV_GYRO "GYRO"
#define DEV_MAGN "MAGN"

#define FINGER_R1_ON "BTN R 1"
#define FINGER_R1_OFF "BTN R 10"
#define FINGER_R2_ON "BTN R 2"
#define FINGER_R2_OFF "BTN R 20"

#define FINGER_L1_ON "BTN L 1"
#define FINGER_L1_OFF "BTN L 10"
#define FINGER_L2_ON "BTN L 2"
#define FINGER_L2_OFF "BTN L 20"

#define FLEX_L "FLEX L"
#define FLEX_R "FLEX R"

const int finger_R1 = 8; //R_index finger
const int finger_R2 = 9; //R_middle finger
const int finger_L1 = 6; //L_index finger
const int finger_L2 = 7; //L_middle finger

//HIGH = no contact, LOW = contact
int fingerR1_state;
int fingerR2_state;
int last_fingerR1_state = HIGH;
int last_fingerR2_state = HIGH;
boolean fingerR1_contact = false;
boolean fingerR2_contact = false;

int fingerL1_state;
int fingerL2_state;
int last_fingerL1_state = HIGH;
int last_fingerL2_state = HIGH;
boolean fingerL1_contact = false;
boolean fingerL2_contact = false;

//flexometer
int Lflex = 0;
int Rflex = 0;
int Lflex_pin = A0;
int Rflex_pin = A1;

//accelerometer readings
int aRx = 0;
int aRy = 0;
int aRz = 0;
int aLx = 0;
int aLy = 0;
int aLz = 0;

//Gyro readings
//int gx, gy, gz;

//Magnetometer readings
// int mx, my, mz; 

//debounce time in milliseconds
long debounceDelay = 50;
long lastDebounceTime_R1 = 0;
long lastDebounceTime_R2 = 0;
long lastDebounceTime_L1 = 0;
long lastDebounceTime_L2 = 0;


void send_3axis(char*, int, int, int);
void send_finger(char*);
void send_flex(char*, int);

void setup() {
  //set analog reference voltage to 3.3v 
  //analogReference(EXTERNAL);
    
  //Initialize I2C bus
  Wire.begin();
  
  //Start software serial (Xbee communication)
  xbee.begin(9600);
  pinMode(finger_R1, INPUT_PULLUP); 
  pinMode(finger_R2, INPUT_PULLUP);
  pinMode(finger_L1, INPUT_PULLUP); 
  pinMode(finger_L2, INPUT_PULLUP);  
  pinMode(Lflex_pin, INPUT);
  digitalWrite(Lflex_pin, HIGH);
  pinMode(Rflex_pin, INPUT);
  digitalWrite(Rflex_pin, HIGH);
  
  xbee.println("Initializing I2 devices..");
  //Initialize devices
  adxl_L.init(ADXL345_ADDR_ALT_HIGH);
  adxl_R.init(ADXL345_ADDR_ALT_LOW);
  adxl_L.powerOn();
  adxl_R.powerOn();

  //gyro.initialize();
  //mag.initialize();

  // verify connection
  xbee.println("Testing device connections...");
  
  //xbee.println(adxl_L.testConnection() ? "Left ADXL345 connection successful" : "Left ADXL345 connection failed");
  //xbee.println(adxl_R.testConnection() ? "Right ADXL345 connection successful" : "Right ADXL345 connection failed");
  //xbee.println(mag.testConnection() ? "HMC5843 connection successful" : "HMC5843 connection failed");
  //xbee.println(gyro.testConnection() ? "ITG3200 connection successful" : "ITG3200 connection failed");

}

void loop() {
    
  //Right accelerometer input
  if(adxl_L.status){
    adxl_L.readAccel(&aLx, &aLy, &aLz);
    delay(50);
    send_3axis(DEV_ACCEL_L, &aLx, &aLy, &aLz);
  }
  
  //Right accelerometer input
  if(adxl_R.status){
    adxl_R.readAccel(&aRx, &aRy, &aRz);
    send_3axis(DEV_ACCEL_R, &aRx, &aRy, &aRz);
    delay(50);
  }
  
  // read raw heading measurements from device NOT FILTERED OR SCALED
  //mag.getHeading(&mx, &my, &mz);
  
  // read raw gyro measurements from device NOT FILTERED OR SCALED
  //gyro.getRotation(&gx, &gy, &gz);
  
  
  //Flex sensors
  Lflex = analogRead(Lflex_pin);
  send_flex(FLEX_L, &Lflex);
  delay(10);

  Rflex = analogRead(Rflex_pin);
  send_flex(FLEX_R, &Rflex); 
  delay(10);
  
  fingerR1_state = digitalRead(finger_R1);
  fingerR2_state = digitalRead(finger_R2);
  fingerL1_state = digitalRead(finger_L1);
  fingerL2_state = digitalRead(finger_L2);
  
  if ( fingerR1_state == LOW && !fingerR1_contact){
    if ((millis() - lastDebounceTime_R1) > debounceDelay){
      send_finger(FINGER_R1_ON);
      fingerR1_contact = true; 
    }
  }else if((fingerR1_contact == true) && (fingerR1_state != LOW) ){
    lastDebounceTime_R1 = millis();
    fingerR1_contact = false;
   send_finger(FINGER_R1_OFF); 
  }else if(fingerR1_state != LOW){
    lastDebounceTime_R1 = millis();
    fingerR1_contact = false; 
  }
  
   if ( fingerR2_state == LOW && !fingerR2_contact){
    if ((millis() - lastDebounceTime_R2) > debounceDelay){
      send_finger(FINGER_R2_ON);
      fingerR2_contact = true; 
    }
  }else if((fingerR2_contact == true) && (fingerR2_state != LOW) ){
    lastDebounceTime_R2 = millis();
    fingerR2_contact = false;
   send_finger(FINGER_R2_OFF); 
  }else if(fingerR2_state != LOW){
    lastDebounceTime_R2 = millis();
    fingerR2_contact = false; 
  }
  
   if ( fingerL1_state == LOW && !fingerL1_contact){
    if ((millis() - lastDebounceTime_L1) > debounceDelay){
      send_finger(FINGER_L1_ON);
      fingerL1_contact = true; 
    }
  }else if((fingerL1_contact == true) && (fingerL1_state != LOW) ){
    lastDebounceTime_L1 = millis();
    fingerL1_contact = false;
   send_finger(FINGER_L1_OFF); 
  }else if(fingerL1_state != LOW){
    lastDebounceTime_L1 = millis();
    fingerL1_contact = false; 
  }
  
   if ( fingerL2_state == LOW && !fingerL2_contact){
    if ((millis() - lastDebounceTime_L2) > debounceDelay){
      send_finger(FINGER_L2_ON);
      fingerL2_contact = true; 
    }
  }else if((fingerL2_contact == true) && (fingerL2_state != LOW) ){
    lastDebounceTime_L2 = millis();
    fingerL2_contact = false;
   send_finger(FINGER_L2_OFF); 
  }else if(fingerL2_state != LOW){
    lastDebounceTime_L2 = millis();
    fingerL2_contact = false; 
  }

  last_fingerL1_state = fingerL1_state;
  last_fingerL2_state = fingerL2_state;
  last_fingerR1_state = fingerR1_state;
  last_fingerR2_state = fingerR2_state;
  
  //delay(5);
}

void send_3axis(char* dev, int *x, int *y, int *z){
  String data_str = dev;
  
  *x = map(*x, -255, 255, 0, 100);
  *y = map(*y, -255, 255, 0, 100);
  *z = map(*z, -255, 255, 0, 100);

  data_str = data_str + " " + *x + " " + *y + " " + *z + ";";
  xbee.println(data_str);
}

void send_finger(char* finger){
  String outFinger = finger;
  outFinger = outFinger + ";";
  xbee.println(finger);
}

//trasnmit flexometer value (0-255) via xbee 
//THIS NEEDS TO BE UPDATED TO (0-100) RANGE WITH NEW RESISTORS
void send_flex(char* dev, int *val){
  String flex = dev;
  
  *val = map(*val, 550 , 1024, 0, 100);

  flex = flex + " " + *val + ";";
  xbee.println(flex);
  
}
