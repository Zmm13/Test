package com.example.administrator.test.daoJavaBean;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Create by zmm
 * Time 2019/5/7
 * PackageName com.example.administrator.test.daoJavaBean
 */
public class DBHelper {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper helper;
    private static DBHelper instance;
    private static SQLiteDatabase db;

    public DBHelper() {
    }

    //单例引用
    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化greenDao
     *
     * @param context
     * @param name    数据表的名称
     * @return
     */
    public DaoSession initDatabase(Context context, String name) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 不需要去编写「CREATE TABLE」SQL语句，greenDAO 已经帮你完成。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        helper = new DaoMaster.DevOpenHelper(context, name, null);
        db = helper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        return daoSession;
    }
}
