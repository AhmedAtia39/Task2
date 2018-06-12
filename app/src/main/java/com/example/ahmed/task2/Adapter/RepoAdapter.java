package com.example.ahmed.task2.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.task2.Model.GetRepo;
import com.example.ahmed.task2.Model.ListItem;
import com.example.ahmed.task2.R;

import java.util.ArrayList;

/**
 * Created by AHMED on 11/06/2018.
 */

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.ViewHolder> {

    ArrayList<ListItem> items = new ArrayList<>();
    Context context;

    public RepoAdapter(Context context, ArrayList<ListItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
        ViewHolder v = new ViewHolder(rootView);
        return v;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.repoName.setText(items.get(position).repoName);
        holder.description.setText(items.get(position).description);
        holder.userName.setText(items.get(position).userName);

        if (items.get(position).fork)
            holder.itemView.findViewById(R.id.all_section_cardView).setBackgroundColor(Color.WHITE);
        else
            holder.itemView.findViewById(R.id.all_section_cardView).setBackgroundColor(Color.GREEN);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView repoName, description, userName;

        public ViewHolder(final View itemView) {
            super(itemView);
            repoName = itemView.findViewById(R.id.txt_repo_name);
            description = itemView.findViewById(R.id.txt_description);
            userName = itemView.findViewById(R.id.txt_user_name);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("You need to go : ");
                    alert.setNegativeButton("repository url", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GetRepo g = new GetRepo(context);
                            g.afterLongItemClicked(getAdapterPosition(), "repository url");
                        }
                    });
                    alert.setPositiveButton("owner url", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GetRepo g = new GetRepo(context);
                            g.afterLongItemClicked(getAdapterPosition(), "owner url");
                        }
                    });
                    alert.show();
                    return false;
                }
            });
        }
    }
}

