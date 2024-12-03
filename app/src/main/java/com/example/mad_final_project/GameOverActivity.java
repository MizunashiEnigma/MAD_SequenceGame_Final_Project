package com.example.mad_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

        if (dbHandler.isTop5(finalScore)) {
            saveScoreButton.setEnabled(true);
        } else {
            playerNameInput.setVisibility(View.GONE);
            saveScoreButton.setVisibility(View.GONE);
        }

        saveScoreButton.setOnClickListener(v -> {
            String playerName = playerNameInput.getText().toString();
            dbHandler.addHighScore(playerName, finalScore);
            viewHighScores();
        });

        viewHighScoresButton.setOnClickListener(v -> viewHighScores());
    }

    private void viewHighScores() {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
        finish();
    }
}
}
