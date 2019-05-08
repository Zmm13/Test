package com.example.administrator.test.daoJavaBean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SONG".
*/
public class SongDao extends AbstractDao<Song, Long> {

    public static final String TABLENAME = "SONG";

    /**
     * Properties of entity Song.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Key = new Property(1, long.class, "key", false, "KEY");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Singer = new Property(3, String.class, "singer", false, "SINGER");
        public final static Property Size = new Property(4, long.class, "size", false, "SIZE");
        public final static Property Duration = new Property(5, int.class, "duration", false, "DURATION");
        public final static Property Path = new Property(6, String.class, "path", false, "PATH");
        public final static Property AlbumId = new Property(7, long.class, "albumId", false, "ALBUMID");
    }


    public SongDao(DaoConfig config) {
        super(config);
    }
    
    public SongDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SONG\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"KEY\" INTEGER NOT NULL ," + // 1: key
                "\"NAME\" TEXT," + // 2: name
                "\"SINGER\" TEXT," + // 3: singer
                "\"SIZE\" INTEGER NOT NULL ," + // 4: size
                "\"DURATION\" INTEGER NOT NULL ," + // 5: duration
                "\"PATH\" TEXT," + // 6: path
                "\"ALBUMID\" INTEGER NOT NULL );"); // 7: albumId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SONG\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Song entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getKey());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String singer = entity.getSinger();
        if (singer != null) {
            stmt.bindString(4, singer);
        }
        stmt.bindLong(5, entity.getSize());
        stmt.bindLong(6, entity.getDuration());
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(7, path);
        }
        stmt.bindLong(8, entity.getAlbumId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Song entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getKey());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String singer = entity.getSinger();
        if (singer != null) {
            stmt.bindString(4, singer);
        }
        stmt.bindLong(5, entity.getSize());
        stmt.bindLong(6, entity.getDuration());
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(7, path);
        }
        stmt.bindLong(8, entity.getAlbumId());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Song readEntity(Cursor cursor, int offset) {
        Song entity = new Song( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // key
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // singer
            cursor.getLong(offset + 4), // size
            cursor.getInt(offset + 5), // duration
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // path
            cursor.getLong(offset + 7) // albumId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Song entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setKey(cursor.getLong(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSinger(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setSize(cursor.getLong(offset + 4));
        entity.setDuration(cursor.getInt(offset + 5));
        entity.setPath(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAlbumId(cursor.getLong(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Song entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Song entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Song entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
