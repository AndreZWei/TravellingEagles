package client;

import java.io.Serializable;

public class Gift implements Serializable {
    private String gift;
    private long time;

    public Gift(String gift, long time) {
        this.gift = gift;
        this.time = time;
    }

    public long getTime(){
        return time;
    }

    public String getGift(){
        return gift;
    }
}