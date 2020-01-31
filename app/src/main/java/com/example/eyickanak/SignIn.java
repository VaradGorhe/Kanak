package com.example.eyickanak;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : SignIn.java
        Global Variables:None
        Functions   : setOnClickListener(),signInUser(),signInWithEmailAndPassword()
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.ETC1;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eyickanak.util.Validator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    //variables for email, password ,buttons as the names suggest
    private EditText email, password;
    private Button signin;


    //Firebase object for user authentication
    FirebaseAuth mAuth;
    String emailText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        //Taking the id's of respective variables
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);


        /*
         *
         * Function Name: 	setOnClickListener
         * Input        : 	-
         * Output       : 	-
         * Logic        : 	After clicking on this button,signInUser() method will be called
         * Example Call :	sign.setOnClickListener();
         *
         */
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator.validateEmail(email, "Please enter a valid email address") && Validator.validateEditText(password, "Please enter a valid password"))
                    signInUser();
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }

    /*
     *
     * Function Name: 	signInUser()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	The firebase object 'mAuth' that is created will call the inbuilt signInWithEmailAndPassword() method.
     *                  It will check whether it is registered user or not.If it is registered,then user will be logged in and app
     *                  will take him to the home page otherwise it will show invalid credentials.
     * Example Call :	signInUser();
     *
     */
    private void signInUser() {
        emailText = email.getText().toString().trim();
        passwordText = password.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            startActivity(new Intent(SignIn.this,Home.class));
                        } else {
                            // If sign in fails
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
            startActivity(new Intent(SignIn.this,Home.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
