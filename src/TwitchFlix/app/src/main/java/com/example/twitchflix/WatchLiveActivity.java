package com.example.twitchflix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.twitchflix.account.AccountActivity;
import com.example.twitchflix.network.Client;
import com.example.twitchflix.network.StreamData;
import com.example.twitchflix.network.WebInterface;
import com.pedro.vlc.VlcListener;
import com.pedro.vlc.VlcVideoLibrary;
import java.util.Arrays;
import org.videolan.libvlc.MediaPlayer;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// activity that uses a VLC player to watch live stream through HLS
public class WatchLiveActivity extends AppCompatActivity implements VlcListener, View.OnClickListener {
    private VlcVideoLibrary vlcVideoLibrary;
    private Button bStartStop;
    private String endPoint = "";

    private String[] options = new String[]{":fullscreen"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // building the player
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_watch_live);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        bStartStop = (Button) findViewById(R.id.b_start_stop);
        bStartStop.setOnClickListener(this);
        vlcVideoLibrary = new VlcVideoLibrary(this, this, surfaceView);
        vlcVideoLibrary.setOptions(Arrays.asList(options));

        // get stream id passed as a parameter
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String streamId = extras.getString("id");

            WebInterface service = Client.getClient().create(WebInterface.class);
            Call<StreamData> call = service.getStream(streamId);
            call.enqueue(new Callback<StreamData>() {
                @Override
                public void onResponse(Call<StreamData> call, Response<StreamData> response) {
                    if(response.body() != null) {
                        Log.d("TAG", Client.getBaseUrl() + response.body().getMessage());
                        endPoint = Client.getBaseUrl() + response.body().getMessage();
                    }
                }

                @Override
                public void onFailure(Call<StreamData> call, Throwable t) {
                    Log.d("TAG2",t.toString());
                }
            });
        }
    }

    @Override
    public void onComplete() {
        Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error, make sure your endpoint is correct", Toast.LENGTH_SHORT).show();
        vlcVideoLibrary.stop();
        bStartStop.setText("Start");
    }


    // method that starts or stops stream playback
    @Override
    public void onClick(View view) {
        if (!vlcVideoLibrary.isPlaying()) {
            vlcVideoLibrary.play(endPoint);
            bStartStop.setText("Stop");
        } else {
            vlcVideoLibrary.stop();
            bStartStop.setText("Start");
        }
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
