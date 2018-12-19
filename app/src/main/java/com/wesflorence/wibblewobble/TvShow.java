package com.wesflorence.wibblewobble;

/**
 * Represents a tv show class with the channel and show name.
 */
public class TvShow {

    String channel;
    String show;

    public TvShow(){}

    /**
     * Creates an instance of a tv show.
     * @param channel the channel
     * @param show the show
     */
    public TvShow(String channel, String show) {
        this.channel = channel;
        this.show = show;
    }


    @Override
    public String toString() {
        return channel + " : " + show;
    }

    /**
     * Gets the show.
     * @return the tv show
     */
    public String getShow() {
        return show;
    }

    /**
     * Gets the channel.
     * @return the channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * Sets the channel.
     * @param channel channel to be set
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * Sets the show.
     * @param show show to be set
     */
    public void setShow(String show) {
        this.show = show;
    }

}