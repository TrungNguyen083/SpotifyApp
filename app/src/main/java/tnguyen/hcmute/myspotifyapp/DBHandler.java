package tnguyen.hcmute.myspotifyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "MusicPlayer";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "song";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String SONG_TITLE = "songtitle";

    // below variable id for our course duration column.
    private static final String SONG_SINGLE = "songsingle";

    // below variable for our course description column.
    private static final String SONG_RESOURCE = "songresource";

    // below variable is for our course tracks column.
    private static final String SONG_DURATION = "songduration";

    private static final String SONG_IMG = "songimg";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SONG_TITLE + " TEXT,"
                + SONG_SINGLE + " TEXT,"
                + SONG_RESOURCE + " INTEGER,"
                + SONG_DURATION + " TEXT,"
                + SONG_IMG + " INTEGER)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewCourse(String songtitle, String songsingle, int songresource, String songduration, int songimg) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(SONG_TITLE, songtitle);
        values.put(SONG_SINGLE, songsingle);
        values.put(SONG_RESOURCE, songresource);
        values.put(SONG_DURATION, songduration);
        values.put(SONG_IMG, songimg);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // we have created a new method for reading all the courses.
    public ArrayList<Song> readSongs() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorSong
                = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Song> songArrayList
                = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorSong.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                Song song = new Song();
                song.setId(cursorSong.getInt(0));
                song.setTitle(cursorSong.getString(1));
                song.setSingle(cursorSong.getString(2));
                song.setResource(cursorSong.getString(3));
                song.setDuration(cursorSong.getString(4));
                song.setImage(cursorSong.getString(5));
                songArrayList.add(song);
            } while (cursorSong.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorSong.close();
        return songArrayList;
    }

    public Song showSongByID(int id) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to
        // read data from database.
        Cursor cursorSong
                = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = " + id, null);

        Song song = new Song();
        // moving our cursor to first position.
        if (cursorSong.moveToFirst()) {

            song.setId(cursorSong.getInt(0));
            song.setTitle(cursorSong.getString(1));
            song.setSingle(cursorSong.getString(2));
            song.setResource(cursorSong.getString(3));
            song.setDuration(cursorSong.getString(4));
            song.setImage(cursorSong.getString(5));
        }
        // at last closing our cursor
        // and returning our array list.
        cursorSong.close();
        return song;
    }

}