package ea.by.myplayer;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Locale;

import ea.by.myplayer.adapter.MyAdapter;
import ea.by.myplayer.db.DBHelper;
import ea.by.myplayer.model.ListItem;

public class MainActivity extends AppCompatActivity {

    final int DIALOG = 1;
    final int MENU_DELETE = 11;

    DBHelper dbHelper;
    ArrayList<ListItem> categories = new ArrayList<ListItem>();
    MyAdapter adapter;

    RelativeLayout layout;
    EditText dialogName;
    EditText dialogImage;

    Button logButton;

    int imagesID[] = new int[]{R.drawable.i0, R.drawable.i1, R.drawable.i2, R.drawable.i3, R.drawable.i4,
                               R.drawable.i5, R.drawable.i6, R.drawable.i7, R.drawable.i8, R.drawable.i9};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        dbHelper = new DBHelper(this);

        fillData();
        adapter = new MyAdapter(this, categories);

        final ListView categoriesView = (ListView) findViewById(R.id.categoryView);
        registerForContextMenu(categoriesView);
        categoriesView.setAdapter(adapter);

        // processing changed category
        categoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem li = (ListItem) (categoriesView.getAdapter().getItem(position));

                Intent intent = new Intent(MainActivity.this, SongsActivity.class);
                intent.putExtra("categoryName", li.name);
                startActivity(intent);
            }
        });

        logButton = (Button) findViewById(R.id.logButton);
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                Log.d("1", "Categories");

                Cursor cursor = database.query("Categories", null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("category_name");
                    int imageIndex = cursor.getColumnIndex("category_image");

                    do {
                        String msg = "ID = " + cursor.getInt(idIndex) + " | name = " + cursor.getString(nameIndex) + " | image = "+ cursor.getInt(imageIndex);
                        Log.d("1", msg);
                    } while (cursor.moveToNext());
                }



                cursor = database.query("Songs", null, null, null, null, null, null);
                Log.d("1", "Songs");

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex("category_id");
                    int nameIndex = cursor.getColumnIndex("song_name");
                    int imageIndex = cursor.getColumnIndex("song_image");

                    do {
                        String msg = "category = " + cursor.getInt(idIndex) + " | name = " + cursor.getString(nameIndex) + " | image = "+ cursor.getInt(imageIndex);
                        Log.d("1", msg);
                    } while (cursor.moveToNext());
                }
            }
        });
    }


    // getting from database
    void fillData() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query("Categories", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int imageIndex = cursor.getColumnIndex("category_image");
            int nameIndex = cursor.getColumnIndex("category_name");

            do {
                categories.add(new ListItem(imagesID[cursor.getInt(imageIndex)], cursor.getString(nameIndex)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    // menu change
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemAddCat) {
            showDialog(DIALOG);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

        layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.dialog_cat, null);
        adb.setView(layout);

        dialogImage = (EditText) layout.findViewById(R.id.imageEdit);
        dialogName = (EditText) layout.findViewById(R.id.categoryEdit);

        adb.setPositiveButton("Add", myClickListener);
        adb.setNeutralButton("Cancel", myClickListener);

        return adb.create();
    }

    // adding new category
    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    String categoryName = dialogName.getText().toString();
                    String image = dialogImage.getText().toString();

                    //adding to database
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();

                    cv.put("category_image", Integer.parseInt(image));
                    cv.put("category_name", categoryName);

                    database.insert("Categories", null, cv);
                    dbHelper.close();

                    //adding to list
                    categories.add(new ListItem(imagesID[Integer.parseInt(image)], categoryName));
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
            ListItem category = categories.get(info.position);

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            int categoryID=0;

            String[] columns = {"id"};
            String[] selectionArgs = {category.name};
            Cursor cursor = database.query("Categories", columns, "category_name = ?",
                    selectionArgs, null, null, null);

            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                categoryID = cursor.getInt(idIndex);
            }

            database.delete("Songs", "category_id = ?", new String[]{Integer.toString(categoryID)});
            database.delete("Categories", "category_name = ?", new String[]{category.name});
            categories.remove(info.position);

            adapter.notifyDataSetChanged();
            dbHelper.close();
        }

        return super.onContextItemSelected(item);
    }
}
