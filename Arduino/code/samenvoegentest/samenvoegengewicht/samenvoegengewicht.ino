
#include "HX711.h"

#define calibration_factor1 390.0
#define calibration_factor2 380.0
#define calibration_factor3 400.0
#define calibration_factor4 380.0

// gewichtsensor 1
#define DOUT1  3
#define CLK1  2
// gewichtsensor 2
#define DOUT2  5
#define CLK2  4
// gewichtsensor 3
#define DOUT3  7
#define CLK3  6
// gewichtsensor 4
#define DOUT4  9
#define CLK4  8
// alle gewichtsensoren
HX711 scale;

// de input vanuit Java 
String inputString;

void setup() {
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) { // als er input komt vanuit Java
    inputString = Serial.readStringUntil("END"); // lees de hele input (tot en met "END")
    delay(2000);
    if (inputString.startsWith("gewicht")) { // als het een commando voor een gewichtsensor is
      int scaleNum = inputString.charAt(7) - '0'; // kijk voor welke gewichtsensor het commando precies is
      activateScale(scaleNum); // activeer deze sensor
    } else { // als het een onbekend commando is, laat dan zien wat er precies ontvangen is
      Serial.print("Error, I received this ->");
      Serial.print(inputString);
      Serial.print("<-\n");
    }
    while (true) { // nadat de gewichtsensor geactiveerd is, toon elke 0.5 seconden de waarde die binnenkomt
      Serial.print("Reading: ");
      Serial.print(scale.get_units(), 1);
      Serial.print(" g\n");
      delay(500);
    }
  }
}

void activateScale(int scaleNum) {
  Serial.print("Activating Scale " + String(scaleNum) + "...\n"); // toon welke gewichtsensor geactiveerd wordt
  switch (scaleNum) { // setup voor de juiste sensorpinnen en de juiste calibratiefactor
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
      Serial.print("Scale " + String(scaleNum) + " not found."); // als er een onbekend gewichtsensornummer ingegeven is
  }
  scale.tare(); // zet het startgewicht op 0.0 g
}
