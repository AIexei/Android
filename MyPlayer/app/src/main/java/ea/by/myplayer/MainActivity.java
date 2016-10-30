package ea.by.myplayer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ea.by.myplayer.adapter.MyAdapter;
import ea.by.myplayer.model.ListItem;

public class MainActivity extends AppCompatActivity {

    ArrayList<ListItem> categories = new ArrayList<ListItem>();
    MyAdapter adapter;

    final int DIALOG = 1;
    RelativeLayout layout;
    EditText dialogName;
    EditText dialogImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fillData();
        adapter = new MyAdapter(this, categories);

        final ListView categoriesView = (ListView) findViewById(R.id.categoryView);
        categoriesView.setAdapter(adapter);

        categoriesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItem li = (ListItem) (categoriesView.getAdapter().getItem(position));

                Intent intent = new Intent(MainActivity.this, SongsActivity.class);
                intent.putExtra("categoryName", li.name);
                startActivity(intent);
            }
        });

    }


    void fillData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

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


    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    String categoryName = dialogName.getText().toString();
                    String image = dialogImage.getText().toString();

                    categories.add(new ListItem(Integer.parseInt(image), categoryName));
                    adapter.notifyDataSetChanged();

                    dialogImage.setText("");
                    dialogName.setText("");
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };
}
