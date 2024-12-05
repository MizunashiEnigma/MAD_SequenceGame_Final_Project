package com.example.mad_final_project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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

        // Get the top 5 high scores from the database
        List<HighscoreClass> topScores = dbHandler.top5Highscore();

        // Create a custom adapter to display HighscoreClass objects
        ArrayAdapter<HighscoreClass> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                topScores
        );

        highScoreList.setAdapter(adapter);
    }
}
