#include <SoftwareSerial.h>


#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>


#include <ESP8266WiFi.h>
#include <DallasTemperature.h>
#include <OneWire.h>

#define temp1 16                         //D0 pin of nodemcu
#define temp2 4                          //D2 pin of nodemcu
#define FIREBASEHOST "eyic-trial1.firebaseio.com/"
#define FIREBASEAUTH "sxqrPYcGUuLBSOqLP6oqEgtXHgAYliuyu4fmXUc8"

float temp_days[15];
float temp_dayAvgs[50];
int j=0
int bad=0;
int delta[50];


OneWire oneWire1(temp1);
OneWire oneWire2(temp2);

 
DallasTemperature sensor1(&oneWire1);
DallasTemperature sensor2(&oneWire2);// Pass the oneWire reference to Dallas Temperature.

const int soilmoisture1 = D5;

const int soilmoisture2 = D7;
 


const char *ssid =  "1111";     // replace with your wifi ssid and wpa2 key
const char *pass =  "Wagonr@1121";

WiFiClient client;


void setup(void)
{
      
       int x=0;        
       Serial.println("Connecting to ");
       Serial.println(ssid); 
       WiFi.begin(ssid, pass);  
       while (WiFi.status() != WL_CONNECTED) 
          {
            x=1;
            delay(500);
            Serial.print(".");
          }
      Serial.println("");
      Serial.println("WiFi connected");

for(int i=0;i<50;i++)
{
  temp_dayAvgs[i]=0;
}
}      

void loop(void)
{ 
  //if(status==1)
  //{
      for(int i=0;i<12;i++)
      {
        sensor1.requestTemperatures(); // Send the command from sensor 1 to get temperatures 
        temp_days[i]=sensor1.getTempCByIndex(0);
        delay(1000); //this delay will be equal to 1 hr in actual code. EDIT              
      }

       for(int i=0;i<12;i++)
      {
          temp_dayAvgs[j]=temp_dayAvgs[j]+temp_days[i];        
      }
      temp_dayAvgs[j]= temp_dayAvgs[j]/12;
      Firebase.pushFloat("ENTER PATH",temp_dayAvgs[j]); 
       if(temp_dayAvgs[j]>70)
        {
          //SEND ALERT TO ANDROID APP ABOUT TEMPERATURE INCREASING UNDESIRABLY
        }
        if(temp_dayAvgs<temp_dayAvgs[0]}
        {
          //SEND SEPERATE ALERT ABOUT TEMP LOWERING UNDESIRABLY.
        }
      j++;
  //}

  //if(status==0)
  //{
     
           

      for(int i=1;i<j;i++)
      {
        if(temp_dayAvgs[i]==temp_dayAvgs[i-1])
        {
          delta[i-1]=0;
        }
        else if(temp_dayAvgs[i]>temp_dayAvgs[i-1])
        {
          delta[i-1]=1;
        }
        else
        {
          delta[i-1]=-1;
        }

        if(temp_dayAvgs[i]>70||temp_dayAvgs[i]<(temp_dayAvgs[0]-5))
        {
          bad++;
        }
        
      }
      

      for(int i=0;i<(j-1);i++)
      {
        if(delta[i]==-1)
        {
          Fall++;
        }
        else if(delta[i]==0)
        {
          NoChange++;
        }
        if(Fall>3)
        {
          for(int j=i+1;j<(i+4);j++)
          {
            if(delta[j]==1)
            {
              Rise++;
              
            }
            if(Rise>3)
            {
              bad++;
            }
          }
        }
        if(NoChange>3)
        {
          Rise=0;
          for(int j=i+1;j<(i+4);j++)
          {
            if(delta[j]==1)
            {
              
              Rise++;
              
            }
            if(Rise>3)
            {
              bad++;
            }
          }
        }
      } 
      
  //}
}
  





 
