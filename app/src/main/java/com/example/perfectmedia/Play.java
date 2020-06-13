package com.example.perfectmedia;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class Play extends AppCompatActivity {
    static MediaPlayer media;
    ArrayList<File> Listsongs;
    int i;
    TextView t1, t2;
    Button play, previous, next;
    ImageView imageView;
    SeekBar sk;
    Thread thr;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        t1 = findViewById(R.id.Songtitle);
        t2 = findViewById(R.id.textView);
        sk  = findViewById(R.id.seekBar);
        sk.getProgressDrawable().setColorFilter(Color.parseColor("#FDFCFD"), PorterDuff.Mode.MULTIPLY);
        sk.getThumb().setColorFilter(Color.parseColor("#FDFCFD") ,PorterDuff.Mode.SRC_IN);

        imageView = findViewById(R.id.imageView);
        previous = findViewById(R.id.button);
        play = findViewById(R.id.button2);
        next = findViewById(R.id.button3);
        thr = new Thread(){
            @Override
            public void run() {
                 int i = 0;
                 while(i< media.getDuration()){
                     try{
                         sleep(500);
                         i=media.getCurrentPosition();
                         sk.setProgress(i);
                     }
                     catch(InterruptedException e){
                         e.printStackTrace();
                     }
                 }
            }
        };
        if (media != null) {
            media.stop();
            media.release();
        }
        Bundle bundle = getIntent().getExtras();
        Listsongs = (ArrayList) bundle.getParcelableArrayList("Listofsongs");
        i = bundle.getInt("Position");
        final Uri uri = Uri.parse(Listsongs.get(i).toString());
        t1.setText(bundle.getString("Song_name"));
        media = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(String.valueOf(uri))));
        media.start();
        sk.setMax(media.getDuration());
        thr.start();
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                media.seekTo(sk.getProgress());
            }
        });
        play.setBackgroundResource(R.drawable.pause);
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                media.stop();
                media.release();
                if (i > 0) {
                    i = i - 1;
                    Uri u = Uri.parse(Listsongs.get(i).toString());
                    media = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(String.valueOf(u))));
                    t1.setText(Listsongs.get(i).getName());
                    media.start();
                    play.setBackgroundResource(R.drawable.pause);
                } else {
                    i = Listsongs.size() - 1;
                    Uri u = Uri.parse(Listsongs.get(i).toString());
                    media = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(String.valueOf(u))));
                    t1.setText(Listsongs.get(i).getName());
                    media.start();
                    play.setBackgroundResource(R.drawable.pause);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                media.stop();
                media.release();
                if (i < Listsongs.size() - 1) {
                    i = i + 1;
                    Uri u = Uri.parse(Listsongs.get(i).toString());
                    media = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(String.valueOf(u))));
                    t1.setText(Listsongs.get(i).getName());
                    media.start();
                    play.setBackgroundResource(R.drawable.pause);
                } else {
                    i = 0;
                    Uri u = Uri.parse(Listsongs.get(i).toString());
                    media = MediaPlayer.create(getApplicationContext(), Uri.fromFile(new File(String.valueOf(u))));
                    t1.setText(Listsongs.get(i).getName());
                    media.start();
                    play.setBackgroundResource(R.drawable.pause);
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (media.isPlaying()) {
                    media.pause();
                    play.setBackgroundResource(R.drawable.play_button);
                } else {
                    media.start();
                    play.setBackgroundResource(R.drawable.pause);
                }
            }
        });
    }
}
