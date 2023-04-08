package tnguyen.hcmute.myspotifyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPlayOrPause;
    private boolean isPlaying;
    private boolean isNewSong = true;
    private SeekBar seekbarSong;
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

    private MyService mService;
    private boolean mIsBound = false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyBinder myBinder = (MyService.MyBinder) iBinder;
            mService = myBinder.getService();
            mIsBound = true;
            mService.setSeekBar(seekbarSong);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsBound = false;
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

        tvTitleMain = findViewById(R.id.tvTitleMain);
        tvSingleMain = findViewById(R.id.tvSingleMain);

        seekbarSong = findViewById(R.id.seekBar);
        seekbarSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mIsBound) {
                    mService.onSeekBarProgressChanged();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mIsBound) {
                    mService.onSeekBarStartTrackingTouch();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mIsBound) {
                    mService.onSeekBarStopTrackingTouch();
                }
            }
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký Broadcast Receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("MEDIA_PLAYER_SEEK_TO");
        filter.addAction("MEDIA_PLAYER_DURATION");
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký Broadcast Receiver
        unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Xử lý các thông báo từ Service
            if (intent.getAction().equals("MEDIA_PLAYER_SEEK_TO")) {
                int seekToPosition = intent.getIntExtra("seek_to_position", 0);
                // Di chuyển SeekBar đến vị trí mới
                seekbarSong.setProgress(seekToPosition);
                TextView tvtimeform = findViewById(R.id.time_from);
                tvtimeform.setText(formattedTime(seekToPosition));
            } else if (intent.getAction().equals("MEDIA_PLAYER_DURATION")) {
                int duration = intent.getIntExtra("duration", 0);
                // Cập nhật chiều dài của SeekBar
                seekbarSong.setMax(duration);

                TextView tvtimeTo = findViewById(R.id.time_to);
                tvtimeTo.setText(formattedTime(duration));

            }
        }
    };

    private String formattedTime(int duration){
        String durationString = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(duration) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
        return durationString;
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