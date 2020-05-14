package com.example.database;

public class Chat {

<<<<<<< HEAD
    //변수 선언
=======
>>>>>>> github/master
    public String email;
    public String text;

    public Chat() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

<<<<<<< HEAD
    //변수 값을 가져오거나, 설정하기 위한 Getter와 Setter

=======
>>>>>>> github/master
    public Chat(String text) {
        this.text = text;
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
