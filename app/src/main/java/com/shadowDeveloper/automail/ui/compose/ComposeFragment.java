package com.shadowDeveloper.automail.ui.compose;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.Utility.GmailMessages;
import com.shadowDeveloper.automail.Utility.Header;
import com.shadowDeveloper.automail.Utility.Payload;
import com.shadowDeveloper.automail.VolleyErrorHelper;
import com.shadowDeveloper.automail.databinding.FragmentComposeBinding;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import static android.content.Context.HARDWARE_PROPERTIES_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class ComposeFragment extends Fragment {

    private static final String TAG = "COMPOSE FRAGMENT";
    //private ComposeViewModel composeViewModel;
    private FragmentComposeBinding binding;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;
    RelativeLayout relativeLayout3;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String mAccessToken,sendmsgurl;
    List<Header> headers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //composeViewModel =
        //new ViewModelProvider(this).get(ComposeViewModel.class);

        binding = FragmentComposeBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        SharedPreferences sp= requireActivity().getSharedPreferences("datafile", MODE_PRIVATE);
        if(sp.contains("access_token")){
            mAccessToken=sp.getString("access_token","");
            Log.d(TAG,mAccessToken);

        }
        sendmsgurl="https://www.googleapis.com/gmail/v1/users/me/messages/send";

        EditText subject = root.findViewById(R.id.compose_subject);
        EditText body = root.findViewById(R.id.compose_body);
        EditText to = root.findViewById(R.id.compose_to);
        EditText cc = root.findViewById(R.id.compose_cc);
        EditText bcc = root.findViewById(R.id.compose_bcc);
        TextView from = root.findViewById(R.id.compose_from);

        relativeLayout = root.findViewById(R.id.compose_from_container);
        relativeLayout2 = root.findViewById(R.id.compose_cc_container);
        relativeLayout3 = root.findViewById(R.id.compose_bcc_container);
        (root.findViewById(R.id.compose_to_container)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to.requestFocus();
            }
        });
        relativeLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc.requestFocus();
            }
        });
        relativeLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bcc.requestFocus();
            }
        });

        (root.findViewById(R.id.compose_subject_container)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subject.requestFocus();
            }
        });



        mAuth=FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();
        String from_email= null;
        if (currentUser != null) {
            from_email = currentUser.getDisplayName()+"\n"+currentUser.getEmail();
        }
        from.setText(from_email);

        ImageButton imageButton = root.findViewById(R.id.compose_toggle_from_cc_bcc);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = View.VISIBLE;
                boolean z = relativeLayout2.getVisibility() == View.VISIBLE;
                relativeLayout2.setVisibility(z ? View.GONE : View.VISIBLE);
                RelativeLayout relativeLayout4 = relativeLayout3;
                RelativeLayout relativeLayout5 = relativeLayout;
                if (z) {
                    i = View.GONE;
                }
                relativeLayout4.setVisibility(i);
                relativeLayout5.setVisibility(i);
                imageButton.setImageResource(z ? R.drawable.toggle_arrow_down : R.drawable.toggle_arrow_up);

            }
        });

        TextView closeButton = root.findViewById(R.id.compose_close);
        closeButton.setOnClickListener(view -> clickExecuted());

        TextView send = root.findViewById(R.id.compose_send);
        send.setOnClickListener(view -> sendMessage(to.getText().toString(),
                currentUser.getEmail(),
                cc.getText().toString(),
                bcc.getText().toString(),
                subject.getText().toString(),
                body.getText().toString(),
                mAccessToken));

        return root;
    }

    private NavController getNavController() {
        Fragment fragment = getParentFragment();
        if(!(fragment instanceof NavHostFragment)) {
            throw new IllegalStateException("Activity"+this+"does not have a NavHostFragment");

        }
        return ((NavHostFragment)fragment).getNavController();
    }

    public void clickExecuted() {
        getNavController().navigate(R.id.nav_home);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public String createEmail(String to, String from,String subject,String bodymsg) {
        GmailMessages sendMessage = new GmailMessages();
        sendMessage.setSnippet(bodymsg);
        Header[] header=new Header[3];
        header[0]=new Header();
        header[1]=new Header();
        header[2]=new Header();

        header[0].setName("To");
        header[0].setValue(to);
        header[1].setName("From");
        header[1].setValue(from);
        header[2].setName("Subject");
        header[2].setValue(subject);

        headers=new ArrayList<Header>();

        headers.add(0,header[0]);
        headers.add(1,header[1]);
        headers.add(2,header[2]);

        Payload payload = new Payload();
        payload.setHeaders(headers);
        payload.setMimeType("text/plain");
        sendMessage.setPayload(payload);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String message = gson.toJson(sendMessage);
        Log.d(TAG,message);
        byte[] encodedBytes = message.getBytes(StandardCharsets.UTF_8);
        String base64 = Base64.encodeToString(encodedBytes,Base64.DEFAULT);
        Log.d(TAG, base64);
        return base64;
    }

    public String createBase64(String from,String to,String cc,String bcc,String subject){

        String from_mail="From: "+from;
        String to_mail="To: "+to;
        String subject_string="Subject: "+subject;
        String cc_mail="Cc: "+cc;
        String bcc_mail="Bcc: "+bcc;
        String head= from_mail+"\n"+to_mail+"\n";
        if(!cc.isEmpty()){
            head=head+cc_mail+"\n";
        }
        if(!bcc.isEmpty()){
            head=head+bcc_mail+"\n";
        }
        head=head+subject_string;
        byte[] encodedBytes = head.getBytes(StandardCharsets.UTF_8);
        String base64= Base64.encodeToString(encodedBytes,Base64.DEFAULT);

        base64=base64.replaceAll("[\\n\\t]","");
        Log.d(TAG,base64);
        return base64;
    }

    public void sendMessage(String to, String from,String cc,String bcc,String subject,String bodymsg, String mAccessToken) {
        if (to.isEmpty()) {
            Toast.makeText(requireContext(), "To field is empty", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("raw", createBase64(to, from, cc, bcc, subject));

                JsonObjectRequest sendRequest = new JsonObjectRequest(Request.Method.POST, sendmsgurl, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        Toast.makeText(requireContext(), "E-mail sent", Toast.LENGTH_SHORT).show();
                        getNavController().navigate(R.id.nav_history);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(requireContext(), "Mail not sent\n" + VolleyErrorHelper.getMessage(error, requireContext()), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Bearer " + mAccessToken);
                        headers.put("Content-type", "application/json");
                        return headers;
                    }
                };
                sendRequest.setRetryPolicy(new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(sendRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

}


