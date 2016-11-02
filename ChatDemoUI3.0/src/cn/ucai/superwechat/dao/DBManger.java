package cn.ucai.superwechat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cn.ucai.superwechat.bean.UserAvatar;


/**
 * Created by mac-yk on 2016/10/24.
 */

public class DBManger  {
    private static DBManger dbManger=new DBManger();
    private DBOpenHelper dbHelper;
    void OnInit(Context context){
        dbHelper=new DBOpenHelper(context);
    }
    public static synchronized DBManger getInstance(){
        return dbManger;
    }
    public synchronized void closeDB(){
        if(dbHelper!=null){
            dbHelper.close();
        }
    }
    public synchronized boolean saveUser(UserAvatar userBean){
        SQLiteDatabase database=dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(UserDao.USER_COLUMN_NAME,userBean.getMUserName());
        values.put(UserDao.USER_COLUMN_NICK,userBean.getMUserNick());
        values.put(UserDao.USER_COLUMN_AVATAR_ID,userBean.getMAvatarId());
        values.put(UserDao.USER_COLUMN_AVATAR_TYPE,userBean.getMAvatarType());
        values.put(UserDao.USER_COLUMN_AVATAR_PATH,userBean.getMAvatarPath());
        values.put(UserDao.USER_COLUMN_AVATAR_SUFFIX,userBean.getMAvatarSuffix());
        values.put(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME,userBean.getMAvatarLastUpdateTime());
        if(database.isOpen()){
            return database.replace(UserDao.USER_TABLE_NAME,null,values)!=-1;
        }
        return false;
    }

    public synchronized boolean updateUser(UserAvatar user) {
        int result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = UserDao.USER_COLUMN_NAME + "=?";
        ContentValues values = new ContentValues();
        values.put(UserDao.USER_COLUMN_NICK, user.getMUserNick());
        if (db.isOpen()) {
            result = db.update(UserDao.USER_TABLE_NAME, values,sql,new String[]{user.getMUserName()});
        }
        return result > 0;
    }

    public synchronized UserAvatar getUser(String userName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + UserDao.USER_TABLE_NAME + " WHERE " + UserDao.USER_COLUMN_NAME + " =?";
        UserAvatar user = null;
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        if (cursor.moveToNext()) {
            user = new UserAvatar();
            user.setMUserName(userName);
            user.setMUserNick(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_NICK)));
            user.setMAvatarId(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_ID)));
            user.setMAvatarType(cursor.getInt(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_TYPE)));
            user.setMAvatarPath(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_PATH)));
            user.setMAvatarSuffix(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_SUFFIX)));
            user.setMAvatarLastUpdateTime(cursor.getString(cursor.getColumnIndex(UserDao.USER_COLUMN_AVATAR_LASTUPDATE_TIME)));
            return user;
        }
        return user;
    }


}
