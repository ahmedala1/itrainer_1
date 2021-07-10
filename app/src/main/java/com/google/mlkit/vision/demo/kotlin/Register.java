package com.google.mlkit.vision.demo.kotlin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.mlkit.vision.demo.R;
import com.google.mlkit.vision.demo.option;

public class Register extends AppCompatActivity {
TextView textView;
Button buttonSignup;
    EditText emailId, password,usernam;
    EditText Editphone;
    DatabaseReference mDatabase;
    int flag2=0;
    int flag4=0;
    int flag1=0;   int flag3=0;
    int flag5=0;
    int flag6=0;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFirebaseAuth = FirebaseAuth.getInstance();

        textView=findViewById(R.id.have);
        buttonSignup=findViewById(R.id.button_SignUp);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, login.class);
                startActivity(intent);
                finish();

            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user");

        emailId = findViewById(R.id.email_signup);
        password = findViewById(R.id.password_signup);
        usernam=findViewById(R.id.Username_signup);


        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailId.getText().toString();
                final String pwd = password.getText().toString();
                final String name = usernam.getText().toString();



                if(name.isEmpty()  &&pwd.isEmpty() &&email.isEmpty())
                {
                    Toast.makeText(Register.this,"Fields are empty",Toast.LENGTH_SHORT).show();


                }
                if (email.isEmpty()){
                    Toast.makeText(Register.this,"Please enter email",Toast.LENGTH_SHORT).show();

                }
                else if(!email.isEmpty())
                {
                    flag3=1;
                }

                if (pwd.isEmpty()){
                    Toast.makeText(Register.this,"Please enter password",Toast.LENGTH_SHORT).show();

                }
                if (!pwd.isEmpty()){
                    flag2=1;
                }




                if (name.isEmpty() ){
                    Toast.makeText(Register.this,"Please enter name ",Toast.LENGTH_SHORT).show();
                }

                else {


                    if ( flag3==1 && flag2 == 1 ) {


                        //connect with firebase Auth
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this,
                                            "Login unsuccessful: " + task.getException().getMessage(), //ADD THIS
                                            Toast.LENGTH_SHORT).show();

                                } else {

                                    String user_id = mFirebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = mDatabase.child(user_id);
                                    current_user_db.child("email").setValue(email);
                                    current_user_db.child("password").setValue(pwd);

                                    current_user_db.child("username").setValue(name);



                                    Intent i = new Intent(Register.this, option.class);
                                    startActivity(i);
                                }

                            }

                        });

                    }
                }
            }
        });
            }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
    }
}