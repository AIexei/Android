package ea.by.myplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ea.by.myplayer.R;
import ea.by.myplayer.model.ListItem;

/**
 * Created by Алексей on 30.10.2016.
 */

public class MyAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ListItem> objects;

    public MyAdapter(Context context, ArrayList<ListItem> objects) {
        this.ctx = context;
        this.objects = objects;

        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.my_list_item, parent, false);
        }

        ListItem  item = getMyListItem(position);

        ((TextView) view.findViewById(R.id.name)).setText(item.name);
        ((ImageView) view.findViewById(R.id.image)).setImageResource(item.imageID);

        return view;
    }

    ListItem getMyListItem(int position) {
        return ((ListItem) getItem(position));
    }
}
