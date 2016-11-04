package com.hyphenate.easeui.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;

public class EaseUserUtils {
    
    static EaseUserProfileProvider userProvider;
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    static final String TAG=EaseUserUtils.class.getName();
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    public static User getUserAppInfo(String username){
        if(userProvider!=null)
            return userProvider.getAppUser(username);

        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	User user = getUserAppInfo(username);
        if(user != null && user.getMAvatarPath() != null){
            try {
                int avatarResId = Integer.parseInt(user.getMAvatarPath());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getMAvatarPath()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        }else{
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
//        	EaseUser user = getUserInfo(username);
            User user=getUserAppInfo(username);
            Log.e(TAG,user.toString());
        	if(user != null && user.getMUserNick() != null){
                Log.e(TAG,"进入获取昵称分支");
        		textView.setText(user.getMUserNick());

        	}else{
                Log.e(TAG,"进入把昵称设置成用户名分支");
        		textView.setText(username);
        	}
        }
    }

    public static void setUserInitialLetter(User user) {

    }

    public static void setCurrentUserName( TextView wechatNumber) {
        if(wechatNumber != null){
//        	EaseUser user = getUserInfo(username);
            String username=EMClient.getInstance().getCurrentUser();
               wechatNumber.setText("微信帐号："+username);

        }
    }

    public static void setAppUserNick(TextView usernick) {
        if(usernick!=null){
            String username= EMClient.getInstance().getCurrentUser();
            setUserNick(username,usernick);
        }
    }

    public static void setAppUserAvatar(FragmentActivity activity, ImageView userAvatar) {
        if (userAvatar!=null){
            String username= EMClient.getInstance().getCurrentUser();
            setUserAvatar(activity,username,userAvatar);
        }
    }
}
