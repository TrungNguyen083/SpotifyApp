package tnguyen.hcmute.myspotifyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPlayOrPause;
    private boolean isPlaying;
    private boolean isNewSong = true;
    private Song song;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }
            song = (Song) bundle.get("object_song");
            isPlaying = bundle.getBoolean("status_player");
            int actionMusic = bundle.getInt("action_music");

            handleLayoutBottomMusic(actionMusic);
        }
    };

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Handle load song
        Intent intent = getIntent();
        int id = intent.getIntExtra("songID", 0);
        DBHandler dbHandler = new DBHandler(MainActivity.this);
        song = dbHandler.showSongByID(id);

        TextView tvTitleMain, tvSingleMain;
        ImageView imgSongMain;

        tvTitleMain = findViewById(R.id.tvTitleMain);
        tvSingleMain = findViewById(R.id.tvSingleMain);
        imgSongMain = (ImageView) findViewById(R.id.imgSongAvt);

        // Lấy tên của hình ảnh từ ID
        String imageName = song.getImage();
        Resources resources = getApplicationContext().getResources();
        int imageResourceID = resources.getIdentifier(imageName, "drawable", getApplicationContext().getPackageName());


        tvTitleMain.setText(song.getTitle());
        tvSingleMain.setText(song.getSingle());

        ImageView imgGlide = findViewById(R.id.imgSongAvt);
        Glide.with(this)
                .load(imageResourceID)
                .apply(new RequestOptions().transform(new CenterCrop()).transform(new RoundedCorners(24)))
                .into(imgGlide);

        ConstraintLayout constraintLayout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();


        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));

        btnPlayOrPause = findViewById(R.id.imgPlayOrPause);

        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartSong(song);
                btnPlayOrPause.setImageResource(R.drawable.outline_pause_circle_white_48);
            }
        });
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, home_activity.class);
        startActivity(intent);
    }

    private void clickStartSong(Song song) {
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        intent.putExtras(bundle);
        startService(intent);
    }


    private void handleLayoutBottomMusic(int action) {
        switch (action) {
            case MyService.ACTION_PAUSE:
                setBtnPlayOrPause();
                break;
            case MyService.ACTION_RESUME:
                btnPlayOrPause.setImageResource(R.drawable.outline_pause_circle_white_48);
                break;
            case MyService.ACTION_PREVIOUS:
                //layoutBottom.setVisibility(View.GONE);
                break;
            case MyService.ACTION_NEXT:
                //layoutBottom.setVisibility(View.GONE);
                break;
            case MyService.ACTION_START:
                //layoutBottom.setVisibility(View.VISIBLE);
                showInforSong();
                setBtnPlayOrPause();
                break;
        }
    }

    private void showInforSong() {
        if (song == null) {
            return;
        }
        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    sendActionToService(MyService.ACTION_PAUSE);
                } else {
                    sendActionToService(MyService.ACTION_RESUME);
                }
            }
        });
    }

    private void setBtnPlayOrPause() {
        if (isPlaying) {
            btnPlayOrPause.setImageResource(R.drawable.outline_pause_circle_white_48);
        } else {
            btnPlayOrPause.setImageResource(R.drawable.outline_play_circle_outline_white_48);
        }
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("action_music_receiver", action);
        startService(intent);
    }
}