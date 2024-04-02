package com.example.twitchflix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.twitchflix.account.AccountActivity;
import com.example.twitchflix.network.Client;
import com.example.twitchflix.network.VideoData;
import com.example.twitchflix.network.WebInterface;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// activity that contains a VOD player
public class WatchVODActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_vod);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mVideoView = findViewById(R.id.videoview);
        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(controller);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String videoId = extras.getString("id");
            String format = getAvailableFormat();

            if(format != null) {
                WebInterface service = Client.getClient().create(WebInterface.class);
                Call<VideoData> call = service.getVideo(videoId, format);
                call.enqueue(new Callback<VideoData>() {
                    @Override
                    public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                        if (response.body() != null) {
                            if (response.body().getMessage() != null) {
                                Uri uri = Uri.parse(Client.getBaseUrl() + response.body().getMessage());
                                mVideoView.setVideoURI(uri);
                                mVideoView.requestFocus();
                                mVideoView.start();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoData> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Couldn't find available video format", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        releasePlayer();
    }

    private String getAvailableFormat() {
        int numCodecs = MediaCodecList.getCodecCount();

        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            List<String> types = Arrays.asList(codecInfo.getSupportedTypes());

            if(types.contains("video/mp4") || types.contains("video/mp4v-es") ) {
                return "mp";
            } else if(types.contains("video/ogg")) {
                return "ogv";
            } else if(types.contains("video/x-msvideo") || types.contains("video/msvideo")  || types.contains("video/avi")) {
                return "avi";
            } else if(types.contains("video/mpeg")) {
                return "mpg";
            }
        }

        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        releasePlayer();
    }

    private void releasePlayer() {
        mVideoView.stopPlayback();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_account) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
