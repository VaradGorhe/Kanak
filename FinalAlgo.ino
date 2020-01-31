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
#define WIFI_SSID "Athu"
#define WIFI_PASSWORD "12345678"

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
int pts=0;


OneWire oneWire1(temp1);
OneWire oneWire2(temp2);

 
DallasTemperature sensor1(&oneWire1);
DallasTemperature sensor2(&oneWire2);// Pass the oneWire reference to Dallas Temperature.

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
  temp_dayAvgs[i]=0;
}
}      

void loop(void)
{ 
 if((Firebase.getString("Status/clNFbLJsSRRModkXTowXZUmYFUX2/status1")=="1"))
  {
      Serial.print("START COMPOSTING");
      for(int i=0;i<12;i++)
      {
        //sensor1.requestTemperatures(); // Send the command from sensor 1 to get temperatures 
        temp_days[i]=i;//sensor1.getTempCByIndex(0);
        soilarr[i]=i;//( 100.00 - ( (analogRead(soilmoisture1)/1023.00) * 100.00 ) );
        delay(10); //this delay will be equal to 1 hr in actual code. EDIT              
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
          bad++;
          //SEND ALERT TO ANDROID APP ABOUT TEMPERATURE INCREASING UNDESIRABLY
        }
        
         if(j>35)
    {
      //Send to android app compost not accepted
      bad++;
    }
      j++;
   
 Firebase.setInt("Status/clNFbLJsSRRModkXTowXZUmYFUX2/BADSTART", bad);     
 }

  
   
   if((Firebase.getString("Status/clNFbLJsSRRModkXTowXZUmYFUX2/status1")=="0"))
    {
     if(temp_dayAvgs[j] < temp_dayAvgs[0])
        {
          //SEND SEPERATE ALERT ABOUT TEMP LOWERING UNDESIRABLY.
          bad++;
        }
      Serial.print("STOP COMPOSTING");    
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
   Firebase.setInt("Status/clNFbLJsSRRModkXTowXZUmYFUX2/BADMID", bad);  
   for(int i=0;i<j;i++)
   {
    if( 
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
       if(bad==0)
{
 pts=50;
}
else if(bad==1||bad==2)
{
  pts=30;
}
else if(bad==3||bad==4)
{
  pts=10;
}
else
{
  pts=0;
 } 
Firebase.setInt("Status/clNFbLJsSRRModkXTowXZUmYFUX2/PointsBAD", pts);
if(j<=25)
{
  pts=pts+50;
}
else if(j>25&&j<=35)
{
  pts=pts+30;
}
else if(j>35&&j<=40)
{
  pts=pts+10;
}
else
{
  pts=pts-10;
  Serial.print("TUMSE NA HO PAYEGA");
}
Firebase.setInt("Status/clNFbLJsSRRModkXTowXZUmYFUX2/BADEND", bad);
Firebase.setInt("Status/clNFbLJsSRRModkXTowXZUmYFUX2/PointsDAYS", pts);
Firebase.setString("Status/clNFbLJsSRRModkXTowXZUmYFUX2/status1", "NA");   
j=0; 
}
    }
