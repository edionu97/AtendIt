package application.messages.request;

import java.io.Serializable;

public class HistoryForAtMessage implements Serializable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsern() {
        return usern;
    }

    public void setUsern(String usern) {
        this.usern = usern;
    }

    private int id;
    private String usern;
}
