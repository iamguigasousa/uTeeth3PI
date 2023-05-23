package br.com.puccampinas.uteeth3pi.recycleview;

public class User {

    String name;
    long phone;

    public User(){}

    public User(String name, long phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }
}
