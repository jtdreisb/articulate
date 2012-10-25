
#include <SoftwareSerial.h>
 
SoftwareSerial xbee(2,3); // RX TX 

void setup()  {
   Serial.begin(9600);
   
   Serial.println("Serial Connected");
   
   xbee.begin(9600);
   xbee.println("Hello, World!");
}
 
void loop()  {
  
  xbee.println("Hi");
  if (xbee.available())
    Serial.write(xbee.read());
    
  delay(1000);
}
