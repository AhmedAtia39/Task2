package com.example.ahmed.task2;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ahmed.task2.Model.GetRepo;


public class MainActivity extends AppCompatActivity {

    public static RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GetRepo getRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.list_repo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //Request the GitHub API and parse the JSON response
        getRepo = new GetRepo(this);
        getRepo.getRepos();

        //Implement swipe-to-refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "refresh", Toast.LENGTH_SHORT).show();
                getRepo.getRepos();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
