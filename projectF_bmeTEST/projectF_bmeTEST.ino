

#include <Wire.h> //I2c
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#define SEALEVELPRESSURE_HPA (1013.25)
#define ARR_SIZE 200
#define UNTILL_MILLS 1000

Adafruit_BME280 bme; // I2C
unsigned long sumValue_temp = 0;
unsigned long sumValue_humid = 0;
unsigned long avgValue_temp = 0;
unsigned long avgValue_humid = 0;

unsigned long preMilisBme_T = 0;
unsigned long preMilisBme_H = 0;

int bmeTemp[ARR_SIZE];
int bmeHumid[ARR_SIZE];

float bme_tempVal=0;
float bme_humidVal=0;
int Bme_Power = 4;
int bmeID=0;
int bme_idx_h=0;
int bme_idx_t=0;

int flag1,flag2,flag3,flag4;
  


//BME 280 print value function
void printValues() 
{
  Serial.print("Temperature = ");
  Serial.print(bme.readTemperature());
  Serial.println(" *C");

  Serial.print("Humidity = ");
  Serial.print(bme.readHumidity());
  Serial.println(" %");
  Serial.println();
}
float get_bme_temp_Avg()
{

  sumValue_temp = 0;
  avgValue_temp =0;
  bme_idx_t = 0;
  bmeTemp[ARR_SIZE] = {0,};
  //smapling the temp data
  while(1)
  {
    bmeTemp[bme_idx_t] = bme.readTemperature();
    delayMicroseconds(50);
   
    bme_idx_t = (bme_idx_t+1)%ARR_SIZE;
    delayMicroseconds(30);
    
    if(bme_idx_t==199)
    {
      
      //get sum result 
      for(int i=0;i<ARR_SIZE;i++)
      {
        sumValue_temp = sumValue_temp + bmeTemp[i];
      }
      
      avgValue_temp = sumValue_temp/ARR_SIZE;
        
      return (float)avgValue_temp;
      
    }
  

  }
}


float get_bme_humid_Avg()
{
  sumValue_humid  = 0;
  avgValue_humid=0;
  bmeHumid[ARR_SIZE] = {0,};
  bme_idx_h=0;
  while(1)
  {
    //sampling the humid data
    
    bmeHumid[bme_idx_h] = bme.readHumidity();
    delayMicroseconds(50);
   
    bme_idx_h = (bme_idx_h+1)%ARR_SIZE;
    delayMicroseconds(30);
    
    if(bme_idx_h==199)
    {
        //get sum result 
      for(int i=0;i<ARR_SIZE;i++)
      {
        sumValue_humid = sumValue_humid + bmeHumid[i];  
      }
      avgValue_humid = sumValue_humid / ARR_SIZE;
      return (float)avgValue_humid;
  
    }
  }

}
void setup()
{
  bool status;
  Serial.begin(115200);
  Serial.println(F("BME280 test"));
  pinMode(Bme_Power,OUTPUT);    //use gpio as power source
  
  status = bme.begin(0x76);

  delay(2000);
  //who am i 가 아니라면
  while(!bme.begin(0x76)) 
  {
    
    Serial.println("Could not find a valid BME280 sensor, check wiring, address, sensor ID!");
    Serial.print("SensorID was: 0x"); 
    Serial.println(bme.sensorID(),16);
    Serial.println(status);
    Serial.print(" ID of 0xFF probably means a bad address, a BMP 180 or BMP 085\n");
    Serial.print(" ID of 0x56-0x58 represents a BMP 280,\n");
    Serial.print(" ID of 0x60 represents a BME 280.\n");
    Serial.print(" ID of 0x61 represents a BME 680.\n");

    Serial.println("BME280 will be reset by SW");
    digitalWrite(Bme_Power,LOW);
    delay(50);
    digitalWrite(Bme_Power,HIGH);
    delay(100);
    
  }
  bmeID = bme.sensorID();
  Serial.println("-- Default Test --");

  Serial.println();
  delay(1000);
}

void loop()
{
  
  if(bme.sensorID()!=bmeID)
  {
    Serial.println("BME280 will be reset by SW");
    digitalWrite(Bme_Power,LOW);
    delay(50);
    digitalWrite(Bme_Power,HIGH);
    delay(100);
  }
  bme_tempVal = get_bme_temp_Avg();
  delayMicroseconds(40);
  bme_humidVal = get_bme_humid_Avg();
  delayMicroseconds(40);

  Serial.print("temp ");
  Serial.println(bme_tempVal);
   
  Serial.print("humid ");
  Serial.println(bme_humidVal);
   
  printValues(); 
  delay(2000); 
/*  //initialize
  sumValue_humid  = 0;
  avgValue_humid=0;
  bmeHumid[ARR_SIZE] = {0,};
  bme_idx_h=0;
    
  sumValue_temp = 0;
  avgValue_temp =0;
  bme_idx_t = 0;
  bmeTemp[ARR_SIZE] = {0,};*/

}
