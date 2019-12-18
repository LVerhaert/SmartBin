/*
  Example using the SparkFun HX711 breakout board with a scale
  By: Nathan Seidle
  SparkFun Electronics
  Date: November 19th, 2014
  License: This code is public domain but you buy me a beer if you use this and we meet someday (Beerware license).

  This example demonstrates basic scale output. See the calibration sketch to get the calibration_factor for your
  specific load cell setup.

  This example code uses bogde's excellent library: https://github.com/bogde/HX711
  bogde's library is released under a GNU GENERAL PUBLIC LICENSE

  The HX711 does one thing well: read load cells. The breakout board is compatible with any wheat-stone bridge
  based load cell which should allow a user to measure everything from a few grams to tens of tons.
  Arduino pin 2 -> HX711 CLK
  3 -> DAT
  5V -> VCC
  GND -> GND

  The HX711 board can be powered from 2.7V to 5V so the Arduino 5V power should be fine.

*/

#include "HX711.h"

#define calibration_factor1 390.0
#define calibration_factor2 380.0
#define calibration_factor3 400.0
#define calibration_factor4 380.0

#define DOUT1  3
#define CLK1  2
#define DOUT2  5
#define CLK2  4
#define DOUT3  7
#define CLK3  6
#define DOUT4  9
#define CLK4  8

//char inputBuffer[9];
String inputString;
HX711 scale;

void setup() {
  Serial.begin(9600);

}

void loop() {
  if (Serial.available() > 0) {
    inputString = Serial.readStringUntil("END");
    delay(2000);
    if (inputString.startsWith("gewicht")) {
      int scaleNum = inputString.charAt(7) - '0';
      activateScale(scaleNum);
    } else {
      Serial.print("Error, I received this ->");
      Serial.print(inputString);
      Serial.print("<-\n");
    }
    while (true) {
      Serial.print("Reading: ");
      Serial.print(scale.get_units(), 1); //scale.get_units() returns a float
      Serial.print(" g\n"); //You can change this to kg but you'll need to refactor the calibration_factor
      delay(100);
    }
  }
}

void activateScale(int scaleNum) {
  Serial.print("Activating Scale " + String(scaleNum) + "...\n");
  switch (scaleNum) {
    case 1:
      scale.begin(DOUT1, CLK1);
      scale.set_scale(calibration_factor1);
      break;
    case 2:
      scale.begin(DOUT2, CLK2);
      scale.set_scale(calibration_factor2);
      break;
    case 3:
      scale.begin(DOUT3, CLK3);
      scale.set_scale(calibration_factor3);
      break;
    case 4:
      scale.begin(DOUT4, CLK4);
      scale.set_scale(calibration_factor4);
      break;
    default:
      Serial.print("Scale " + String(scaleNum) + " not found.");
  }
  scale.tare(); //Assuming there is no weight on the scale at start up, reset the scale to 0
}

void activateScale2() {
  scale.begin(DOUT2, CLK2);
  scale.set_scale(calibration_factor2); //This value is obtained by using the SparkFun_HX711_Calibration sketch
  scale.tare(); //Assuming there is no weight on the scale at start up, reset the scale to 0
}
