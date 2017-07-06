package com.example.mahmoudheshmat.missing.Models;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class chat_items{


    private String room_id;
    private String sender_id;
    private String reciever_id;

    // for message
    private String message_id;
    private String user_id;
    private String user_name;
    private String type;
    private String content;
    private String timeStamp;

    //Constructor for rooms
    public chat_items(String room_id, String user_name){
        this.room_id = room_id;
        this.user_name = user_name;
    }

    //Constructor for messages
    public chat_items(String message_id, String room_id, String user_id, String user_name, String type, String content, String timeStamp){
        this.room_id = room_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.type = type;
        this.content = content;
        this.timeStamp = timeStamp;
        this.message_id = message_id;
    }

    //Constructor for add messages
    public chat_items(String room_id, String user_id, String user_name, String type, String content, String timeStamp){
        this.room_id = room_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.type = type;
        this.content = content;
        this.timeStamp = timeStamp;
    }


    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        if (timeStamp == null) {
            return "now";
        } else {
            SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date = null;
            try {
                date = serverFormat.parse(timeStamp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            TimeZone timeZone = TimeZone.getDefault();
            int rawOffset = timeZone.getRawOffset();
            long local = 0;
            if (date != null) {
                local = date.getTime() + rawOffset;
            }

            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(local);
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            return format.format(calendar.getTime());
        }
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

}
