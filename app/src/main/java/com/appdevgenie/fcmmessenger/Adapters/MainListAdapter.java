package com.appdevgenie.fcmmessenger.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appdevgenie.fcmmessenger.Activities.MessengerActivity;
import com.appdevgenie.fcmmessenger.Models.User;
import com.appdevgenie.fcmmessenger.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.appdevgenie.fcmmessenger.Utils.Constants.PARSE_UID;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder> {

    private Context context;
    private ArrayList<User> users;

    public MainListAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_list, parent, false);
        return new MainListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainListViewHolder holder, int position) {

        User user = users.get(holder.getAdapterPosition());

        holder.tvUserName.setText(user.getDisplayName());
        holder.tvUserEmail.setText(user.getEmail());

        Picasso.with(context)
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.user_placeholder)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if(users != null){
            return users.size();
        }else {
            return 0;
        }
    }

    class MainListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView tvUserName;
        private TextView tvUserEmail;

        MainListViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivUserPhoto);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserEmail = itemView.findViewById(R.id.tvUserEmail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String uid = users.get(getAdapterPosition()).getUid();

            Intent intent = new Intent(context, MessengerActivity.class);
            intent.putExtra(PARSE_UID, uid);
            context.startActivity(intent);
        }
    }
}
