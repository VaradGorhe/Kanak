package com.example.eyickanak;
/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : Home.java
        Global Variables:-
        Functions   : onNavigationItemSelected();
 */

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.eyickanak.fragments.FragmentHome;
import com.example.eyickanak.fragments.FragmentInfo;
import com.example.eyickanak.fragments.FragmentLeaderboard;
import com.example.eyickanak.fragments.FragmentPoints;
import com.example.eyickanak.fragments.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadFragment(new FragmentHome());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);


    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }



    /*
     *
     * Function Name: 	onNavigationItemSelected()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	When the user presses a particular button on the bottom navigation bar,it will take him to that particular fragment.
     *                  The fragments are home,points,leaderboard,information and profile.
     * Example Call :	new Handler().postDelayed();
     *
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new FragmentHome();
                break;

            case R.id.navigation_rewards:
                fragment = new FragmentPoints();
                break;

            case R.id.navigation_leaderboard:
                fragment = new FragmentLeaderboard();
                break;

            case R.id.navigation_info:
                fragment = new FragmentInfo();
                break;

            case R.id.navigation_profile:
                fragment = new FragmentProfile();
                break;
        }

        return loadFragment(fragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
