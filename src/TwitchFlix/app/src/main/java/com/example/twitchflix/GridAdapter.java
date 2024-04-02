package com.example.twitchflix;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.twitchflix.network.Client;
import com.example.twitchflix.network.StreamData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


// generic adapter for filling catalog grid
public class GridAdapter <T extends Data> extends ArrayAdapter<T> {

    private Context c;
    private boolean isLive;

    public GridAdapter(@NonNull Context context, boolean isLive, ArrayList<T> catalog) {
        super(context, 0, catalog);

        this.c = context;
        this.isLive = isLive;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_card, parent, false);
        }

        T data = getItem(position);

        TextView title = listitemView.findViewById(R.id.title);
        title.setText(data.getTitle());

        ImageView img = listitemView.findViewById(R.id.img);
        Picasso.get().load(Client.getBaseUrl() + data.getImg()).into(img);

        String id = data.getId();
        img.setOnClickListener(new MyOnClickListener(id));

        return listitemView;
    }

    // custom click listener that sends either live stream or VOD id to the corresponding player activity
    class MyOnClickListener implements View.OnClickListener {
        private String id;

        MyOnClickListener(String id){
            this.id = id;
        }

        @Override
        public void onClick(View v){
            if(isLive) {
                Intent intent = new Intent(c, WatchLiveActivity.class);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            } else {
                Intent intent = new Intent(c, WatchVODActivity.class);
                intent.putExtra("id", id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }
        }
    }

}
