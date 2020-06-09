package com.example.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMail extends AsyncTask<Void,Void,Void> {

    //Declaring Variables
    public static   Context context;
    private Session session;


    private String email;
    private String subject;
    private String message;
    private String user;
    private String password;



    public static String emailCode;


    private ProgressDialog progressDialog;


    public SendMail(Context context, String email, String subject, String message){
       //변수 선언
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.emailCode=createEmailCode();

    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();

        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);

    }

    public static String createEmailCode() { //이메일 인증코드 생성 8자리 난수 생성
        String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        String newCode = new String();

        for (int x = 0; x < 8; x++) {
            int random = (int) (Math.random() * str.length);
            newCode += str[random];
        }

        return newCode;
    }



    @Override
    public  void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();

        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }


    public Void doInBackground(final Void... params) {

        Properties props = new Properties();




        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {

            MimeMessage mm = new MimeMessage(session);

            //수신인설정
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //제목
            mm.setSubject("이메일 인증번호");
            //보낼 내용
            mm.setText(emailCode);
            //보내기
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }







}