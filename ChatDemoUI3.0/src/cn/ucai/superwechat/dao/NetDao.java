package cn.ucai.superwechat.dao;

import android.content.Context;
import android.os.Message;

import java.io.File;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.data.OkHttpUtils;
import cn.ucai.superwechat.utils.MD5;


/**
 * Created by clawpo on 2016/10/17.
 */

public class NetDao {



    public static void findUser(Context context,String userName,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_USER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void UserLogin(Context context,String userName,String passwd,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_LOGIN)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.PASSWORD,passwd)
                .targetClass(String.class)
                .execute(listener);

    }
    public static void UserRegister(Context context,String userName,String passwd,String userNick,OkHttpUtils.OnCompleteListener<Result> listener){
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_REGISTER)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.PASSWORD, MD5.getMessageDigest(passwd))
                .addParam(I.User.NICK,userNick)
                .targetClass(Result.class)
                .post()
                .execute(listener);
    }
    public static void UnRegister(Context context,String userName,OkHttpUtils.OnCompleteListener<Result> listener){
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UNREGISTER)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(Result.class)
                .execute(listener);
    }

    public static void updateAvatar(Context context,String userName,String avatarType,File file,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_AVATAR)
                .addParam(I.NAME_OR_HXID,userName)
                .addParam(I.AVATAR_TYPE,avatarType)
                .addFile2(file)
                .post()
                .targetClass(String.class)
                .execute(listener);
    }
    public static void updateNick(Context context,String userName,String nick,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_UPDATE_USER_NICK)
                .addParam(I.User.USER_NAME,userName)
                .addParam(I.User.NICK,nick)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void downloadAvatar(Context context,String userName,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_AVATAR)
                .addParam(I.NAME_OR_HXID,userName)
                .addParam(I.AVATAR_TYPE,I.AVATAR_TYPE_USER_PATH)
                .addParam(I.Avatar.AVATAR_SUFFIX,I.AVATAR_SUFFIX_JPG)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void addContact(Context context,String username,String contactName,OkHttpUtils.OnCompleteListener<String> listener){
        OkHttpUtils<String> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_ADD_CONTACT)
                .addParam(I.Contact.USER_NAME,username)
                .addParam(I.Contact.CU_NAME,contactName)
                .targetClass(String.class)
                .execute(listener);
    }
    public static void deleteContact(Context context,String username,String contactName,OkHttpUtils.OnCompleteListener<Result> listener){
        OkHttpUtils<Result> utils=new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_DELETE_CONTACT)
                .addParam(I.Contact.USER_NAME,username)
                .addParam(I.Contact.CU_NAME,contactName)
                .targetClass(Result.class)
                .execute(listener);
    }


}