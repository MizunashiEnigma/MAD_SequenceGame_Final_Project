package com.example.mad_final_project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.utilities.Score;

import java.util.List;

public class HighScoreActivity extends AppCompatActivity {
    private ListView highScoreList;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        highScoreList = findViewById(R.id.highScoreList);
        dbHandler = new DatabaseHandler(this);

        List<Score> topScores = dbHandler.getTop5Scores();
        ArrayAdapter<Score> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topScores);
        highScoreList.setAdapter(adapter);
    }
}
