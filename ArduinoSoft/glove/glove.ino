int value = 0;

void setup()                    // run once, when the sketch starts
{
  Serial.begin(9600);           // set up Serial library at 9600 bps
  Serial.println("Hello world!");  // prints hello with ending line break 
}

void loop() {
  value = analogRead(A0);
  Serial.println(value);
  delay(1000);
}
