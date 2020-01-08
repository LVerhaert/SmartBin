#include "Adafruit_TCS34725.h"

Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_50MS, TCS34725_GAIN_4X);

void setup() {
  Serial.begin(9600);

  if (!tcs.begin()) {
    Serial.println("No TCS34725 found ... check your connections");
    while (1); // halt!
  }
}

void loop() {
  float red, green, blue;

  delay(100);  // takes 50ms to read
  tcs.getRGB(&red, &green, &blue);
  
  Serial.print("R:"); Serial.print(int(red)); 
  Serial.print(",G:"); Serial.print(int(green)); 
  Serial.print(",B:"); Serial.print(int(blue));
  Serial.print("\n");
}
