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
#define FIREBASE_HOST "kanak-96289.firebaseio.com"
#define FIREBASE_AUTH "bt4TBiNLEiwGjALtPCrca202sKpnljoKUhnvIxNn"
#define WIFI_SSID "1111"
#define WIFI_PASSWORD "Wagonr@1121            "

float temp_days[15];
float temp_dayAvgs[50];
int j=0;
int bad=0;
int delta[50];
int Rise;
int Fall;
int NoChange;
const int soilmoisture1 = A0;
float soilarr[15];
float soil_comp_avg[50];
float soilavg=0;


OneWire oneWire1(temp1);
OneWire oneWire2(temp2);

 
DallasTemperature sensor1(&oneWire1);
DallasTemperature sensor2(&oneWire2);// Pass the oneWire reference to Dallas Temperature.





//const char *ssid =  "1111";     // replace with your wifi ssid and wpa2 key
//const char *pass =  "Wagonr@1121";

WiFiClient client;


void setup(void)
{      

  Serial.begin(9600);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
   Serial.println("hey in setup");

for(int i=0;i<50;i++)
{
   Serial.println("hey in setup's loop");
  temp_dayAvgs[i]=0;
}
}      

void loop(void)
{ 

  
 // Firebase.pushFloat("Status/User",1); 
 //if((Firebase.getInt("Status/clNFbLJsSRRModkXTowXZUmYFUX2/status1")==1))
 // {
    Serial.println("hey in loop's loop");
      for(int i=0;i<12;i++)
      {
        //sensor1.requestTemperatures(); // Send the command from sensor 1 to get temperatures 
        temp_days[i]=i;//sensor1.getTempCByIndex(0);
        soilarr[i]=i;//( 100.00 - ( (analogRead(soilmoisture1)/1023.00) * 100.00 ) );
        delay(1000); //this delay will be equal to 1 hr in actual code. EDIT              
      }

       for(int i=0;i<12;i++)
      {
          temp_dayAvgs[j]=temp_dayAvgs[j]+temp_days[i]; 
          soilavg=soilavg+soilarr[i];       
      }
      soilavg=soilavg/12;
      soil_comp_avg[j]=soilavg;
      temp_dayAvgs[j]= temp_dayAvgs[j]/12;
      Firebase.pushFloat("Status/clNFbLJsSRRModkXTowXZUmYFUX2/soil",soil_comp_avg[j]);
      Firebase.pushFloat("Status/clNFbLJsSRRModkXTowXZUmYFUX2/temp",temp_dayAvgs[j]); 
       if(temp_dayAvgs[j]>70)
        {
          //SEND ALERT TO ANDROID APP ABOUT TEMPERATURE INCREASING UNDESIRABLY
        }
        if(temp_dayAvgs[j] < temp_dayAvgs[0])
        {
          //SEND SEPERATE ALERT ABOUT TEMP LOWERING UNDESIRABLY.
          bad++;
        }
         if(j>35)
    {
      //Send to android app compost not accepted
      bad++;
    }
      j++;
      Firebase.pushFloat("Status/clNFbLJsSRRModkXTowXZUmYFUX2/Bad",bad); //SHIFT THIS AFTER STATUS==0 loop
      Firebase.pushFloat("Status/clNFbLJsSRRModkXTowXZUmYFUX2/Days",j); 
//  }

  /*
   
   if(status==0)
  //{
     
           
      if(soilavg<=40&&soilavg>=30)
      {
        Serial.print("Success");
        //POints assignment
        
        
      }  
      else
      {
        Serial.print("Rejected");
      }
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
      
  //}*/
}
 
