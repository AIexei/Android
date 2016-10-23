package ea.by.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GridLayout gridLayout;

    EditText nameText;
    EditText emailText;
    Button unsubsButton;
    Button subsButton;
    TextView msgView;

    HashMap<String, String> data;
    LinkedList<CheckBox> followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        gridLayout = (GridLayout) findViewById(R.id.gridLayout2);

        nameText = (EditText) findViewById(R.id.editText1);
        emailText = (EditText) findViewById(R.id.editText2);
        subsButton = (Button) findViewById(R.id.subsButton);
        unsubsButton = (Button) findViewById(R.id.unsubsButton);
        msgView = (TextView) findViewById(R.id.msgView);

        data = new HashMap<String, String>();
        followers = new LinkedList<CheckBox>();

        subsButton.setOnClickListener(this);
        unsubsButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemDel) {
            gridLayout.removeAllViews();
            followers.clear();
            data.clear();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subsButton:
                String name = nameText.getText().toString();
                String email = emailText.getText().toString();

                if (isEmpty(name) || isEmpty(email)) {
                    msgView.setText("Field is empty");
                } else {
                    if (!data.keySet().contains(email)) {
                        data.put(email, name);
                        msgView.setText("");

                        emailText.setText(null);
                        nameText.setText(null);

                        toastNotification("You are subscribed!");

                        CheckBox checkBox = new CheckBox(this);
                        checkBox.setText(email);
                        checkBox.setChecked(false);

                        followers.add(checkBox);

                        gridLayout.addView(checkBox);
                    } else {
                        msgView.setText("User with this email is subscribed");
                    }
                }

                break;
            case R.id.unsubsButton:
                ArrayList<Integer> index = new ArrayList<Integer>();

                for (int i = 0; i < followers.size(); i++) {
                    if (followers.get(i).isChecked()) {
                        index.add(i);
                    }
                }

                for (int i = 0; i < index.size(); i++) {
                    String mail = followers.get(index.get(i)).getText().toString();
                    data.remove(mail);

                    gridLayout.removeView(followers.get(index.get(i)));
                    followers.remove(index.get(i));
                }
                break;
        }
    }

    private void toastNotification(String msg) {
        Toast toast = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
        toast.show();
    }

    private boolean isEmpty(String temp) {
        if (temp.length() == 0)
            return true;

        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) != ' ')
                return false;
        }

        return true;
    }
}
