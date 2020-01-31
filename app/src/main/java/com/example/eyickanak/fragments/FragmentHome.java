package com.example.eyickanak.fragments;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : FragmentHome.java
        Global Variables:-
        Functions   : startCompost1(),startCompost2().endCompost1(),endCompost2();
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eyickanak.Home;
import com.example.eyickanak.R;
import com.example.eyickanak.SignIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentHome extends Fragment {

    private Button start1, end1, start2, end2;
    private ProgressBar progressBar;
    private ImageView imageBin1, imageBin2;

    DatabaseReference database;
    String status1, status2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, null);


        start1 = view.findViewById(R.id.start1);
        end1 = view.findViewById(R.id.end1);
        start2 = view.findViewById(R.id.start2);
        end2 = view.findViewById(R.id.end2);
        imageBin1 = view.findViewById(R.id.imageBin1);
        imageBin2 = view.findViewById(R.id.imageBin2);

        progressBar = view.findViewById(R.id.progressBar);

        //The database object is assigned the path of the status.
        database = FirebaseDatabase.getInstance().getReference("Status");

//        getCurrentStatus();

        database.child(FirebaseAuth.getInstance().getUid()).child("status1").addValueEventListener(new ValueEventListener() {
            @Override

            /*
            Here,the value of the status is checked by fetching it from the database.
            According to the value,the start and end composting buttons are enabled and disabled.
            Initially,end is disabled and start is enabled.
            As soon as the user presses the start composting button,it becomes disable and end button
            becomes enable and thus user can press it once he/she has done with composting.
            The images also change according to the current status.
             */
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    status1 = dataSnapshot.getValue().toString();
                    if (status1.equals("0") || status1.equals("NA") ) {
                        start1.setEnabled(true);
                        end1.setEnabled(false);
                        imageBin1.setBackgroundResource(R.drawable.off);
                    } else {
                        start1.setEnabled(false);
                        end1.setEnabled(true);
                        imageBin1.setBackgroundResource(R.drawable.on);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
        database.child(FirebaseAuth.getInstance().getUid()).child("status2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*SAME IS THE LOGIC.THIS IS FOR Bin 2
            Here,the value of the status is checked by fetching it from the database.
            According to the value,the start and end composting buttons are enabled and disabled.
            Initially,end is disabled and start is enabled.
            As soon as the user presses the start composting button,it becomes disable and end button
            becomes enable and thus user can press it once he/she has done with composting.
            The images also change according to the current status.
             */
                if (dataSnapshot != null) {
                    status2 = dataSnapshot.getValue().toString();
                    if (status2.equals("0") || status2.equals("NA")) {
                        start2.setEnabled(true);
                        end2.setEnabled(false);
                        imageBin2.setBackgroundResource(R.drawable.off);
                    } else {
                        start2.setEnabled(false);
                        end2.setEnabled(true);
                        imageBin2.setBackgroundResource(R.drawable.on);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });

        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCompost1();
            }
        });

        end1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCompost1();
            }
        });

        start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCompost2();
            }
        });

        end2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endCompost2();
            }
        });

        return view;
    }

    private void getCurrentStatus() {
        database.child(FirebaseAuth.getInstance().getUid()).child("status1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    status1 = dataSnapshot.getValue().toString();
                    if(status1.equals("0")){
                        start1.setEnabled(true);
                        end1.setEnabled(false);
                    } else {
                        start1.setEnabled(false);
                        end1.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        database.child(FirebaseAuth.getInstance().getUid()).child("status2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    status2 = dataSnapshot.getValue().toString();
                    if(status2.equals("0")){
                        start2.setEnabled(true);
                        end2.setEnabled(false);
                    } else {
                        start2.setEnabled(false);
                        end2.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    /*
     *
     * Function Name: 	startCompost1()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	In this method,when the user presses the Start Composting button for Bin1,
     *                  the child node 'status1' created during the user registration,is set to value
     *                  '1'.This value is stored in the database.As soon as the status becomes 1,this
     *                  condition is checked by the arduino code.If it is 1,then sensors start sending
     *                  the data to the FIREBASE.
     * Example Call :   startCompost1();
     *
     */
    private void startCompost1() {
        final String userid = FirebaseAuth.getInstance().getUid();
        database.child(userid).child("status1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    database.child(userid).child("status1").setValue("1");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*
     *
     * Function Name: 	endCompost1()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	In this method,when the user presses the End Composting button for Bin1,
     *                  the child node 'status1' created during the user registration,is set to value
     *                  '0'.This value is stored in the database.As soon as the status becomes 0,this
     *                  condition is checked by the arduino code.If it is 0,then sensors stop sending
     *                  the data to the FIREBASE.
     * Example Call :   endCompost1();
     *
     */
    private void endCompost1() {
        final String userid = FirebaseAuth.getInstance().getUid();
        database.child(userid).child("status1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    database.child(userid).child("status1").setValue("0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*
     *
     * Function Name: 	startCompost2()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	In this method,when the user presses the Start Composting button for Bin2,
     *                  the child node 'status2' created during the user registration,is set to value
     *                  '1'.This value is stored in the database.As soon as the status becomes 1,this
     *                  condition is checked by the arduino code.If it is 1,then sensors start sending
     *                  the data to the FIREBASE.
     * Example Call :   startCompost2();
     *
     */
    private void startCompost2() {
        final String userid = FirebaseAuth.getInstance().getUid();
        database.child(userid).child("status2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    database.child(userid).child("status2").setValue("1");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /*
     *
     * Function Name: 	endCompost2()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	In this method,when the user presses the End Composting button for Bin2,
     *                  the child node 'status2' created during the user registration,is set to value
     *                  '0'.This value is stored in the database.As soon as the status becomes 0,this
     *                  condition is checked by the arduino code.If it is 0,then sensors stop sending
     *                  the data to the FIREBASE.
     * Example Call :   endCompost2();
     *
     */

    private void endCompost2() {
        final String userid = FirebaseAuth.getInstance().getUid();
        database.child(userid).child("status2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    database.child(userid).child("status2").setValue("0");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
