package ea.by.myplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Алексей on 30.10.2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DBVersion = 1;

    public DBHelper(Context context) {
        super(context, "myDB", null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Categories ("
                + "id integer primary key autoincrement,"
                + "category_name text not null,"
                + "category_image integer not null" + ");");

        db.execSQL("create table Songs ("
                + "song_name text not null,"
                + "song_image integer not null,"
                + "category_id integer not null,"
                + "foreign key (category_id) references Categories(id)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table if exists Categories");
        //db.execSQL("drop talbe if exists Sons");
        //onCreate(db);
    }
}
