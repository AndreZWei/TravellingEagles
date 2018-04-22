import java.util.Objects;

public class DriftBottle {
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