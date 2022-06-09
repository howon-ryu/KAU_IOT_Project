#include <SPI.h>
#include <Wire.h>
#define SEALEVELPRESSURE_HPA (1013.25)


#define DUST_VOC 0.8


unsigned long prevMeasure;
unsigned long measureInterval = 20; 
int dustVocPin =39;
int iredPin = 32; 

float AvgDustResult = 0;

long sumVocValue = 0;
long avgVocValue = 0;

// get Avg dust value function 

float getAvgDustVal()
{

  //initialize value 
  sumVocValue =0;
  avgVocValue=0;
  int count=0;
  float smoothDensity=0;
  prevMeasure=millis();
  
  while(1)
  {
    float voMeasured = 0;  
    float calcVoltage = 0;  
    float dustDensity = 0;  
    
    //iled 켜기 
    digitalWrite(iredPin,LOW);
      
    delayMicroseconds(280);     //sampling time 
    voMeasured = analogRead(dustVocPin);

    Serial.print("analog ");
    Serial.println(voMeasured);
    delayMicroseconds(40);
    digitalWrite(iredPin,HIGH);
    delayMicroseconds(9680);
    
    // 0 - 5V mapped to 0 - 1023 integer values  
    calcVoltage = voMeasured *(5.0 / 1024.0);  //아날로그 값 전압값으로 바꿈
    calcVoltage = (calcVoltage - DUST_VOC)/0.005;  //미세먼지 농도 값으로 바꿈
    
    delayMicroseconds(40);
    sumVocValue = sumVocValue+calcVoltage;
    //Serial.print("sumval is ");
    //Serial.println(sumVocValue);
    Serial.print("calcVoltage is");
    Serial.println(calcVoltage);

    count=count+1;
    

    if(millis()-prevMeasure >= 1100)
    {
      prevMeasure = millis();
      avgVocValue  = sumVocValue / count ;
      Serial.print("count is ");
      Serial.println(count);
      //Serial.print("avgVocValue is ");
      //Serial.println(avgVocValue);
      return (float)avgVocValue;
      
    }
    
    
    
    
  }
}

//*-------------------------------------------------------------------------*

void setup() 
{
   // put your setup code here, to run once:
  Serial.begin(115200);
  
  pinMode(iredPin,OUTPUT);
  //prevMeasure = millis();
  
}

void loop() 
{
  // put your main code here, to run repeatedly:
    
    float dusVot,AvgDustResult;
    int temp=0;
    AvgDustResult=0;
    dusVot = getAvgDustVal();
   
    
    Serial.println(dusVot);

   /* digitalWrite(iredPin,LOW);
      
    delayMicroseconds(280);     //sampling time 
  
    temp = analogRead(dustVocPin);
    delayMicroseconds(200);

  
    digitalWrite(iredPin,HIGH);

    Serial.println(temp);
    */
    delay(3000);
}
