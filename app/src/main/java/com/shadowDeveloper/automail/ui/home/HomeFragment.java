package com.shadowDeveloper.automail.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shadowDeveloper.automail.Adapters.HomeAdapter;
import com.shadowDeveloper.automail.R;
import com.shadowDeveloper.automail.Utility.GmailMessages;
import com.shadowDeveloper.automail.Utility.Header;
import com.shadowDeveloper.automail.Utility.Mails;
import com.shadowDeveloper.automail.Utility.Message;
import com.shadowDeveloper.automail.Utility.Payload;
import com.shadowDeveloper.automail.VolleyErrorHelper;
import com.shadowDeveloper.automail.databinding.FragmentHomeBinding;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    ArrayList<HomeViewModel> homeViewModels;
    List<Message> messages;
    List<Header> headers;
    String subject,from,description,date,time;
    String token_url = "https://oauth2.googleapis.com/token";

    private static final String TAG ="HOME FRAGMENT";

    HomeAdapter adapter;

    String message_url,batch_url,host_url;
    RequestQueue requestQueue;
    String mAccessToken,mRefreshToken;

    Boolean isScrolling = false;

    int currentItems,totalItems,scrolledOutItems;
    String nextPageToken = "";
    SpinKitView progress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        requestQueue = Volley.newRequestQueue(requireContext());

        recyclerView=root.findViewById(R.id.recHomeView);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        homeViewModels=new ArrayList<>();
        adapter = new HomeAdapter(homeViewModels);
        recyclerView.setAdapter(adapter);

        progress=root.findViewById(R.id.spin_kit);


        message_url = "https://www.googleapis.com/gmail/v1/users/me/messages";
        batch_url = "/batch/gmail/v1";
        host_url = "https://www.googleapis.com";

        SharedPreferences sp= requireActivity().getSharedPreferences("datafile", MODE_PRIVATE);
        if(sp.contains("access_token")){
            mAccessToken=sp.getString("access_token","");
            Log.d(TAG,mAccessToken);

        }

        if(sp.contains("refresh_token")) {
            mRefreshToken=sp.getString("refresh_token","");
            Log.d(TAG,mRefreshToken);
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling=true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                /*
                Visible Items + Scrolled Out Items == Total Items then Fetch new items
                 */

                currentItems=manager.getChildCount();
                totalItems=manager.getItemCount();
                scrolledOutItems=manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems+scrolledOutItems == totalItems)) {
                    fetchGmail(mAccessToken);
                }
            }
        });

        fetchGmail(mAccessToken);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchGmail(String access_token) {
        String url = message_url;
        if(!nextPageToken.equals("")) {
            url=url+"?pageToken"+nextPageToken;
        }
        if(nextPageToken==null){
            return;
        }
        progress.setVisibility(View.VISIBLE);
        JsonObjectRequest gmailRequest = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson =gsonBuilder.create();
                Mails mails =gson.fromJson(response.toString(),Mails.class);
                messages= mails.getMessages();
                nextPageToken=mails.getNextPageToken();
                Log.d(TAG,String.valueOf(messages.size()));
                for(int i=0;i<messages.size();i++){
                    fetchMessages(messages.get(i).getId());
                }

                progress.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse.statusCode==401) {
                    refreshAccessToken();
                }else {
                    Toast.makeText(getContext(),"Some Error occurred\nPlease retry after some time",Toast.LENGTH_LONG).show();
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
        gmailRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(gmailRequest);
    }



    private void fetchMessages(String id ) {

        JsonObjectRequest messageRequest = new JsonObjectRequest(Request.Method.GET,
                message_url + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson =gsonBuilder.create();
                GmailMessages gmailMessages = gson.fromJson(response.toString(),GmailMessages.class);
                Payload payload = gmailMessages.getPayload();
                headers = payload.getHeaders();
                for (int i = 0; i<headers.size(); i++) {
                    if(headers.get(i).getName().equals("Subject")) {
                       subject= headers.get(i).getValue();
                    }
                    if(headers.get(i).getName().equals("From")) {

                        String from_imm= headers.get(i).getValue();
                        String[] split = from_imm.split("<");
                        from=split[0];
                    }
                    if(headers.get(i).getName().equals("Date")) {
                        String[] split = headers.get(i).getValue().split(" ");
                        date=split[1]+" "+split[2]+" "+split[3];
                        time=split[4];

                    }
                }

                if(gmailMessages.getSnippet().length()<50){
                    description=gmailMessages.getSnippet();
                }
                else{
                    description=gmailMessages.getSnippet().substring(0,50)+"...";
                }



                HomeViewModel msg = new HomeViewModel(R.drawable.ic_dot,from,date,time,subject,description);
                homeViewModels.add(msg);
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, VolleyErrorHelper.getMessage(error,getContext()));
                Toast.makeText(getContext(),
                        VolleyErrorHelper.getMessage(error,getContext()),
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
        messageRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(messageRequest);
    }


    private void refreshAccessToken() {
        SharedPreferences sp= requireActivity().getSharedPreferences("datafile",MODE_PRIVATE);
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("client_id", getString(R.string.default_web_client_id));
            jsonBody.put("client_secret", getString(R.string.default_client_secret));
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
                                SharedPreferences sp= requireActivity().getSharedPreferences("datafile",MODE_PRIVATE);
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
                    Log.d(TAG,VolleyErrorHelper.getMessage(error,getContext()));
                    Toast.makeText(getContext(),
                            VolleyErrorHelper.getMessage(error,getContext()),
                            Toast.LENGTH_LONG).show();
                }
            });
            requestQueue.add(refreshTokenRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

