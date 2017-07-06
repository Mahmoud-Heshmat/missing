package com.example.mahmoudheshmat.missing.Models;


public class DatabaseURL {

    public static final String KEY_childName = "childName";
    public static final String KEY_childAge = "childAge";
    public static final String KEY_childSkinColor = "childSkinColor";
    public static final String KEY_childHairColor = "childHairColor";
    public static final String KEY_childEyeColor= "childEyeColor";
    public static final String KEY_childLength = "childLength";
    public static final String KEY_childLostDate = "childLostDate";
    public static final String KEY_longtidue = "longtidue";
    public static final String KEY_latitude = "latitude";
    public static final String KEY_addressLine = "addressLine";
    public static final String KEY_image = "image";
    public static final String KEY_subjectId = "subjectId";
    public static final String KEY_city = "city";
    public static final String KEY_country= "country";


    public static final String app_id = "6f3c7e63";
    public static final String api_key = "c00711005a61204e8a2df22a548c65fb";

    public static final String accept = "ACCEPT";
    public static final String cancel = "CANCEL";

    // Gmail email and password
    public static final String EMAIL ="heshmattest@gmail.com";
    public static final String PASSWORD ="zxc4382015123";

    //private static final String ip = "http://156.192.8.171/missing/";
    //private static final String ip = "http://10.13.178.42/missing/";
    //private static final String ip = "http://192.168.56.1/missing/";
    private static final String ip = "http://192.168.43.19/missing/";
    //private static final String ip = "http://192.168.0.101/missing/";

    public static final String login_url = ip+"login.php";
    public static final String checkUserEmail_url = ip+"checkUserEmail.php";
    public static final String register_url = ip+"registration.php";
    public static final String checkImageID_url = ip+"checkImageID.php";
    public static final String Gallery_url = ip+"Gallery.php";
    public static final String Addtogallery_url = ip+"Addtogallery.php";
    public static final String deleteFromGallery_url = ip+"deleteFromGallery.php";
    public static final String getchildBysubjectID_url = ip+"getchildBysubjectID.php";
    public static final String getDetails_url = ip+"getDetails.php";
    public static final String getGallery_url = ip+"knownGallery.php";
    public static final String getunknownGallery_url = ip+"unknownGallery.php";
    public static final String addunknownMissing_url = ip+"addunknownMissing.php";
    public static final String AddtogalleryUnknown_url = ip+"AddtogalleryUnknown.php";
    public static final String getchildBysubjectIDUnknown_url = ip+"getchildbysubjectIdUnknown.php";
    public static final String notify_url = ip+"notify.php";
    public static final String Displaynotify_url = ip+"displayNotificationContent.php";
    public static final String Displaynotify_title_url = ip+"displayNotification.php";
    public static final String userProfile_url = ip+"userProfile.php";
    public static final String updateProfile_url = ip+"updateProfile.php";
    public static final String imageprofile_url = ip+"imageprofile.php";
    public static final String imageprofile2_url = ip+"imageprofile2.php";
    public static final String deleteimage_url = ip+"deleteimage.php";
    public static final String deleteimage2_url = ip+"deleteimage2.php";
    public static final String filterKnown_url = ip+"filterKnown.php";
    public static final String filterunKnown_url = ip+"filterunKnown.php";
    public static final String DeleteNotify_url = ip+"deleteNotify.php";
    public static final String push_notification_url = ip+"push_notification.php";
    public static final String chatRooms_url = ip+"chatRooms.php";
    public static final String addRoom_url = ip+"add_chatRoom.php";
    public static final String getMessages_url = ip+"get_message.php";
    public static final String add_message_url = ip+"add_message.php";
    public static final String messageText_url = ip+"message_text.php";
    public static final String messageImage_url = ip+"message_image.php";
    public static final String onTokenRefresh_url = ip+"onTokenRefresh.php";
    public static final String chat_delete_url = ip+"chat_delete.php";
    public static final String chat_clear_url = ip+"chat_clear.php";
    public static final String OTP_add_url = ip+"OTP_add.php";
    public static final String OTP_check_url = ip+"OTP_check.php";
    public static final String change_password_url = ip+"change_password.php";





    /*public static final String checkUserEmail_url = "http://192.168.0.107/missing/checkUserEmail.php";
    public static final String register_url = "http://192.168.0.107/missing/registration.php";
    public static final String checkImageID_url = "http://192.168.0.107/missing/checkImageID.php";
    public static final String Gallery_url = "http://192.168.0.107/missing/Gallery.php";
    public static final String Addtogallery_url = "http://192.168.0.107/missing/Addtogallery.php";
    public static final String deleteFromGallery_url = "http://192.168.0.107/missing/deleteFromGallery.php";
    public static final String getchildBysubjectID_url = "http://192.168.0.107/missing/getchildBysubjectID.php";
    public static final String getDetails_url = "http://192.168.0.107/missing/getDetails.php";
    public static final String getGallery_url = "http://192.168.0.107/missing/knownGallery.php";
    public static final String getunknownGallery_url = "http://192.168.0.107/missing/unknownGallery.php";
    public static final String addunknownMissing_url = "http://192.168.0.107/missing/addunknownMissing.php";
    public static final String AddtogalleryUnknown_url = "http://192.168.0.107/missing/AddtogalleryUnknown.php";
    public static final String getchildBysubjectIDUnknown_url = "http://192.168.0.107/missing/getchildbysubjectIdUnknown.php";
    public static final String notify_url = "http://192.168.0.107/missing/notify.php";
    public static final String Displaynotify_url = "http://192.168.0.107/missing/displayNotificationContent.php";
    public static final String Displaynotify_title_url = "http://192.168.0.107/missing/displayNotification.php";
    public static final String userProfile_url = "http://192.168.0.107/missing/userProfile.php";
    public static final String updateProfile_url = "http://192.168.0.107/missing/updateProfile.php";
    public static final String imageprofile_url = "http://192.168.0.107/missing/imageprofile.php";
    public static final String imageprofile2_url = "http://192.168.0.107/missing/imageprofile2.php";
    public static final String deleteimage_url = "http://192.168.0.107/missing/deleteimage.php";
    public static final String deleteimage2_url = "http://192.168.0.107/missing/deleteimage2.php";
    public static final String filterKnown_url = "http://192.168.0.107/missing/filterKnown.php";
    public static final String filterunKnown_url = "http://192.168.0.107/missing/filterunKnown.php";
    public static final String DeleteNotify_url = "http://192.168.0.107/missing/deleteNotify.php";
    public static final String push_notification_url = "http://192.168.0.107/missing/push_notification.php";
    public static final String chatRooms_url = "http://192.168.0.107/missing/chatRooms.php";
    public static final String addRoom_url = "http://192.168.0.107/missing/add_chatRoom.php";
    public static final String getMessages_url = "http://192.168.0.107/missing/get_message.php";
    public static final String add_message_url = "http://192.168.0.107/missing/add_message.php";
    public static final String messageText_url = "http://192.168.0.107/missing/message_text.php";
    public static final String messageImage_url = "http://192.168.0.107/missing/message_image.php";
    public static final String onTokenRefresh_url = "http://192.168.0.107/missing/onTokenRefresh.php";
    public static final String chat_delete_url = "http://192.168.0.107/missing/chat_delete.php";
    public static final String chat_clear_url = "http://192.168.0.107/missing/chat_clear.php";
    public static final String OTP_add_url = "http://192.168.0.107/missing/OTP_add.php";
    public static final String OTP_check_url = "http://192.168.0.107/missing/OTP_check.php";
    public static final String change_password_url = "http://192.168.0.107/missing/change_password.php";*/


    /*
    public static final String login_url = "http://192.168.1.6:8080/missing/login.php";
    public static final String checkUserEmail_url = "http://192.168.1.6:8080/missing/checkUserEmail.php";
    public static final String register_url = "http://192.168.1.6:8080/missing/registration.php";
    public static final String checkImageID_url = "http://192.168.1.6:8080/missing/checkImageID.php";
    public static final String Gallery_url = "http://192.168.1.6:8080/missing/Gallery.php";
    public static final String Addtogallery_url = "http://192.168.1.6:8080/missing/Addtogallery.php";
    public static final String deleteFromGallery_url = "http://192.168.1.6:8080/missing/deleteFromGallery.php";
    public static final String getchildBysubjectID_url = "http://192.168.1.6:8080/missing/getchildBysubjectID.php";
    public static final String getGallery_url = "http://192.168.1.6:8080/missing/knownGallery.php";
    public static final String getunknownGallery_url = "http://192.168.1.6:8080/missing/unknownGallery.php";
    public static final String addunknownMissing_url = "http://192.168.1.6:8080/missing/addunknownMissing.php";
    public static final String AddtogalleryUnknown_url = "http://192.168.1.6:8080/missing/AddtogalleryUnknown.php";
    public static final String getchildBysubjectIDUnknown_url = "http://192.168.1.6:8080/missing/getchildbysubjectIdUnknown.php";
    public static final String notify_url = "http://192.168.1.6:8080/missing/notify.php";
    public static final String Displaynotify_url = "http://192.168.1.6:8080/missing/displayNotificationContent.php";
    public static final String userProfile_url = "http://192.168.1.6:8080/missing/userProfile.php";
    public static final String updateProfile_url = "http://192.168.1.6:8080/missing/updateProfile.php";
    public static final String imageprofile_url = "http://192.168.1.6:8080/missing/imageprofile.php";
    public static final String imageprofile2_url = "http://192.168.1.6:8080/missing/imageprofile2.php";
    public static final String deleteimage_url = "http://192.168.1.6:8080/missing/deleteimage.php";
    public static final String deleteimage2_url = "http://192.168.1.6:8080/missing/deleteimage2.php";
    public static final String filterKnown_url = "http://192.168.1.6:8080/missing/filterKnown.php";
    public static final String filterunKnown_url = "http://192.168.1.6:8080/missing/filterunKnown.php";
    public static final String DeleteNotify_url = "http://192.168.1.6:8080/missing/deleteNotify.php";
    public static final String HomeImages_url = "http://192.168.1.6:8080/missing/HomeImages.php";
    public static final String push_notification_url = "http://192.168.1.6:8080/missing/push_notification.php";
    public static final String chatRooms_url = "http://192.168.1.6:8080/missing/chatRooms.php";
    public static final String addRoom_url = "http://192.168.1.6:8080/missing/add_chatRoom.php";
    public static final String getMessages_url = "http://192.168.1.6:8080/missing/get_message.php";
    public static final String add_message_url = "http://192.168.1.6:8080/missing/add_message.php";
    public static final String onTokenRefresh_url = "http://192.168.1.6:8080/missing/onTokenRefresh.php";
    public static final String chat_delete_url = "http://192.168.1.6:8080/missing/chat_delete.php";
    public static final String chat_clear_url = "http://192.168.1.6:8080/missing/chat_clear.php";
    public static final String OTP_add_url = "http://192.168.1.6:8080/missing/OTP_add.php";
    public static final String OTP_check_url = "http://192.168.1.6:8080/missing/OTP_check.php";
    public static final String change_password_url = "http://192.168.1.6:8080/missing/change_password.php";
    public static final String Displaynotify_title_url = "http://192.168.1.6:8080/missing/displayNotification.php";
*/

}
