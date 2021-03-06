/* Sweep
  by BARRAGAN <http://barraganstudio.com>
  This example code is in the public domain.

  modified 8 Nov 2013
  by Scott Fitzgerald
  http://www.arduino.cc/en/Tutorial/Sweep

  setup:
  oranje 9
  rood 5V
  bruin GND

*/


#include <Servo.h>
#define PIN (A0)


Servo myservo;  // create servo object to control a servo
// twelve servo objects can be created on most boards

int pos = 0;    // variable to store the servo position

void setup() {
  myservo.attach(PIN);  // attaches the servo on pin 9 to the servo object
}

void loop() {
  for (pos = 20; pos >= 0; pos -= 1) { // goes from 20 degrees to 0 degrees
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(30);                       // waits 30ms for the servo to reach the position
  }

  for (pos = 0; pos <= 20; pos += 1) { // goes from 0 degrees to 20 degrees
    // in steps of 1 degree
    myservo.write(pos);              // tell servo to go to position in variable 'pos'
    delay(30);                       // waits 30ms for the servo to reach the position
  }
  while (true) {}



}
