package com.appdevgenie.fcmmessenger.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {


    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{


        public MessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
