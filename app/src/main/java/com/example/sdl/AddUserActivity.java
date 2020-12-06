package com.example.sdl;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;


public class AddUserActivity extends AppCompatActivity {


    public static final String TAG = null;
    // Created the variables
    EditText mFullName, mEmail, mPassword, mPhone,mType;
    Button mRegisterBtn;
    FirebaseDatabase rootNode;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mFullName = findViewById(R.id.editTextName);
       mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mPhone = findViewById(R.id.editTextPhoneNumber);
        progressBar = findViewById(R.id.loading);
        mRegisterBtn = findViewById(R.id.loginBtn);
        mType = findViewById(R.id.editTextType);

        fAuth = FirebaseAuth.getInstance();
         rootNode = FirebaseDatabase.getInstance();


        // If the user is already logged in then it will go to main
      //  if (fAuth.getCurrentUser() != null){
        //    startActivity(new Intent(getApplicationContext(), MainActivity.class));
          //  finish();
       // }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Storing the mail and password in variable which is used later for creating account

                final String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone = mPhone.getText().toString().trim();
                final String email = mEmail.getText().toString();
                final String type =mType.getText().toString();



                // Checking the conditions
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Username is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if (TextUtils.isEmpty(fullName)){
                    mPassword.setError("Name  is Required.");
                    return;
                }

                if (TextUtils.isEmpty(phone)){
                    mPassword.setError("Phone Number is Required.");
                    return;
                }
                if (TextUtils.isEmpty(type)){
                    mPassword.setError("Type is Required.");
                    return;
                }


                progressBar.setVisibility(View.VISIBLE);


                // Register the user in the firebase
                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
                        if (task.isSuccessful()){

                            Toast.makeText(AddUserActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                            User user = new User(fullName,email,phone,password,type);


                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser()
                            .getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddUserActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), ManagerMainActivity.class));


                                    }else{
                                        Toast.makeText(getApplicationContext(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }
                            });
                        }
                        if(!task.isSuccessful()){
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(AddUserActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                }




            });
        }



        });

    }

}





