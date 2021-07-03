package com.shadowDeveloper.automail;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "LOGIN";
    private static final int RC_SIGN_IN=123;
    private SignInButton signInButton;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Scope scope = new Scope("https://mail.google.com/");
    private String mAccessToken,mRefreshToken;
    SpinKitView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.sign_in_button);

        //progress=(SpinKitView)findViewById(R.id.spin_kit_login);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null) {
            Log.d(TAG, "Current Logged in User: " + mAuth.getCurrentUser().getEmail());
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }

        createRequest();
    }




    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp =getSharedPreferences("datafile",MODE_PRIVATE);
        sp.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sp = getSharedPreferences("datafile",MODE_PRIVATE);
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }





    public void signInOnClick(View view) {
        EditText email = (EditText) findViewById(R.id.email);
        EditText pwd = (EditText) findViewById(R.id.password);

        if (validate_input(email.getText().toString(), pwd.getText().toString())) {

            mAuth.signInWithEmailAndPassword(email.getText().toString(), pwd.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    private boolean validate_input(String email, String password){
        boolean res = true;

        if(email.isEmpty()) {
            res=false;
            Toast.makeText(this,"Email is empty",Toast.LENGTH_LONG).show();
        }
        else if (password.isEmpty()){
            res=false;
            Toast.makeText(this,"Password is empty",Toast.LENGTH_LONG).show();
        }
        return res;
    }

    public void signUpOnClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(scope)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }


    /*
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e);
                        }

                    }
                }
            });

    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        someActivityResultLauncher.launch(signInIntent);
    }

     */

    @SuppressWarnings("deprecation")
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //progress.setVisibility(View.VISIBLE);
                            FirebaseUser user = mAuth.getCurrentUser();
                            authorize(account.getIdToken(),account.getServerAuthCode());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }


    private void authorize(String idToken, String serverAuthCode) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("grant_type", "authorization_code");
            jsonBody.put("client_id", getString(R.string.default_web_client_id));
            jsonBody.put("client_secret", getString(R.string.default_client_secret));
            jsonBody.put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
            jsonBody.put("code", serverAuthCode);
            jsonBody.put("id_token", idToken);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://oauth2.googleapis.com/token", jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    final String message = response.toString();
                    Log.d(TAG, message);
                    try {
                        mAccessToken = response.getString("access_token");
                        mRefreshToken= response.getString("refresh_token");
                        Log.d(TAG,mAccessToken);
                        Log.d(TAG,mRefreshToken);
                        Toast.makeText(LoginActivity.this,"Authorization Successful",Toast.LENGTH_SHORT).show();
                        SharedPreferences sp= getSharedPreferences("datafile",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("refresh_token",mRefreshToken).apply();
                        editor.putString("access_token",mAccessToken).apply();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, com.shadowDeveloper.automail.VolleyErrorHelper.getMessage(error,LoginActivity.this));
                    Toast.makeText(LoginActivity.this,
                            com.shadowDeveloper.automail.VolleyErrorHelper.getMessage(error,LoginActivity.this),
                            Toast.LENGTH_LONG).show();
                }
            });
            request.setRetryPolicy(new RetryPolicy() {
                                       @Override
                                       public int getCurrentTimeout() {
                                           return 50000;
                                       }

                                       @Override
                                       public int getCurrentRetryCount() {
                                           return 50000;
                                       }

                                       @Override
                                       public void retry(VolleyError error) throws VolleyError {

                                       }

                                   });


            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onBackPressed() {
        finishAffinity();
    }

    public void resetPassword(View v){
        EditText resetMail = new EditText(v.getContext());
        new AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setMessage("Enter your mail to receive Reset link")
                .setView(resetMail)
                .setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mail = resetMail.getText().toString();
                        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setTitle("Sending....");
                        progressDialog.show();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Reset Link sent to your email",Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this,"Error! Reset Link is not sent"+e.getMessage(),Toast.LENGTH_SHORT).show();


                            }
                        });


                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        if (s.equals("access_token")) {
            //progress.setVisibility(View.GONE);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }



}
