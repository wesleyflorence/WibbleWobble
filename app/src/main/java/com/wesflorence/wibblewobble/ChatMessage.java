package com.wesflorence.wibblewobble;

import java.util.Objects;

/**
 * Represents a a chat message with a username and message.
 */
public class ChatMessage {

    private String user;
    private String message;

    ChatMessage() { }
    /**
     * Creates an instance of a chat message given user and message.
     * @param user the user name
     * @param message the message
     */
    public ChatMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }

    /**
     * Gets the username.
     * @return the username
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the username.
     * @param user username to be set
     */
    public void setUser(String user) { this.user = user; }

    /**
     * Gets the message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(user, that.user) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, message);
    }
}
