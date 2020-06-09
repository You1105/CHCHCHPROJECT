package com.example.database;

public class User {

    //변수 선언
    public String email;
    public String key;
    public String photo;

    public User() {

    }

    //email, key, photo 변수를 변경, 호출하기 위한 getter와 setter 설정

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
