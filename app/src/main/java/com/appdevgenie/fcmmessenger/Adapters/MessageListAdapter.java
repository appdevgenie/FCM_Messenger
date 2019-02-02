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
import java.util.Calendar;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    private static final int VIEW_TYPE_SENT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;

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

            case VIEW_TYPE_RECEIVED:
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

        //String dateString = DateFormat.format("EEEE dd MMM yyyy  HH:mm", new Date(message.getTimestamp())).toString();

        holder.tvMessageBody.setText(message.getBody());
        holder.tvMessageDate.setText(getFormattedDate(message.getTimestamp()));
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

    private String getFormattedDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "HH:mm";
        final String dateTimeFormatString = "EEEE, MMMM d, HH:mm";
        //final long HOURS = 60 * 60 * 60;

        if (now.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
            return "Today " + DateFormat.format(timeFormatString, calendar);
        } else if (now.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1) {
            return "Yesterday " + DateFormat.format(timeFormatString, calendar);
        } else if (now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, calendar).toString();
        } else {
            return DateFormat.format("MMMM dd yyyy, HH:mm", calendar).toString();
        }
    }
}
