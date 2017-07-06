package com.example.mahmoudheshmat.missing.Models;


public class helper_items {
    private String childName;
    private String age;
    private String skinColor;
    private String hairColor;
    private String eyeColor;
    private String length;
    private String Lostdate;
    private String imagePath;
    private String addressLine;
    private String city;
    private String country;
    private String user_id;
    private String uploadDate;

    // Notification Items
    private String imagePath2;
    private String phone;
    private String time;
    private String longti;
    private String latit;

    //Delete image Items
    private String child_id;
    private String image_id;
    private String location_id;
    private String subject_id;
    private String key;
    private String notify_id;


    // for unknown child
    public helper_items(String imagePath, String addressLine, String city, String country,String user_id, String uploadDate){

        this.imagePath = imagePath;
        this.addressLine =addressLine;
        this.city = city;
        this.country = country;
        this.user_id = user_id;
        this.uploadDate = uploadDate;
    }

    public helper_items(String imagePath, String addressLine, String city, String country,String user_id,
                     String uploadDate, String image_id, String subject_id, String location_id, String key){

        this.imagePath = imagePath;
        this.addressLine =addressLine;
        this.city = city;
        this.country = country;
        this.user_id = user_id;
        this.uploadDate = uploadDate;
        this.image_id = image_id;
        this.subject_id = subject_id;
        this.location_id = location_id;
        this.key = key;
    }

    public helper_items(String childName, String phone, String imagePath, String imagePath2, String time,String longti,
                     String latit, String notify_id){

        this.imagePath = imagePath;
        this.phone = phone;
        this.time = time;
        this.imagePath2 = imagePath2;
        this.childName = childName;
        this.longti = longti;
        this.latit = latit;
        this.notify_id = notify_id;
    }

    public helper_items(String childName, String imagePath2, String time, String notify_id){

        this.time = time;
        this.imagePath2 = imagePath2;
        this.childName = childName;
        this.notify_id = notify_id;
    }

    public helper_items(String imagePath){

        this.imagePath = imagePath;
    }
    public String getImagePath2() {
        return imagePath2;
    }

    public void setImagePath2(String imagePath2) {
        this.imagePath2 = imagePath2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLongti() {
        return longti;
    }

    public void setLongti(String longti) {
        this.longti = longti;
    }

    public String getLatit() {
        return latit;
    }

    public void setLatit(String latit) {
        this.latit = latit;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(String skinColor) {
        this.skinColor = skinColor;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getLostdate() {
        return Lostdate;
    }

    public void setLostdate(String lostdate) {
        Lostdate = lostdate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }
}
