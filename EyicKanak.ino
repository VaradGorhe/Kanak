/*
 * Project Name : KANAK: Smart Composting
 * Author List : Riya Deshpande, Atharva Weginwar
 * Filename: EYICKANAK.ino
 * Functions: None
 * Globall Variables: days, soilarr[15], soil_comp_avg[50], soilavg, temp_days[15];
   temp_dayAvgs[50], pts, bad, delta[], Rise, Fall NoChange;
*/


#include <SoftwareSerial.h>




#include <Firebase.h>
#include <FirebaseArduino.h>
#include <FirebaseCloudMessaging.h>           //Various Libraries related to Firebase
#include <FirebaseError.h>
#include <FirebaseHttpClient.h>
#include <FirebaseObject.h>
#include <DallasTemperature.h>               //Various libraries related to DS18b20 temperature sensor used
#include <OneWire.h>                             



#include <ESP8266WiFi.h>

#define temp1 16                         //D0 pin of nodemcu


               

               
#define FIREBASE_HOST "kanak-96289.firebaseio.com"
#define FIREBASE_AUTH "bt4TBiNLEiwGjALtPCrca202sKpnljoKUhnvIxNn"      //Entering Firebase database related secrets and authorization    
#define WIFI_SSID "Enter your wifi user id"                                               
#define WIFI_PASSWORD "Enter password"                                      //Entering the wifi ssid and password


void check_soil_Moisture(void);
void check_temp_first(void);
void check_temp_second(void);
void give_points(void);

const int soilmoisture1 = A0;

int days=0;                          //Variable storing the number of days it will take between one cycle of composting
float soilarr[15];                   //Array storing soil moisture readings of one day. 12 readings in one day. Size kept 15 to avoid overflow.   
float soil_comp_avg[50];             //The average of values in above array will be taken and stored in this array. Thi will correspond to a particular day reading
float soilavg=0;
float temp_days[15];
int temp_dayAvgs[50];               //The same 2 arrays declared for temperature readings.
int pts=0;                          //Points given to a particular user.
int bad=0;                          //This variable gives a count of number of times violations occur in compost making process. Eg: No increase in temp for more than 4 days will increement bad
int delta[50];                      //array indicating difference in values of present day reading compared to previous day readings.
int Rise;                           //Count of times temp rose wrt previous day value
int Fall;                           //Count of times temp fell wrt previous day value
int NoChange;                       //Count of times temp stayed constant wrt previous day value



OneWire oneWire1(temp1);           


 
DallasTemperature sensor1(&oneWire1);          // Pass the oneWire reference to Dallas Temperature.


 


WiFiClient client;

/*
 * Function Name: void setup(void)
 * Input : None
 * Output: None
 * Logic: Sets up wifi connection, initialization of arrays.
*/

void setup(void)
{      
//Process to setup wifi connection

  
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

//array initialized to zero
   
 for(int i=0;i<50;i++)
{
  temp_dayAvgs[i]=0;
  soil_comp_avg[i]=0;
}
     
}  
/*
 * Function Name: void loop(void)
 * Input : None
 * Output: None
 * Logic:  All the main code and function calls stored here.
*/ 


void loop(void)
{
  //Checking value of status variable in firebase. 1=user pressed start composting. 0= User pressed stop composting. NA= no action
   
 if((Firebase.getString("Status/7HEypfe0s5QzOtW2xhkoD2IUeEy1/status1")=="1"))
 {
    Serial.print("START COMPOSTING");
      
    //Loop to take 12 values of temperature and soil moisture in a day. the delay is should be 2 hours 
      
    for(int i=0;i<12;i++)
     {
       sensor1.requestTemperatures(); // Send the command from sensor 1 to get temperatures 
       temp_days[i]=sensor1.getTempCByIndex(0);                                                  
       soilarr[i]=( 100.00 - ( (analogRead(soilmoisture1)/1023.00) * 100.00 ) );
         
       
       delay(1000); //this delay will be equal to 1 hr in actual scenerio.              
     }

     //Loop to calculate average of the above 12 values
       
     for(int i=0;i<12;i++)
      {
         temp_dayAvgs[days]=temp_dayAvgs[days]+temp_days[i]; 
         soil_comp_avg[days]=soil_comp_avg[days]+soilarr[i];       
      }
      
      
      //soil_comp_avg[] and temp_dayAvgs[] array stores the average of each day's values  in it.
      
      soil_comp_avg[days]=soil_comp_avg[days]/12;
      temp_dayAvgs[days]= temp_dayAvgs[days]/12;

      //The daily averaged values are sent to Firebase for storage
      
      Firebase.pushFloat("Status/7HEypfe0s5QzOtW2xhkoD2IUeEy1/Bin2/soil",soil_comp_avg[days]);
      Firebase.pushFloat("Status/7HEypfe0s5QzOtW2xhkoD2IUeEy1/Bin2/temp",temp_dayAvgs[days]); 

      //Starting with the test cases. If temperature exceeds 72 degree celsius it is undesirable and bad is increemented.
      
       if(temp_dayAvgs[days]>72)
        {
          bad++;
          //SEND ALERT TO ANDROID APP ABOUT TEMPERATURE INCREASING UNDESIRABLY
        }

       days++; //incremented value of days after every day readings
   }

  //Now status=0 which means user has pressed stop composting button and now the collected sensor data over one complete compost cycle will be analysed
   
 if((Firebase.getString("Status/7HEypfe0s5QzOtW2xhkoD2IUeEy1/status1")=="0"))
    {
      //If end day temperature is less than first day temperature then it is undesirable
     
     if(temp_dayAvgs[days] < temp_dayAvgs[0])
        {
           bad++;
        }
      Serial.print("STOP COMPOSTING");

     //Checking final soil moisture value
     check_soil_Moisture();
      

     //Delta array creation. Consecutive day temperature values are compared with each other 1-> Increase in temp. 0-> No change. -1-> decreement in temperature 
        
      for(int i=1;i<days;i++)
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

        //If ith day temperature had gone beyond 72 or final day temperature was 5 degrees lesser than first day temperature bad has been incremented.
        
        if(temp_dayAvgs[i]>72||temp_dayAvgs[i]<(temp_dayAvgs[0]-5))
        {
          bad++;
        }
        
      }
  
   check_temp_first();
   check_temp_second();

  //Points are now assigned based on value of bad variable and number of days. Higher the number of bad, implies more the violations in the process implying lesser points.
  //More number of days it takes lesser the point. optimum value is 2-3 weeks.
  give_points();
   
  days=0; 
  bad=0;
 // Array Again initialized to zero
 
 for(int i=0;i<50;i++)
 {
  temp_dayAvgs[i]=0;
  soil_comp_avg[i]=0;
  }
     
}
}
/*
 * Function Name: void check_soil_Moisture(void)
 * Input : None
 * Output: None
 * Logic:  Checking whether the soil moisture readings are within the prescribed limits.
*/ 

void check_soil_Moisture(void)
{
  if(soil_comp_avg[days]<=55&&soil_comp_avg[days]>=45)
      {
        Serial.print("Success");
      } 
      
      //Indicates compost too soggy or too dry thus bad value will be increemented
      
      else
      {
        bad=bad+3;
        Serial.print("Not upto the mark!");
      }
}

/*
 * Function Name: void check_temp_first()
 * Input : None
 * Output: None
 * Logic:  Checks if temperature has coninuouslt increased, decreased or stayed constant throughout.
*/ 

void check_temp_first()
{
  //First case is checked whether temperature either continuously increases/decreses/stays constant then that becomes wrong composting. That is checked and if true bad and days 
  //are incremented to make points=0
      int i;
      for( i=0;i<(days-1);i++)
      {
        if(delta[i+1]==delta[i])
        {
          continue;
         }
         
         else
         break;
      }
      if(i==(days-2))
      {
        bad=bad+5;
        days=days=40;
      }

}
/*
 * Function Name: void check_temp_second()
 * Input : None
 * Output: None
 * Logic:  Checks whether temperature through 1 composting cycle is violating the desired trend.
*/ 
void check_temp_second()
{
  /*Temperature variation should follow certain trend. If sudden violtaions in the trend are observed bad variable is increemented. Usually in composting temperature increases 
   for certain days, stays constant at a peak value for days and then decreases to attain values very close to ambient temperature. Thus following conditions are checked to avoid
   deception by user. 
   1 If number of days the temperature falls is greater than 3 (i.e. 3 consecutive -1 values in delta) then temperature should not rise for more than 2 consecutive days. (not more
   than 2 consecutive 1s)
   
   2. If temperature is constant for greater than 3 days(consecutive 3 0s) it should not rise for more than 3 days(Consecutive 3 1s)
   
   These conditions for days are applied considering the temperature trend and also certain relaxation is given as per fluctuating practical conditions due to environmental factors, 
   sensor sensitivity issues, etc*/
   
      for(int i=0;i<(days-1);i++)
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
          for(int days=i+1;days<(i+4);days++)
          {
            if(delta[days]==1)
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
          for(int days=i+1;days<(i+4);days++)
          {
            if(delta[days]==1)
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
}

/*
 * Function Name: void give_points()
 * Input : None
 * Output: None
 * Logic:  Assigns points to the user for one composting cycle.
*/ 
void give_points()
{
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
 
Firebase.setInt("Points/7HEypfe0s5QzOtW2xhkoD2IUeEy1/Bin2/score", pts);            //Points send to database

//Compost done properly should not take more than 40 days at worst and also should not be ready in less than 12 days.

if(days>=12&&days<=25)
{
  pts=pts+50;
}
else if(days>25&&days<=35)
{
  pts=pts+30;
}
else if(days>35&&days<=40)
{
  pts=pts+10;
}
else
{
  pts=pts-10;
  Serial.print("Too many days taken or Too few");
}
 
 //Finally the total value of bad and points send to Firebase database and satus set to NA

 
 Firebase.setString("Status/7HEypfe0s5QzOtW2xhkoD2IUeEy1/status1", "NA");
}
