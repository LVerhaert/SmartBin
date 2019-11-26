#include<Servo.h>

Servo myservo;

void setup() {
 myservo.attach(9);
}

void loop() {
  for (int hoek=0; hoek<=180; hoek++){
  myservo.write(hoek);
    delay(10);
  }
}
