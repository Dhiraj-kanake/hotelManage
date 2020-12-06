package com.example.sdl;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sdl.menu.ChefMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // Creating the variable
    EditText mEmail, mPassword;
    Button mLoginBtn;
    TextView forgotTextLink;
    ProgressBar progressBar;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //firebase database ka object get kia
    DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

//    CheckBox waiter ,manager ,chef ;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);

        mLoginBtn = findViewById(R.id.loginBtn);

        forgotTextLink = findViewById(R.id.forgotPassword);

        progressBar = findViewById(R.id.loading);
/*
        manager = findViewById(R.id.cbManager);
        chef = findViewById(R.id.cbChef);
        waiter = findViewById(R.id.cbWaiter);*/


        fAuth = FirebaseAuth.getInstance();


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Storing the mail and password in variable which is used later for login the account
                final String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                // Checking the conditions
                if (TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }



                progressBar.setVisibility(View.VISIBLE);


                // Authenticate the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            /*Toast.makeText(LoginActivity.this, "Logged In.", Toast.LENGTH_SHORT).show();


                            if(manager.isChecked()){
                                startActivity(new Intent(getApplicationContext(), managerScreen1.class));
                            }

                            if(waiter.isChecked()){
                                startActivity(new Intent(getApplicationContext(), AddUserActivity.class));
                            }

                            if(chef.isChecked()){
                                startActivity(new Intent(getApplicationContext(), AddUserActivity.class));
                            }*/

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                User user;

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                        user = dsp.getValue(User.class);
                                        if (user != null) {
                                            if (user.getType().equals("Manager") && email.equals(user.getEmail())) {
                                                databaseReference.removeEventListener(this);
                                                Intent i = new Intent(LoginActivity.this,  ManagerMainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(i);
                                            }
                                        }
                                        if (user != null) {
                                            if (user.getType().equals("Chef") && email.equals(user.getEmail())) {
                                                databaseReference.removeEventListener(this);
                                                Intent i = new Intent(LoginActivity.this, ChefMainActivity.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(i);
                                            }
                                        }
                                        if(user!=null) {
                                            if (user.getType().equals("Waiter") && email.equals(user.getEmail())) {
                                                databaseReference.removeEventListener(this);
                                                Intent i = new Intent(LoginActivity.this, ActivityForTable.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(i);
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //SyncStateContract.Helpers.getAds(SignIn.this);
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();


                        }

                        else {
                            Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


            }
        });





        // Forgot password
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetEmail = new EditText(view.getContext());

                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter your email to receive password reset link");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Extract the mail and send reset link

                        // Storing the mail form textbox
                        String mail = resetEmail.getText().toString();

                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this,"Reset Link has been sent to your mail", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is not sent"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });


                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Close the dialog box
                    }
                });

                passwordResetDialog.create().show();



            }
        });

    }

    /*public void checkBox (View view){
        if(manager.isChecked()){
            Toast.makeText(getApplicationContext(),"'Manager' Selected",Toast.LENGTH_SHORT).show();
        }

        if(waiter.isChecked()){
            Toast.makeText(getApplicationContext(),"'Waiter' Selected",Toast.LENGTH_SHORT).show();
        }

        if(chef.isChecked()){
            Toast.makeText(getApplicationContext(),"'Chef' Selected",Toast.LENGTH_SHORT).show();
        }

    }*/
}