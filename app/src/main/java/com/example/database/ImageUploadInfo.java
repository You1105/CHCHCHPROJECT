package com.example.database;

public class ImageUploadInfo {

    private String imageName;

    private  String imageURL;

    private String text;

    private String category;

    public ImageUploadInfo() {

    }

    public ImageUploadInfo(String imageName, String imageURL,String text,String category) {

        this.imageName = imageName;
        this.imageURL= imageURL;
        this.text= text;
        this.category=category;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName=imageName;}
    public String getImageURL() {
        return imageURL;
    }
    public void getImageURL(String imageURL) {

        this.imageURL=imageURL;
    }
    public String gettext() {

        return text;
    }
    public void gettext(String text){
        this.text=text;
    }
    public String getcategory() {
        return category;
    }
    public void setcategory(String category){
        this.category=category;
    }


}