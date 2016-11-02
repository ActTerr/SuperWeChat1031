package cn.ucai.superwechat.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.ucai.superwechat.I;


/**
 * Created by mac-yk on 2016/10/24.
 */

public class DBOpenHelper extends SQLiteOpenHelper{
    private final static int DATABASEVERSION=1;
    private static DBOpenHelper instance;
    private static final String USER_TABLE_CREATE = "CREATE TABLE "
            + UserDao.USER_TABLE_NAME + " ("
            + UserDao.USER_COLUMN_NAME + " TEXT PRIMARY KEY, "
            + UserDao.USER_COLUMN_NICK + " TEXT, "
            + UserDao.USER_COLUMN_AVATAR_ID + " INTEGER, "
            + UserDao.USER_COLUMN_AVATAR_TYPE + " INTEGER, "
            + UserDao.USER_COLUMN_AVATAR_PATH + " TEXT, "
            + UserDao.USER_COLUMN_AVATAR_SUFFIX + " TEXT, "
            + UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME + " TEXT);";
    public DBOpenHelper(Context context) {
        super(context,getUserDatabaseName(),null,DATABASEVERSION);
    }
    public static DBOpenHelper onInit(Context context){
        if(instance==null){
            instance=new DBOpenHelper(context);
        }
        return instance;
    }
    private static String getUserDatabaseName() {
        return I.User.TABLE_NAME+"_demo.db";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
