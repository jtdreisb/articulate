/*  Serial Test code 
    Sends "testing..." over the serial connection once per second 
    while blinking the LED on pin 13. Used for testing serial devices 
    such as the XBee. 
     
    Adapted from the SoftSerial Demonstration code 
*/ 
#include <SoftwareSerial.h>

#define ledPin 13 
byte pinState = 0; 
int counter = 0;
SoftwareSerial serial(6,7); //RX, TX

float x, y, z;

void setup()  { 
  pinMode(ledPin, OUTPUT); 
  serial.begin(9600);
} 
void loop() { 
  x = analogRead(2);
  y = analogRead(1);
  z = analogRead(0);
  
  serial.print("Counter=");
  serial.print(counter++);
  serial.print("|x=");
  serial.print(x);
  serial.print("|y=");
  serial.print(y);
  serial.print("|z=");
  serial.print(z);
  serial.println("|");
  // toggle an LED just so you see the thing's alive.  
  toggle(13); 
} 
void toggle(int pinNum) { 
  // set the LED pin using the pinState variable: 
  digitalWrite(pinNum, pinState); 
  // if pinState = 0, set it to 1, and vice versa: 
  pinState = !pinState; 
} 
