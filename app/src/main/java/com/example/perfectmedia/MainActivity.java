package com.example.perfectmedia;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView UiForSongs;
    String[] items ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiForSongs = findViewById(R.id.songlist);
        runtimePermission();
    }
    public void runtimePermission(){
        Dexter.withActivity(this)
                .withPermission(String.valueOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        readSongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                }).check();

    }
    public ArrayList<File> Fetchsongs(File file){
        ArrayList<File> songsList = new ArrayList<>();
        File[] files = file.listFiles();
        for(File singleFile: files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                songsList.addAll(Fetchsongs(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    songsList.add(singleFile); } }
        }
        return songsList;
    }
    void readSongs(){
        final ArrayList<File> Listsongs = Fetchsongs(Environment.getExternalStorageDirectory());
        items = new String[Listsongs.size()];
        for(int i=0;i<Listsongs.size();i++){
            items[i]= Listsongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        final ArrayAdapter<String> SongAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        UiForSongs.setAdapter(SongAdapter);
        UiForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,Play.class);
                intent.putExtra("Listofsongs",Listsongs).putExtra("Position", i);
                intent.putExtra("Song_name", Listsongs.get(i).getName().toString());
                startActivity(intent);


            }
        });
    }
}

