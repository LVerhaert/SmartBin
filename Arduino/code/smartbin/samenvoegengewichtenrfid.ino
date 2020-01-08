
#include <HX711.h>
#include <Wire.h>
#include <SPI.h>
#include <Adafruit_PN532.h>
#include <Adafruit_TCS34725.h>
#include <Servo.h>


//Gewichtsensoren
// gewichtsensor 1
#define DOUT1 3
#define CLK1  2
// gewichtsensor 2
#define DOUT2 5
#define CLK2  4
// gewichtsensor 3
#define DOUT3 7
#define CLK3  6
// gewichtsensor 4
#define DOUT4 9
#define CLK4  8
// alle gewichtsensoren
HX711 scale;
#define calibration_factor1 390.0
#define calibration_factor2 380.0
#define calibration_factor3 400.0
#define calibration_factor4 380.0


//RFID-sensor
#define PN532_SCK  10
#define PN532_MOSI 11
#define PN532_SS   12
#define PN532_MISO 13
// GND -> GND, VCC -> 5V
Adafruit_PN532 nfc(PN532_SCK, PN532_MISO, PN532_MOSI, PN532_SS);
#if defined(ARDUINO_ARCH_SAMD)
#define Serial SerialUSB
#endif


//Kleursensor
Adafruit_TCS34725 tcs = Adafruit_TCS34725(TCS34725_INTEGRATIONTIME_50MS, TCS34725_GAIN_4X);
// SDA -> A4
// SCL -> A5
// GND -> GND
// VIN -> 5V

//Servo's
#define SERVO1 A0
#define SERVO2 A1
#define SERVO3 A2
#define SERVO4 A3
Servo servo;


boolean scaleActive = false;
boolean rfidActive = false;
boolean kleurActive = false;


// de input vanuit Java
String inputString;

void setup() {
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) { // als er input komt vanuit Java
    inputString = Serial.readStringUntil("END"); // lees de hele input (tot en met "END")
    delay(500);
    if (inputString.startsWith("gewicht")) { // als het een commando voor een gewichtsensor is
      int scaleNum = inputString.charAt(7) - '0'; // kijk voor welke gewichtsensor het commando precies is
      scaleActive = activateScale(scaleNum); // activeer deze sensor
    } else if (inputString.startsWith("rfid")) {
      rfidActive = activateRFID();
    } else if (inputString.startsWith("open")) {
      int servoNum = inputString.charAt(4) - '0';
      openLid(servoNum);
    } else if (inputString.startsWith("dicht")) {
      int servoNum = inputString.charAt(5) - '0';
      closeLid(servoNum);
    } else if (inputString.startsWith("kleur")) {
      kleurActive = activateKleur();
    } else if (inputString.startsWith("stop")) {
      kleurActive = false;
      scaleActive = false;
      rfidActive = false;
    } else { // als het een onbekend commando is, laat dan zien wat er precies ontvangen is
      Serial.print("Error, I received this ->");
      Serial.print(inputString);
      Serial.print("<-\n");
    }
  }
  if (scaleActive) { // nadat de gewichtsensor geactiveerd is, toon elke 0.5 seconden de waarde die binnenkomt
    Serial.print("gReading: ");
    Serial.print(scale.get_units(), 1);
    Serial.print(" g\n");
    delay(500);
  }
  if (rfidActive) {
    uint8_t success;
    uint8_t uid[] = { 0, 0, 0, 0, 0, 0, 0 };  // Buffer to store the returned UID
    uint8_t uidLength;                        // Length of the UID (4 or 7 bytes depending on ISO14443A card type)

    success = nfc.readPassiveTargetID(PN532_MIFARE_ISO14443A, uid, &uidLength);

    if (success) {
      Serial.print("rUID Value: ");
      nfc.PrintHex(uid, uidLength);
      rfidActive = false;
    }
  }
  if (kleurActive) {
    float red, green, blue;

    delay(100);  // takes 50ms to read
    tcs.getRGB(&red, &green, &blue);

    Serial.print("kR:"); Serial.print(int(red));
    Serial.print(",G:"); Serial.print(int(green));
    Serial.print(",B:"); Serial.print(int(blue));
    Serial.print("\n");
  }
}

boolean activateScale(int scaleNum) {
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
      return false;
  }
  scale.tare(); // zet het startgewicht op 0.0 g
  return true;
}

boolean activateKleur() {
  if (!tcs.begin()) {
    Serial.println("No TCS34725 found ... check your connections");
    return false;
  }
  return true;
}

boolean activateRFID() {
#ifndef ESP8266
  while (!Serial); // for Leonardo/Micro/Zero
#endif
  Serial.begin(9600);
  Serial.print("Hello!\n");

  nfc.begin();

  uint32_t versiondata = nfc.getFirmwareVersion();
  if (! versiondata) {
    Serial.print("Didn't find PN53x board");
    return false;
  }

  // configure board to read RFID tags
  nfc.SAMConfig();

  Serial.print("Present card ...\n");

  return true;

}

void openLid(int servoNum) {
  Serial.print("Activating Servo " + String(servoNum) + "...\n"); // toon welke servo geactiveerd wordt
  switch (servoNum) { // setup voor de juiste servopin
    case 1:
      servo.attach(SERVO1);
      break;
    case 2:
      servo.attach(SERVO2);
      break;
    case 3:
      servo.attach(SERVO3);
      break;
    case 4:
      servo.attach(SERVO4);
      break;
    default:
      Serial.print("Servo " + String(servoNum) + " not found."); // als er een onbekend servonummer ingegeven is
  }
  int pos;
  for (pos = 0; pos <= 20; pos += 1) { // goes from 0 degrees to 20 degrees in steps of 1 degree
    servo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(30);                       // waits 30ms for the servo to reach the position
  }
  Serial.print("Servo activated.\n");
}

void closeLid(int servoNum) {
  Serial.print("Activating Servo " + String(servoNum) + "...\n"); // toon welke servo geactiveerd wordt
  switch (servoNum) { // setup voor de juiste servopin
    case 1:
      servo.attach(SERVO1);
      break;
    case 2:
      servo.attach(SERVO2);
      break;
    case 3:
      servo.attach(SERVO3);
      break;
    case 4:
      servo.attach(SERVO4);
      break;
    default:
      Serial.print("Servo " + String(servoNum) + " not found."); // als er een onbekend servonummer ingegeven is
  }
  int pos;
  for (pos = 20; pos >= 0; pos -= 1) { // goes from 20 degrees to 0 degrees in steps of 1 degree
    servo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(30);                       // waits 30ms for the servo to reach the position
  }
  Serial.print("Servo activated.\n");
}
