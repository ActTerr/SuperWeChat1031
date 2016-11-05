package cn.ucai.superwechat.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by mac-yk on 2016/11/5.
 */

public class ExitActivityUtils {
    static  final ExitActivityUtils instance=new ExitActivityUtils();
    List<Activity> list=new LinkedList<>();

    public static ExitActivityUtils getInstance(){
        return instance;
    }
    public void addActivity(Activity activity){
        list.add(activity);
    }
    public void removeActivity(Activity activity){
        list.remove(activity);
    }
    public void exitActivity(){
        for(Activity activity:list){
            activity.finish();
        }
    }
}
