package com.example.mad_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GameOverActivity extends AppCompatActivity {

    private TextView finalScoreDisplay;
    private EditText playerNameInput;
    private Button saveScoreButton, viewHighScoresButton;
    private int finalScore;

    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        finalScoreDisplay = findViewById(R.id.finalScoreDisplay);
        playerNameInput = findViewById(R.id.playerNameInput);
        saveScoreButton = findViewById(R.id.saveScoreButton);
        viewHighScoresButton = findViewById(R.id.viewHighScoresButton);

        dbHandler = new DatabaseHandler(this);
        finalScore = getIntent().getIntExtra("finalScore", 0);
        finalScoreDisplay.setText("Final Score: " + finalScore);

        if (isTop5(finalScore)) {
            saveScoreButton.setEnabled(true);
        } else {
            playerNameInput.setVisibility(View.GONE);
            saveScoreButton.setVisibility(View.GONE);
        }

        saveScoreButton.setOnClickListener(v -> {
            String playerName = playerNameInput.getText().toString().trim();
            if (TextUtils.isEmpty(playerName)) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                saveHighScore(playerName, finalScore);
                viewHighScores();
            }
        });

        viewHighScoresButton.setOnClickListener(v -> viewHighScores());
    }

    private boolean isTop5(int score) {
        // Get the top 5 scores from the database
        List<HighscoreClass> topScores = dbHandler.top5Highscore();

        // Check if the list has less than 5 scores or if the score qualifies as top 5
        return topScores.size() < 5 || score > topScores.get(topScores.size() - 1).getHighscore();
    }

    private void saveHighScore(String name, int score) {
        HighscoreClass newHighScore = new HighscoreClass(name, score);
        dbHandler.addHighscore(newHighScore);
        Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
    }

    private void viewHighScores() {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        finish();
    }
}
