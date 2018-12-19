package com.wesflorence.wibblewobble;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import com.wesflorence.wibblewobble.R;

/**
 * Represents a recycler view adapter for the chat messages with the context, message list, and
 * the username
 */
public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final int SENDER = 1;
    private static final int RECEIVER = 2;

    private Context context;
    private List<ChatMessage> messageList;
    private String thisUser;

    /**
     * Creates an instance of the chat message adapter given the following:
     * @param context the context
     * @param messageList the message list
     * @param user the user name
     */
    public ChatMessageAdapter(Context context, List<ChatMessage> messageList, String user) {
        this.context = context;
        this.messageList = messageList;
        this.thisUser = user;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) messageList.get(position);

        if (message.getUser().equals(this.thisUser)) {
            return SENDER;
        } else {
            return RECEIVER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == SENDER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatmessage_sender, parent, false);
            Log.d("Adapter", "new Sender");
            return new SendChatHolder(view);
        }
        if (viewType == RECEIVER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chatmessage_receiver, parent, false);
            Log.d("Adapter", "new Receiver");
            return new ReceivedChatHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = (ChatMessage) messageList.get(position);

        if (holder.getItemViewType() == SENDER) {
            ((SendChatHolder) holder).bind(message);
        }
        if (holder.getItemViewType() == RECEIVER) {
            ((ReceivedChatHolder) holder).bind(message);
        }
    }


    /**
     * Represents a view holder class for received messages with the message and username view
     * attributes.
     */
    private class ReceivedChatHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView username;

        /**
         * Creates an instance of the received chat holder class.
         * @param itemView the item view
         */
        ReceivedChatHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message_input);
            username = (TextView) itemView.findViewById(R.id.message_name);
        }

        /**
         * Sets the chat message to the message and username views.
         * @param chatMessage the chat message
         */
        void bind(ChatMessage chatMessage) {
            message.setText(chatMessage.getMessage());
            username.setText(chatMessage.getUser());
        }
    }

    /**
     * Represents a view holder class for sent messages with a message view attribute.
     */
    private class SendChatHolder extends RecyclerView.ViewHolder {
        TextView message;

        /**
         * Creates an instance of the send chat holder class.
         * @param itemView the item view
         */
        SendChatHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message_input);
        }

        /**
         * Sets the chat message as the message view's text.
         * @param chatMessage
         */
        void bind(ChatMessage chatMessage) {
            message.setText(chatMessage.getMessage());
        }
    }

}
