package ea.by.myplayer;

import android.app.Activity;
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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ea.by.myplayer.adapter.MyAdapter;
import ea.by.myplayer.model.ListItem;

public class SongsActivity extends AppCompatActivity {

    ArrayList<ListItem> songs = new ArrayList<ListItem>();
    MyAdapter adapter;

    final int DIALOG = 1;
    RelativeLayout layout;
    EditText dialogName;
    EditText dialogImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.songs);

        Intent intent = getIntent();
        TextView categoryName = (TextView) findViewById(R.id.textView);
        categoryName.setText(intent.getStringExtra("categoryName"));

        fillData();
        adapter = new MyAdapter(this, songs);

        final ListView songsView = (ListView) findViewById(R.id.SongsView);
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

                    songs.add(new ListItem(Integer.parseInt(image), songName));
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
