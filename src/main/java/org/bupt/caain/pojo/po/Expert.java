package org.bupt.caain.pojo.po;

public class Expert {

    //    自增主键
    private int id;

    //    专家编号
    private String num;

    //    专家IP
    private String ip;

    private boolean pre_voted;

    //    是否已投票
    private boolean voted;

    public Expert(String num, String ip) {
        this.num = num;
        this.ip = ip;
        this.pre_voted = false;
        this.voted = false;
    }

    public Expert() {
    }

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

    public boolean isPre_voted() {
        return pre_voted;
    }

    public void setPre_voted(boolean pre_voted) {
        this.pre_voted = pre_voted;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    @Override
    public String toString() {
        return "Expert{" +
                "id=" + id +
                ", num='" + num + '\'' +
                ", ip='" + ip + '\'' +
                ", pre_voted=" + pre_voted +
                ", voted=" + voted +
                '}';
    }
}
