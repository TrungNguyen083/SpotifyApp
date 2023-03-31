package tnguyen.hcmute.myspotifyapp;


import static tnguyen.hcmute.myspotifyapp.MySpotifyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class MyService extends Service {

    public static final int ACTION_PAUSE = 1;
    public static final int ACTION_RESUME = 2;
    public static final int ACTION_PREVIOUS = 3;
    public static final int ACTION_NEXT = 4;
    public static final int ACTION_START = 5;


    private MediaPlayer mediaPlayer;
    private boolean isPlaying;
    private Song msong;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("TrungNguyen","Myservice onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        Bundle bundle = intent.getExtras();
        if(bundle!=null)
        {
            Song song = (Song) bundle.get("object_song");

            if(song != null)
            {
                msong = song;
                isPlaying = true;
                startMusic(song);
                sendNotification(song);
            }
        }

        int actionMusic = intent.getIntExtra("action_music_receiver", 0);
        handleActionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    private void startMusic(Song song) {
        if(mediaPlayer == null)
        {
            mediaPlayer = mediaPlayer.create(getApplicationContext(), song.getResource());
        }
        mediaPlayer.start();
        isPlaying = true;
        sendActionToActivity(ACTION_START);
    }

    private void handleActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_PREVIOUS:
                sendActionToActivity(ACTION_PREVIOUS);
                break;
            case ACTION_NEXT:
                sendActionToActivity(ACTION_NEXT);
        }
    }

    private void pauseMusic() {
        if(mediaPlayer != null && isPlaying){
            mediaPlayer.pause();
            isPlaying = false;
            sendNotification(msong);
            sendActionToActivity(ACTION_PAUSE);
        }
    }

    private void resumeMusic() {
        if(mediaPlayer != null && !isPlaying)
        {
            mediaPlayer.start();
            isPlaying = true;
            sendNotification(msong);
            sendActionToActivity(ACTION_RESUME);
        }
    }

    private void sendNotification(Song song)
    {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), song.getImage());

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.tv_titleSong, song.getTitle());
        remoteViews.setTextViewText(R.id.tv_singleSong, song.getSingle());
        remoteViews.setImageViewBitmap(R.id.img_song,bitmap);

        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.outline_play_circle_outline_white_36);
        if(isPlaying) {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.pausepx);
        } else {
            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.play_circle48px);
        }

        remoteViews.setOnClickPendingIntent(R.id.img_previous, getPendingIntent(this, ACTION_PREVIOUS));
        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(this, ACTION_NEXT));


        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();
        startForeground(1,notification);

    }

    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("action_music", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("TrungNguyen","MyService onDestroy");
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void sendActionToActivity(int action)
    {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", msong);
        bundle.putBoolean("status_player", isPlaying);
        bundle.putInt("action_music", action);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}