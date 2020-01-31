package com.example.eyickanak;
/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : SignUp.java
        Global Variables:None
        Functions   : setOnClickListener(),registerUser(),createUSerWithEmailAndPassword(),addToDatabase
        */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

import com.example.eyickanak.model.User;
import com.example.eyickanak.util.Validator;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {


    //Creating the Firebase Authentication and Daatabase Reference objects
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    //As the names suggest,varaiables are created according to their functionality
    EditText edemail,edpassword, eduser, edphone;
    Button btnsignup,btnsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        edemail = findViewById(R.id.email);
        edpassword = findViewById(R.id.passwod);
        eduser = findViewById(R.id.user);
        edphone = findViewById(R.id.contact);
        btnsignin = findViewById(R.id.signin);
        btnsignup = findViewById(R.id.register);

        /*
         *
         * Function Name: 	setOnClickListener
         * Input        : 	-
         * Output       : 	-
         * Logic        : 	After clicking on this button,registerUser() method will be called
         * Example Call :	btnsignup.setOnClickListener();
         *
         */

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailID = edemail.getText().toString();
                String passwd = edpassword.getText().toString();

                if(Validator.validateEditText(eduser, "Please enter valid name") && Validator.validateEditText(edphone, "Please enter a valid phone") && Validator.validateEmail(edemail, "Please enter a valid email address") && Validator.validateEditText(edpassword, "Please enter a valid password"))
                    registerUser(emailID, passwd);

            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });
    }

    //registering the user with firebase
    /*
     *
     * Function Name: 	registerUser()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	The Firebase object 'mAuth' will call the createUserWithEmailAndPassword() method.
     *                  The database object will register the user if all the entered email and password is
     *                  genuine.Thus,the user will be registered.
     * Example Call :	registerUser()
     *
     */
    private void registerUser(String email, String password) {
        String emailText = email.trim();
        String passwordText = password.trim();

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user != null){
                                Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                addToDatabase(user);
                            }


                        } else {
                            // If sign in fails
                            Log.d("REGISTER", task.getException().getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), "Registration failed :" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("REGISTER", task.getException().getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), "Registration failed : " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /*
     *
     * Function Name: 	addToDatabase()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	When the user registers for the first time, separate child nodes will be created
     *                  under his  unique user id generated by firebase.The child nodes created will be
     *                  status1,status2 and points.The initial value assigned to both status1 and status2
     *                  will be 0 and points too will be assigned 0.
     *                  All this assigned values to the generated child nodes will be stored in the database
     *                  under that particular registered user.
     *
     * Example Call :	addToDatabase()
     *
     */


    private void addToDatabase(final FirebaseUser user) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                String status = "0";
                String userId = user.getUid();

                database = FirebaseDatabase.getInstance().getReference("Status");
                database.child(userId).child("status1").setValue(status);
                database.child(userId).child("status2").setValue(status);

                database = FirebaseDatabase.getInstance().getReference("Points");
                database.child(userId).child("score").setValue("0");

                User object = new User();
                object.setName(eduser.getText().toString().trim());
                object.setContact(edphone.getText().toString().trim());
                object.setEmail(edemail.getText().toString().trim());
                database = FirebaseDatabase.getInstance().getReference("User");
                database.child(userId).setValue(object);

            }
        }).start();
    }

}

