package cn.ucai.superwechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.easeui.domain.User;

import cn.ucai.superwechat.R;
import cn.ucai.superwechat.live.data.model.LiveRoom;
import cn.ucai.superwechat.live.ui.activity.LiveDetailsActivity;
import cn.ucai.superwechat.live.ui.activity.StartLiveActivity;
import cn.ucai.superwechat.ui.AddContactActivity;
import cn.ucai.superwechat.ui.AddFriendActivity;
import cn.ucai.superwechat.ui.FriendProfileActivity;
import cn.ucai.superwechat.ui.GroupsActivity;
import cn.ucai.superwechat.ui.LoginActivity;
import cn.ucai.superwechat.ui.NewGroupActivity;
import cn.ucai.superwechat.ui.RegisterActivity;
/**
 * Created by mac-yk on 2016/11/1.
 */

public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoLogin(Activity context){
        startActivity(context, LoginActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        startActivity(context,intent);
    }
    public static void startActivity(Context context, Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public static void startActivity2(Context context, Intent intent){
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out);
    }
    public static void gotoRegister(Activity context){
        startActivity(context, RegisterActivity.class);
    }

    public static void gotoUserAddContact(Activity context){
        startActivity(context, AddContactActivity.class);
    }
    public static void gotoFriendProfile(Activity context,User u){
        Intent intent=new Intent();
        intent.putExtra("user",u);
        intent.setClass(context,FriendProfileActivity.class);
        startActivity(context, intent);
    }
    public static void gotoAddFriendMSG(Activity context,String username){

        Intent intent=new Intent();
        intent.putExtra("name",username);
        intent.setClass(context,AddFriendActivity.class);
        startActivity(context, intent);
    }
    public static void gotoGroupCreated(Activity context){
        startActivity(context, NewGroupActivity.class);
    }
    public static void gotoGroups(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity, GroupsActivity.class);
        startActivity2(activity,intent);
    }
    public static void startLive(Context context, LiveRoom liveRoom){
        Intent intent=new Intent();
        intent.setClass(context,StartLiveActivity.class);
        intent.putExtra("liveroom",liveRoom);
        startActivity(context, intent);
    }
    public static void gotoLiveDetails(Context context, LiveRoom liveRoom){
        Intent intent=new Intent();
        intent.setClass(context,LiveDetailsActivity.class);
        intent.putExtra("liveroom",liveRoom);
        startActivity(context, intent);
    }


}
