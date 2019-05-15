package com.example.administrator.test.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.administrator.test.daoJavaBean.DBHelper;
import com.example.administrator.test.daoJavaBean.SongDao;
import com.example.administrator.test.singleton.MusicListTool;

/**
 * Create by zmm
 * Time 2019/5/15
 * PackageName com.example.administrator.test
 */
public class MyContentProvider extends ContentProvider {
    private DBHelper dbHelper;
    private SongDao songDao;

    // uri匹配相关
    private static UriMatcher uriMatcher;

    // 主机名称(这一部分是可以随便取得)
    private static final String authority = "com.example.administrator.test.provider.myContentProvider";

    // 注册该内容提供者匹配的uri
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        /*
         * path_chenzheng部分的字符串是随便取得，1代表着如果请求的uri与当前加入
         * 的匹配uri正好吻合，uriMathcher.match()方法返回的值.#代表任意数字，*代表任意字符串
         */
        uriMatcher.addURI(authority, "song", 1);// 代表当前表中的所有的记录
        uriMatcher.addURI(authority, "song/#", 2);// 代表当前表中的某条特定的记录，记录id便是#处得数字
        uriMatcher.addURI(authority, "song/*", 3);//查询名字包含此字符串的
        uriMatcher.addURI(authority, "update", 4);//修改
        uriMatcher.addURI(authority, "delete", 5);//删除
        uriMatcher.addURI(authority, "insert", 6);//插入
    }

    @Override
    public boolean onCreate() {
        try {
            dbHelper = DBHelper.getInstance();
            songDao = dbHelper.initDatabase(getContext(), MusicListTool.getInstance().TABLE_NAME).getSongDao();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                String sql="select _id,KEY,NAME,SINGER,SIZE,DURATION,PATH,ALBUMID from SONG";
                String[] values = {};
                cursor = songDao.getDatabase().rawQuery(sql,values);
                break;
            case 2:
                // 从uri中解析出ID
                long id = ContentUris.parseId(uri);
                String sql1="select _id,KEY,NAME,SINGER,SIZE,DURATION,PATH,ALBUMID from SONG where _id = ?";
                String[] values1 = {id+"",};
                cursor = songDao.getDatabase().rawQuery(sql1,values1);
                break;
            default:
                throw new IllegalArgumentException("参数错误");
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int code = uriMatcher.match(uri);
        switch (code) {
            case 1:
                return "vnd.android.cursor.dir/song";
            case 2:
                return "vnd.android.cursor.item/song";
            default:
                throw new IllegalArgumentException("异常参数");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Cursor cursor = null;
        int code = uriMatcher.match(uri);
        if(code == 6){
            String sql="INSERT INTO SONG (KEY,NAME,SINGER,SIZE,DURATION,PATH,ALBUMID) VALUES (123,'123','123',123,123,'123',123)";
            String[] values = {};
            songDao.getDatabase().execSQL(sql);
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Cursor cursor = null;
        int code = uriMatcher.match(uri);
        if(code == 5){
            String sql="DELETE FROM SONG WHERE ";
            String[] values = {};
            songDao.getDatabase().execSQL(sql + s,strings);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Cursor cursor = null;
        int code = uriMatcher.match(uri);
        if(code == 4){
            String sql="UPDATE SONG SET NAME = '修改的名字'WHERE ";
            String[] values = {};
            songDao.getDatabase().execSQL(sql + s,strings);
        }
        return 0;
    }
}
