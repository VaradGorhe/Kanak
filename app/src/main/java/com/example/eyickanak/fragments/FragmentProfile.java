package com.example.eyickanak.fragments;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : FragmentProfile.java
        Global Variables:-
        Functions   : onDataChange()
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eyickanak.R;
import com.example.eyickanak.SignIn;
import com.example.eyickanak.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfile extends Fragment {

    //Objects created as names given as per the functionalities
    private TextView name, email, phone;

    private DatabaseReference databaseReference;
    private String userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_profile, null);

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        phone = view.findViewById(R.id.phone);


        /*When the user presses the Logout button, inbuilt signOut() method will be called by the
          FrebaseAuth.After successful logout,it will take user to the signin page.
         */
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignIn.class));
            }
        });


        //Unique user id will be stored in the userID variable.
        userId = FirebaseAuth.getInstance().getUid();

        //Path of the details of the user is stored in databaseReference  and onDataChange() method is called.
        databaseReference = FirebaseDatabase.getInstance().getReference("User");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            /*
            In the onDataChange(),User object is created .
            dataSnapshot() will get the values from the User class.The User class is created in different file.
            After it gets the values,they will be assigned to respective variables(name,email,phone).
             */
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    User object = dataSnapshot.getValue(User.class);

                    name.setText(object.getName());
                    email.setText(object.getEmail());
                    phone.setText(object.getContact());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return view;
    }
}
