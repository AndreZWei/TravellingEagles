package client;

import java.io.Serializable;
import java.util.Objects;

public class Gift implements Serializable {
    private String name;
    private long time;

    public Gift(String name, long time) {
        this.name = name;
        this.time = time;
    }

    public long getTime(){
        return time;
    }

    public String getName(){
        return name;
    }

    public static class DriftBottle implements Serializable{
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