package com.shadowDeveloper.automail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.shadowDeveloper.automail.models.Creator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameText, passwordText, emailText, confirmPasswordText;
    private FirebaseAuth mAuth;
    private String TAG = "SignUpPage";
    //private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        emailText = (EditText) findViewById(R.id.email);
        confirmPasswordText = (EditText) findViewById(R.id.confirm_password);
        //db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public void registerOnClick(View view){
        String username, password, confirmPassword, email;
        username = usernameText.getText().toString();
        password = passwordText.getText().toString();
        confirmPassword = confirmPasswordText.getText().toString();
        email = emailText.getText().toString();

        if (validate_input(username, password, confirmPassword, email)){
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Creating user...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                // Save to db and bitmap uir to storage
                                Creator creator = new Creator(
                                        username,
                                        email,
                                        mAuth.getCurrentUser().getUid()
                                );

                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, com.shadowDeveloper.automail.LoginActivity.class);
                                startActivity(intent);


                                // Direct to login page
                                //saveCreatorToFirebase(creator);
                                Log.d(TAG, "createUserWithEmail:success");

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    public void returnToLogin(View v){
        Intent intent=new Intent(SignUpActivity.this, com.shadowDeveloper.automail.LoginActivity.class);
        startActivity(intent);
    }




    private boolean validate_input(String username, String password, String confirmPassword, String email){
        boolean res = true;
        if (username.isEmpty()){
            Toast.makeText(getApplicationContext(), "Username cannot be blank!",
                    Toast.LENGTH_SHORT).show();
            res = false;
        } else if (!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(), "Two password do not match!.",
                    Toast.LENGTH_SHORT).show();
            res = false;
        } else if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(getApplicationContext(), "Email is invalid!",
                    Toast.LENGTH_SHORT).show();
            res = false;
        }
        return  res;
    }
}
