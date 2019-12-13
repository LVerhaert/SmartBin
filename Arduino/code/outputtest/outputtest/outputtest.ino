char inputBuffer[10];   

void setup() {                
  Serial.begin(9600);  
}

void loop() {
    while (true) 
    {
      if (Serial.available() > 0) {
          Serial.readBytes(inputBuffer, Serial.available());
          delay(5000);
          Serial.print("I got this ->");
          Serial.print(inputBuffer);
          Serial.println("<-");
      }
    }
}
