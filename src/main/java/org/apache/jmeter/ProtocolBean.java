package org.apache.jmeter;

public class ProtocolBean {
    private byte type;
    private byte flag;
    private int length;
    private String content;

    public ProtocolBean(byte type, byte flag, int length, String content) {
        this.type = type;
        this.flag = flag;
        this.length = length;
        this.content = content;
    }


    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getFlag() {
        return flag;
    }

    public void setFlag(byte flag) {
        this.flag = flag;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
