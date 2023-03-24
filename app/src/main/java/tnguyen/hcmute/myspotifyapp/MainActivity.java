package tnguyen.hcmute.myspotifyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class MainActivity extends AppCompatActivity {

    private ImageView btnPlayOrPause;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imgGlide = findViewById(R.id.imgSongAvt);
        Glide.with(this)
                .load(R.drawable.dontcoiavt)
                .apply(new RequestOptions().transform(new CenterCrop()).transform(new RoundedCorners(24)))
                .into(imgGlide);

        ConstraintLayout constraintLayout = findViewById(R.id.main_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        //Handle load song

        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });

        btnPlayOrPause = findViewById(R.id.imgPlayOrPause);

//        if(!isPlaying)
//        {
        btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartService();
            }
        });
//        }
//        else {
//            btnPlayOrPause.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    clickStopService();
//                }
//            });
//        }
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, home_activity.class);
        startActivity(intent);
    }

    private void clickStartService() {
        Song song = new Song("Có em bên đời bổng vui", "Chillies", R.drawable.dontcoiavt, R.raw.filemusic);
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        intent.putExtras(bundle);

        startService(intent);

    }

    private void clickStopService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);

    }


}