package com.example.ahmed.task2.Model;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ahmed.task2.Adapter.RepoAdapter;
import com.example.ahmed.task2.Constants.Constants;
import com.example.ahmed.task2.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by AHMED on 11/06/2018.
 */

public class GetRepo {
    private final int threat_shot = 1;
    Context context;
    ArrayList<ListItem> items = new ArrayList<>();
    int i, j, allItems = 10;
    private JSONArray jsonArray;
    private RequestQueue requestQueue;
    private RepoAdapter repoAdapter;
    private StringRequest stringRequest;
    private boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private SqliteHelper sqliteHelper;

    public GetRepo(Context context) {
        this.context = context;
    }

    public void getRepos() {

        sqliteHelper = new SqliteHelper(context);

        //chech internet connection
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null && network.isConnected()) {
            initData();

            layoutManager = (LinearLayoutManager) MainActivity.recyclerView.getLayoutManager();

            // when user scroll recyclerview
            MainActivity.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!isLoading && layoutManager.getItemCount() - threat_shot == layoutManager.findLastVisibleItemPosition()) {

                        if (allItems != jsonArray.length()) {
                            allItems += 10;
                            j = i;
                            Toast.makeText(context, "loading..", Toast.LENGTH_SHORT).show();
                            loadMoreData();
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });
        } else {

            Toast.makeText(context, "get From Local", Toast.LENGTH_SHORT).show();
            repoAdapter = new RepoAdapter(context, sqliteHelper.show_all_Repos());
            MainActivity.recyclerView.setAdapter(repoAdapter);
        }

    }

    // parse first 10 repos
    public void initData() {

        sqliteHelper.deleteTable();
        items.clear();
        stringRequest = new StringRequest(Request.Method.GET, Constants.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    for (i = 0; i < allItems; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String repo_name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        JSONObject owner_user_object = jsonObject.getJSONObject("owner");
                        String userName = owner_user_object.getString("login");
                        boolean fork = jsonObject.getBoolean("fork");

                        items.add(new ListItem(repo_name, description, userName, fork));
                        repoAdapter = new RepoAdapter(context, items);
                        MainActivity.recyclerView.setAdapter(repoAdapter);

                        sqliteHelper.addRepo(repo_name, description, userName, fork);
                    }
                    j = i;

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                Log.e("error", error.getMessage() + "");
            }
        });

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // load and parse other 10 repo
    public void loadMoreData() {

        stringRequest = new StringRequest(Request.Method.GET, Constants.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    for (i = j; i < allItems; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String repo_name = jsonObject.getString("name");
                        String description = jsonObject.getString("description");
                        JSONObject owner_user_object = jsonObject.getJSONObject("owner");
                        String userName = owner_user_object.getString("login");
                        boolean fork = jsonObject.getBoolean("fork");

                        items.add(new ListItem(repo_name, description, userName, fork));
                        MainActivity.recyclerView.getAdapter().notifyDataSetChanged();

                        sqliteHelper.addRepo(repo_name, description, userName, fork);
                    }

                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                Log.e("error", error.getMessage() + "");
            }
        });

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    // when long item clicked
    public void afterLongItemClicked(final int m, final String url) {
        stringRequest = new StringRequest(Request.Method.GET, Constants.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(m);
                    String repo_html_url = jsonObject.getString("html_url");

                    JSONObject owner_url = jsonObject.getJSONObject("owner");
                    String owner_html_url = owner_url.getString("html_url");

                    if (url == "repository url")
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(repo_html_url)));
                    else if (url == "owner url")
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(owner_html_url)));
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                Log.e("error", error.getMessage() + "");
            }
        });

        requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
