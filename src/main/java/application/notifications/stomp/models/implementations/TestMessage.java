package application.notifications.stomp.models.implementations;

import application.notifications.stomp.models.IMessage;

public class TestMessage implements IMessage {

    public TestMessage(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }

    public TestMessage() {
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    private String field1;
    private String field2;

}
