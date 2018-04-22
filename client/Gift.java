package client;

import java.io.Serializable;
import java.util.Objects;

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

    public static class DriftBottle {
        private Gift gift;
        private String msg;

        public DriftBottle(Gift gift, String msg) {
            this.gift = gift;
            this.msg = Objects.toString(gift.getTime())+": "+msg;
        }

        public String getMessage(){
            return msg;
        }

        public Gift getGift(){
            return gift;
        }


    }
}