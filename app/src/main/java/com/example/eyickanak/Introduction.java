package com.example.eyickanak;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : Introduction.java
        Global Variables:None
        Functions   : addSlide(),onDonePressed(),onSkipPressed()
 */

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class Introduction extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_introduction);
        /*
         *
         * Function Name: 	addSlide()
         * Input        : 	-
         * Output       : 	-
         * Logic        : 	The app contains 6 introduction pages so that user can get used to the app.
         *                  the addSlide() function gets called 6 times and creates a new page.It takes title,description,picture and color of the page.
         *
         * Example Call :	addSlide()
         *
         */

        addSlide(AppIntroFragment.newInstance("Welcome User!","Easy Sign Up with just your email-id and password.",
                R.drawable.farmer, ContextCompat.getColor(getApplicationContext(),R.color.eco)));

        addSlide(AppIntroFragment.newInstance("Not sure about composting?","Don't worry,Kanak is there for you!Click on the Info button present at the bottom.",
                R.drawable.information, ContextCompat.getColor(getApplicationContext(),R.color.eco)));

        addSlide(AppIntroFragment.newInstance("Ready,Steady,Go!","Press the Start and End buttons present on the home page once you start and are done with composting. ",
                R.drawable.ready, ContextCompat.getColor(getApplicationContext(),R.color.eco)));

        addSlide(AppIntroFragment.newInstance("Hey wait!A surprise waiting for you!","You will be rewarded after successful completion of your compost.So why wait?Start Composting Now! ",
                R.drawable.rewards, ContextCompat.getColor(getApplicationContext(),R.color.eco)));

        addSlide(AppIntroFragment.newInstance("Competing was never soo exciting!","A leaderboard will be created between the users and the best ones will be rewarded! ",
                R.drawable.podium, ContextCompat.getColor(getApplicationContext(),R.color.eco)));

        addSlide(AppIntroFragment.newInstance("Kanak-Waste is Gold","Kanak,a one of it's kind app,will help you Go Green,reward you for your care towards environment.Happy Composting! ",
                R.drawable.compost, ContextCompat.getColor(getApplicationContext(),R.color.eco)));




    }


    /*
     *
     * Function Name: 	onDonePressed(),onSkipPressed()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	After pressing on either Skip or Done ,the user will be directed to the SignUp page.
     *
     *
     * Example Call :	onDonePressed();
     *                  onSkipPressed();
     *
     */
    @Override
    public void onDonePressed(Fragment currentFragment)
    {
        super.onDonePressed(currentFragment);
        Intent intent= new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment)
    {
        super.onSkipPressed(currentFragment);
        Intent intent= new Intent(getApplicationContext(),SignUp.class);
        startActivity(intent);

    }

}


