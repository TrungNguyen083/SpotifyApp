package tnguyen.hcmute.myspotifyapp;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicListViewAdapter extends BaseAdapter
{

    final ArrayList<Song> listSong;

    MusicListViewAdapter(ArrayList<Song> listsong) {
        this.listSong = listsong;
    }
    @Override
    public int getCount() {
        return listSong.size();
    }

    @Override
    public Object getItem(int i) {
        return listSong.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewSong;
        if (view == null) {
            viewSong = View.inflate(viewGroup.getContext(), R.layout.custorm_music_items, null);
        } else viewSong = view;

        //Bind sữ liệu phần tử vào View
        Song song = (Song) getItem(i);
        ((ImageView) viewSong.findViewById(R.id.img_song_item)).setImageResource(song.getImage());
        ((TextView) viewSong.findViewById(R.id.tv_titleSong_item)).setText(String.format(song.getTitle()));
        ((TextView) viewSong.findViewById(R.id.tv_singleSong_item)).setText(String.format(song.getSingle()));


        return viewSong;
    }
}
