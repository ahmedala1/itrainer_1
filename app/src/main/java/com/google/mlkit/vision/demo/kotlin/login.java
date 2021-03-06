package com.google.mlkit.vision.demo.kotlin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.option;

public class login extends AppCompatActivity {
    TextView dont;
    Button butonSignIn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    EditText emailId, password;
    Button btn,btnsign;

    private static final String TAG = "Test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dont=findViewById(R.id.dont);
        butonSignIn=findViewById(R.id.button_SignIn);
        emailId = findViewById(R.id.Username);

        password = findViewById(R.id.password);
        mFirebaseAuth = FirebaseAuth.getInstance();
        dont.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent = new Intent(login.this, Register.class);
           startActivity(intent);
       }
   });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){

                    Toast.makeText(login.this,"Login Please",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(login.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        butonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty() && pwd.isEmpty()){
                    emailId.requestFocus();
                    password.requestFocus();

                    Toast.makeText(login.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }



                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }


                else  if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(login.this,"Invalid email or password",Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onComplete: Failed=" + task.getException().getMessage());

                            }

                            else{

                                Intent intToHome = new Intent(login.this, option.class);
                                startActivity(intToHome);
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(login.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }






            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}