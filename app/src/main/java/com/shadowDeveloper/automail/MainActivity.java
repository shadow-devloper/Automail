package com.shadowDeveloper.automail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shadowDeveloper.automail.Utility.Mails;
import com.shadowDeveloper.automail.Utility.Message;
import com.shadowDeveloper.automail.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener
{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private NavigationView navigationView;
    private static final String TAG ="MAIN ACTIVITY";
    boolean doubleBackToExitPressedOnce=false;
    private NavController navController;
    String token_url = "https://oauth2.googleapis.com/token";
    String message_url,batch_url,host_url;
    RequestQueue requestQueue;
    String mAccessToken,mRefreshToken;
    List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Log.i(TAG, "onCreate : " + binding.getRoot());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        /*
        message_url = "https://www.googleapis.com/gmail/v1/users/me/messages";
        batch_url = "/batch/gmail/v1";
        host_url = "https://www.googleapis.com";

        SharedPreferences sp=getSharedPreferences("datafile",MODE_PRIVATE);
        if(sp.contains("access_token")){
            mAccessToken=sp.getString("access_token","");
            Log.d(TAG,mAccessToken);

        }

        if(sp.contains("refresh_token")) {
            mRefreshToken=sp.getString("refresh_token","");
            Log.d(TAG,mRefreshToken);
        }

         */


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_compose, R.id.nav_history)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavHeader();

        //requestQueue = Volley.newRequestQueue(this);
        //fetchGmail(mAccessToken);
    }

    /*

    private void fetchGmail(String access_token) {
        JsonObjectRequest gmailRequest = new JsonObjectRequest(Request.Method.GET
                , message_url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson =gsonBuilder.create();
                Mails mails =gson.fromJson(response.toString(),Mails.class);
                messages= mails.getMessages();
                Log.d(TAG,String.valueOf(messages.size()));
                for(int i=0;i<messages.size();i++){
                    fetchMessages(messages.get(i).getId());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode==401) {
                    refreshAccessToken();
                }else {
                    Toast.makeText(MainActivity.this,"Some Error occurred\nPlease retry after some time",Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+access_token);
                headers.put("Content-type","application/json");
                return headers;
            }
        };
        requestQueue.add(gmailRequest);
    }


    private void fetchMessages(String id ) {
        JsonObjectRequest messageRequest = new JsonObjectRequest(Request.Method.GET,
                message_url + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, com.shadowDeveloper.automail.VolleyErrorHelper.getMessage(error,MainActivity.this));
                Toast.makeText(MainActivity.this,
                        com.shadowDeveloper.automail.VolleyErrorHelper.getMessage(error,MainActivity.this),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String,String> getHeaders() {
                Map<String,String> headers = new HashMap<>();
                headers.put("Authorization","Bearer "+mAccessToken);
                headers.put("Content-type","application/json");
                return headers;
            }
        };
        requestQueue.add(messageRequest);
    }


    private void refreshAccessToken() {
        SharedPreferences sp=getSharedPreferences("datafile",MODE_PRIVATE);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("client_id", getString(R.string.default_web_client_id));
            jsonBody.put("client_secret",getString(R.string.default_client_secret));
            jsonBody.put("refresh_token", sp.getString("refresh_token",""));
            jsonBody.put("grant_type", "refresh_token");

            JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST,
                    token_url,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            final String message = response.toString();
                            Log.d(TAG, message);
                            try {
                                mAccessToken = response.getString("access_token");
                                SharedPreferences sp= getSharedPreferences("datafile",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("access_token",mAccessToken).apply();
                                fetchGmail(mAccessToken);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, com.shadowDeveloper.automail.VolleyErrorHelper.getMessage(error,MainActivity.this));
                    Toast.makeText(MainActivity.this,
                            com.shadowDeveloper.automail.VolleyErrorHelper.getMessage(error,MainActivity.this),
                            Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(refreshTokenRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

     */

    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if(doubleBackToExitPressedOnce) {
                finishAffinity();
            }
            this.doubleBackToExitPressedOnce=true;
            Toast.makeText(this,"Please click back again to exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            },2000);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void updateNavHeader() {
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_username);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);

        navUserName.setText(currentUser.getDisplayName());
        navUserMail.setText(currentUser.getEmail());
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG,"onNavigationItemSelected : " + item.toString() + "  " + item.getItemId());
        if (item.getItemId()==R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to logout")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                                    build();
                            GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(MainActivity.this,gso);
                            googleSignInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    Log.d(TAG,"Scope Revoked Successfully");

                                }
                            });
                            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mAuth.signOut();
                                        Log.d(TAG, "SIGN OUT successful");
                                        Intent intent = new Intent(MainActivity.this, com.shadowDeveloper.automail.LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            navController.navigate(item.getItemId());
        }
        binding.drawerLayout.closeDrawers();
        return true;
    }

}


