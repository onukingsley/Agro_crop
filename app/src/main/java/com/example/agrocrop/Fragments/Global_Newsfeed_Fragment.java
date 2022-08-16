package com.example.agrocrop.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agrocrop.Global_News_adapter;
import com.example.agrocrop.NewsModel;
import com.example.agrocrop.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Global_Newsfeed_Fragment extends Fragment {
    ListView listview_container;
    ArrayList<NewsModel> models = new ArrayList<>();


    public Global_Newsfeed_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_global__newsfeed, container, false);
        Global_News_adapter adapter = new Global_News_adapter(view.getContext(),models);
        stringrequest(view,adapter);
        listview_container = view.findViewById(R.id.listview_container);

        listview_container.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        return view;
    }

    public void stringrequest(View view, Global_News_adapter adapter){
        StringRequest request = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/everything?q=agriculture%20and%20planting%20and%20harvest&from=2022-07-22 &sortBy=publishedAt&apiKey=819b3b2ae72f4ed48335a601245879c9", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response.toString());
                try {
                    JSONObject  main = new JSONObject(response);
                    JSONArray articles = main.getJSONArray("articles");
                    for (int i = 0; i<articles.length();i++){
                        JSONObject content = articles.getJSONObject(i);
                        NewsModel model = new NewsModel(content.getString("title"),
                                content.getString("description"),
                                content.getString("url"),
                                content.getString("urlToImage"),
                                content.getString("content"),
                                content.getString("author"),
                                content.getString("publishedAt"));
                                models.add(model);
                                adapter.notifyDataSetChanged();
                    }
                }catch (Exception  e){
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "error fetching info "+ error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0");

                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(request);


    }
}