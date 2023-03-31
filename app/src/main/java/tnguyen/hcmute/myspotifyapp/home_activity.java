package tnguyen.hcmute.myspotifyapp;

import androidx.appcompat.app.AppCompatActivity;

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

    ArrayList<Song> listSong; //Mảng dữ liệu sản phẩm
    MusicListViewAdapter musicListViewAdapter;
    ListView listViewSong;

    private int bitmapImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ArrayList<Song> listSongTest = DAOSong.getAllSong();
        //Khoi tao ListProduct, tự sinh một số dữ liệu
        listSong = new ArrayList<>();
        listSong.add(new Song("Don't Côi", "Orijjin", R.drawable.dontcoiavt, R.raw.filemusic));
        listSong.add(new Song("Bước Qua Nhau", "Vũ", R.drawable.buocquamuacodonavt, R.raw.filemusic));
        listSong.add(new Song("Bài Này Chill Phết", "Đen", R.drawable.bainaychillphet, R.raw.filemusic));
        listSong.add(new Song("Tình Yêu Chậm Trễ", "MONSTAR", R.drawable.tinhyeuchamtre, R.raw.filemusic));
        listSong.add(new Song("Sinh Ra Đã Là Thứ Đối Lập Nhau", "Chillies", R.drawable.sinhradalathudoilapnhau, R.raw.filemusic));
        listSong.add(new Song("Có Ai Thương Em Như Anh", "Tóc Tiên", R.drawable.coaithuongemnhuanh, R.raw.filemusic));
        listSong.add(new Song("Rồi Ta Sẽ Ngắm Pháo Hoa Cùng Nhau", "Chillies", R.drawable.roitasengamphaohoacungnhau, R.raw.filemusic));
        listSong.add(new Song("Anh Đã Ổn Hơn", "MCK", R.drawable.anhdaonhon, R.raw.filemusic));

        musicListViewAdapter = new MusicListViewAdapter(listSongTest);

        listViewSong = findViewById(R.id.List0fSong);
        listViewSong.setAdapter(musicListViewAdapter);

        listViewSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song song = (Song) musicListViewAdapter.getItem(position);
                //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)
                Intent intent = new Intent(home_activity.this, MainActivity.class);
                intent.putExtra("index", position);
                home_activity.this.startActivity(intent);
            }
        });
    }
}
