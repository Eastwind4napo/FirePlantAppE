package com.example.kaumadi.fireplantapp.main;

/**
 * Created by Eastwind on 4/3/2017.
 */

public class User {
    private String uid,fname,lname,uname,mail,pwd,post;

    public User() {
    }

    public User(String uid, String fname, String lname, String uname, String mail, String pwd, String post) {
        this.uid = uid;
        this.fname = fname;
        this.lname = lname;
        this.uname = uname;
        this.mail = mail;
        this.pwd = pwd;
        this.post = post;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}


