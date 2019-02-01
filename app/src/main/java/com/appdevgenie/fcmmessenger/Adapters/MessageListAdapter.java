package com.appdevgenie.fcmmessenger.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdevgenie.fcmmessenger.Models.Message;
import com.appdevgenie.fcmmessenger.R;

import java.util.ArrayList;
import java.util.Date;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_SENT_WITH_DATE = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_RECEIVED_WITH_DATE = 3;

    private Context context;
    private ArrayList<Message> messageArrayList;
    private String senderUserId;

    public MessageListAdapter(Context context, ArrayList<Message> messageArrayList, String senderUserId) {
        this.context = context;
        this.messageArrayList = messageArrayList;
        this.senderUserId = senderUserId;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_SENT:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
                break;

            case VIEW_TYPE_SENT_WITH_DATE:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
                break;

            case VIEW_TYPE_RECEIVED:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
                break;

            case VIEW_TYPE_RECEIVED_WITH_DATE:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false);
                break;

            default:
                view = LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {

        Message message = messageArrayList.get(position);

        if (message.getFrom().equals(senderUserId)) {
            return VIEW_TYPE_SENT;

        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        Message message = messageArrayList.get(holder.getAdapterPosition());

        String dateString = DateFormat.format("EEEE dd MMM yyyy  HH:mm", new Date(message.getDayTimestamp())).toString();

        holder.tvMessageBody.setText(message.getBody());
        holder.tvMessageDate.setText(dateString);
    }

    @Override
    public int getItemCount() {
        if (messageArrayList != null) {
            return messageArrayList.size();
        } else {
            return 0;
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView tvMessageBody;
        private TextView tvMessageDate;

        MessageViewHolder(View itemView) {
            super(itemView);

            tvMessageBody = itemView.findViewById(R.id.tvMessageBody);
            tvMessageDate = itemView.findViewById(R.id.tvMessageDate);
        }
    }


}
