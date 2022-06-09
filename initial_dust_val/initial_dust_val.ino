//영점 구하기 
unsigned long prevMeasure;
unsigned long measureInterval = 20; 
int dustVocPin =39;
int iredPin = 32; 

float AvgDustResult = 0;

long sumVocValue = 0;
unsigned long avgVocValue = 0;
float smoothADC = 0; 

float voltageADC = 0.0;  


void setup() 
{
  Serial.begin(115200);
  
  pinMode(iredPin,OUTPUT);
  prevMeasure = millis();
  
}
 
void loop() 
{  
 
  if(millis() - prevMeasure > measureInterval) 
  {
    prevMeasure = millis();
    digitalWrite(iredPin, LOW);                         // Pulse ON
    delayMicroseconds(280);                     // 0.28 ms 
    
    voltageADC = analogRead(dustVocPin) * (5.0/1024.0);      // ADC to Voltage Convertion
    
    delayMicroseconds(40);                        // 0.04 ms
    digitalWrite(iredPin, HIGH);                        // Pulse OFF
    delayMicroseconds(9680);                        // 9.68 ms
 
    smoothADC = voltageADC * 0.005 + smoothADC * 0.995;  // Exponential Moving Average
    
    Serial.print("* V_instant = ");
    Serial.print(voltageADC, 4);
    Serial.print(",   * V_smooth = ");
    Serial.println(smoothADC, 4);
  }
  
  
}
