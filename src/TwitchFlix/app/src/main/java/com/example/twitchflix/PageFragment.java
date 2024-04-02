package com.example.twitchflix;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.example.twitchflix.network.Client;
import com.example.twitchflix.network.StreamData;
import com.example.twitchflix.network.VideoData;
import com.example.twitchflix.network.WebInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// universal fragment class that supports both live streams and VODs
public class PageFragment extends Fragment {

    private GridView liveGrid;
    private boolean isLive;

    public PageFragment(boolean isLive){
        this.isLive = isLive;
    }

    @Override
    public void onStart() {
        super.onStart();

        WebInterface service = Client.getClient().create(WebInterface.class);

        // check if this fragment is meant for live streams of VODs
        if(isLive) {
            Call<List<StreamData>> call = service.getStreamCatalog();

            call.enqueue(new Callback<List<StreamData>>() {
                @Override
                public void onResponse(Call<List<StreamData>> call, Response<List<StreamData>> response) {
                    if (response.body() != null) {
                        ArrayList<StreamData> catalog = new ArrayList<>(response.body());
                        generateCatalog(catalog);
                    }
                }

                @Override
                public void onFailure(Call<List<StreamData>> call, Throwable t) {
                    Log.d("FRAGMENT", t.toString());
                }
            });
        } else {
            Call<List<VideoData>> call = service.getVideoCatalog();

            call.enqueue(new Callback<List<VideoData>>() {
                @Override
                public void onResponse(Call<List<VideoData>> call, Response<List<VideoData>> response) {
                    if(response.body() != null) {
                        ArrayList<VideoData> catalog = new ArrayList<>(response.body());
                        generateCatalog(catalog);
                    }
                }

                @Override
                public void onFailure(Call<List<VideoData>> call, Throwable t) {
                    Log.d("FRAGMENT",t.toString());
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(R.layout.catalog_grid, container, false);
    }

    // generic method that fills the fragment's grid with either live stream or VOD catalog
    private <T extends Data> void generateCatalog(ArrayList<T> catalog) {
        liveGrid = (GridView) getView();
        GridAdapter adapter = new GridAdapter<T>(getActivity(), isLive, catalog);
        liveGrid.setAdapter(adapter);
    }

}