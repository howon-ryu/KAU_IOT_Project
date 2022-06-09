//soil moisture and pumb test code and 3RGB led test code 

#define ARR_SIZE 200
#define Moisture_PIN 36
const int AirValue = 4095;  // row value (high)
const int WaterValue = 1540; // row value (low)
int intervals = (AirValue - WaterValue)/3;
int soilMoistureValue = 0;
int soilArr[ARR_SIZE];

int red_pin = 5; //5
int blue_pin = 18;
int green_pin = 19;//19

int pump_AA = 16;
int pump_AB = 17;


int readSoilMoister()
{

  int avg_soil=0;
  int processed_value = 0;
  int soilidx = 0;
  soilArr[ARR_SIZE]={0,};
  while(1)
  {
    soilMoistureValue = analogRead(Moisture_PIN); //put Sensor insert into soil
    Serial.print("raw soil ");
    Serial.println(soilMoistureValue);2
    
    soilArr[soilidx] = 100 - (scaling(soilMoistureValue, WaterValue, AirValue, 0, 100));
     
    delayMicroseconds(30);
    soilidx = (soilidx+1)%ARR_SIZE;
    delayMicroseconds(30);
    
    if(soilidx==199)
    {
      for(int i=0;i<ARR_SIZE;i++)
      {
        processed_value = processed_value + soilArr[i];
      }
    
      avg_soil = processed_value/ARR_SIZE;
      
      return (avg_soil); //SCALINE soil moisture value in range of 0% - 100%
    }
  
  }
}
// scaling function to convert from any range to any range
int scaling(int value, int rowMin, int rowMax, int newRange0, int newRange100 ) 
{
    int x = (((value - rowMin) * (newRange100 - newRange0)) / (rowMax - rowMin)) + newRange0;
    if (x < 0)
    { 
      return 0;
    }
    else if(x > 100)
    { 
      return 100; 
    }
    else
    { 
      return x; 
    }


}
void setup() 
{
  Serial.begin(115200);
  pinMode(pump_AA,OUTPUT);
  pinMode(pump_AB,OUTPUT);
  pinMode(red_pin,OUTPUT);
  pinMode(blue_pin,OUTPUT);
  pinMode(green_pin,OUTPUT);
  
  

}

void loop() 
{
  
  int soil_moisture = readSoilMoister();    //avg soil moisture 
  
  Serial.print("Soile moisture is ");
  Serial.println(soil_moisture);
  delay(1000);

  Serial.println("pump start! for 5 sec ");
  digitalWrite(pump_AA,HIGH);
  digitalWrite(pump_AB,LOW);
  delay(5000);

  digitalWrite(pump_AA,LOW);
  digitalWrite(pump_AB,LOW);    

  delay(2000);  //5초동안 물 충분히 들어가는 시간 줌 **시간 조정 해야할수도있다**


  
}
