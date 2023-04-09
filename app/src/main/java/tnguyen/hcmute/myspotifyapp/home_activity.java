package tnguyen.hcmute.myspotifyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class home_activity extends AppCompatActivity {

    MusicListViewAdapter musicListViewAdapter;
    ListView listViewSong;

    private ArrayList<Song> songArrayList;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initializing our all variables.
        songArrayList = new ArrayList<>();
        dbHandler = new DBHandler(home_activity.this);
        songArrayList = dbHandler.readSongs();
        listViewSong = findViewById(R.id.List0fSong);
        musicListViewAdapter = new MusicListViewAdapter(songArrayList, this);
        int listSongsize = musicListViewAdapter.getCount();
        // setting our adapter to recycler view.
        listViewSong.setAdapter(musicListViewAdapter);
        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song) musicListViewAdapter.getItem(position);
                //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)
                Intent intent = new Intent(home_activity.this, MainActivity.class);
                intent.putExtra("songID", song.getId());
                intent.putExtra("listSongsize", listSongsize);
                home_activity.this.startActivity(intent);
            }
        });

    }
}
