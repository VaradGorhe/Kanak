package com.example.eyickanak;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : Gif.java
        Global Variables:None
        Functions   : postDelayed()
 */

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.Toast;

public class Gif extends AppCompatActivity {

    /*
     *
     * Function Name: 	postDelayed()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	A delay given of 3 seconds .This is the starting page of the app.The next activity will open after the specified delay.
     * Example Call :	new Handler().postDelayed();
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openMainActivity =  new Intent(Gif.this, SplashActivity.class);
                startActivity(openMainActivity);

                finish();

            }
        }, 3000);
    }
}