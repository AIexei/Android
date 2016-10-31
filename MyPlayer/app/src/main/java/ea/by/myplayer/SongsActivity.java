package ea.by.myplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ea.by.myplayer.adapter.MyAdapter;
import ea.by.myplayer.db.DBHelper;
import ea.by.myplayer.model.ListItem;

public class SongsActivity extends AppCompatActivity {

    final int MENU_DELETE = 12;
    final int DIALOG = 1;

    DBHelper dbHelper;
    int categoryID;

    ArrayList<ListItem> songs = new ArrayList<ListItem>();
    MyAdapter adapter;

    RelativeLayout layout;
    EditText dialogName;
    EditText dialogImage;

    int imagesID[] = new int[]{R.drawable.i0, R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4,
                               R.drawable.i5, R.drawable.i6, R.drawable.i7, R.drawable.i8, R.drawable.i9};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs);

        dbHelper = new DBHelper(this);

        Intent intent = getIntent();
        TextView categoryName = (TextView) findViewById(R.id.textView);
        categoryName.setText(intent.getStringExtra("categoryName"));

        fillData();
        adapter = new MyAdapter(this, songs);

        final ListView songsView = (ListView) findViewById(R.id.SongsView);
        registerForContextMenu(songsView);
        songsView.setAdapter(adapter);

        songsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ////////////////////////////////////////////
                ///////////////////////////////////////////
                ////////////////////////////////////////////
            }
        });


    }


    void fillData() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String[] columns = {"id"};
        String[] selectionArgs = {getIntent().getStringExtra("categoryName")};
        Cursor cursor = database.query("Categories", columns, "category_name = ?",
                selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            categoryID = cursor.getInt(idIndex);
        }

        columns = new String[]{"song_name", "song_image"};
        selectionArgs = new String[]{Integer.toString(categoryID)};
        cursor = database.query("Songs", columns, "category_id = ?",
                selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("song_name");
            int imageIndex = cursor.getColumnIndex("song_image");

            do {
                songs.add(new ListItem(imagesID[cursor.getInt(imageIndex)], cursor.getString(nameIndex)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.song_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemAddSong) {
            showDialog(DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_song, null);
        adb.setView(layout);

        dialogImage = (EditText) layout.findViewById(R.id.imageEdit1);
        dialogName = (EditText) layout.findViewById(R.id.songEdit);

        adb.setPositiveButton("Add", myClickListener);
        adb.setNeutralButton("Cancel", myClickListener);

        return adb.create();
    }


    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    String songName = dialogName.getText().toString();
                    String image = dialogImage.getText().toString();

                    //adding to database
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();

                    cv.put("song_image", Integer.parseInt(image));
                    cv.put("song_name", songName);
                    cv.put("category_id", categoryID);

                    database.insert("Songs", null, cv);
                    dbHelper.close();

                    // adding to list
                    songs.add(new ListItem(imagesID[Integer.parseInt(image)], songName));
                    adapter.notifyDataSetChanged();

                    dialogImage.setText("");
                    dialogName.setText("");
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, MENU_DELETE, 0, "Delete");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_DELETE) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            ListItem song = songs.get(info.position);

            SQLiteDatabase database = dbHelper.getWritableDatabase();

            database.delete("Songs", "song_name = ?", new String[]{song.name});
            songs.remove(info.position);

            adapter.notifyDataSetChanged();
            dbHelper.close();
        }

        return super.onContextItemSelected(item);
    }
}
