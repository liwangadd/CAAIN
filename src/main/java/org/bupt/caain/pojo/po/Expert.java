package org.bupt.caain.pojo.po;

public class Expert {

    private int id;
    private String num;
    private String ip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Expert{" +
                "id=" + id +
                ", num='" + num + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }
}
