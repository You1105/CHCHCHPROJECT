package com.example.database;

public class User {

    //변수 선언
    public String email;
    public String key;
    public String photo;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    //이메일, 키, 사진을 가져오는 Getter와 Setter 설정

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
