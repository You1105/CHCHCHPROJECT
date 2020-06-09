package com.example.database;


public class Chat {

    //변수 선언
    public String email;
    public String text;
    public String photo;

    public Chat() {

    }
    //email, text, photo 변수를 변경, 호출하기 위한 getter와 setter 설정

    public Chat(String text) {
        this.text = text;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}