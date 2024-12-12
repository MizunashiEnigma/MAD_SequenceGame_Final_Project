package com.example.mad_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GameOverActivity extends AppCompatActivity {

    private TextView finalScoreDisplay;
    private EditText playerNameInput;
    private Button saveScoreButton, viewHighScoresButton;
    private int finalScore;

    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        // The Views
        TextView scoreTextView = findViewById(R.id.scoreTV);
        Button playAgainButton = findViewById(R.id.playAgainButton);
        Button returnHomeButton = findViewById(R.id.returnButton);
        Button highScoresButton = findViewById(R.id.highScoresButton);

        // Grab the Score from the Game
        int score = getIntent().getIntExtra("score", 0);

        // Display the Players Score
        scoreTextView.setText("Final Score: " + score);

        // Is it in Top 5?
        if (isTop5Score(score))
        {
            // If it is, Get the Players Name
            promptForName(score);
        }
        else
        {
            // If not... no recognition for them
        }

        // Restart the Game
        playAgainButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(GameOverActivity.this, SequenceActivity.class);
            startActivity(intent);
            finish();
        });

        // Return Home
        returnHomeButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(GameOverActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // View High-Scores
        highScoresButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(GameOverActivity.this, com.example.mad_final_project.HighScoreActivity.class);
            startActivity(intent);
        });
    }

    // Is it in the Top 5?
    private boolean isTop5Score(int score)
    {
        DatabaseHandler dbHandler = new DatabaseHandler(this);

        // Grab the Top Scores
        Cursor cursor = dbHandler.getTopScores();

        if (cursor != null && cursor.moveToLast())
        {
            int lowestTopScore = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHandler.KEY_HIGHSCORE));
            cursor.close();

            return score > lowestTopScore || cursor.getCount() < 5;
        }
        // Kinda useless now, but the entry will always display is there isn't 5 entries
        return true;
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


    // Get the Users Name
    private void promptForName(int score)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New High Score!");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_get_name, null, false);
        final EditText input = viewInflated.findViewById(R.id.inputName);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                String name = input.getText().toString();

                if (!name.isEmpty())
                {
                    // Save the Score with the Entered Name in the Database
                    DatabaseHandler dbHandler = new DatabaseHandler(GameOverActivity.this);
                    dbHandler.addScore(name, score);
                }
                else
                {
                    // Validation if they Enter an Empty Name
                    Toast.makeText(GameOverActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Cancel Button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                Toast.makeText(GameOverActivity.this, "Score not saved", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }
}

//dbHandler = new DatabaseHandler(this);
//finalScore = getIntent().getIntExtra("finalScore", 0);
//        finalScoreDisplay.setText("Final Score: " + finalScore);
//
//        if (isTop5(finalScore)) {
//        saveScoreButton.setEnabled(true);
//        } else {
//                playerNameInput.setVisibility(View.GONE);
//            saveScoreButton.setVisibility(View.GONE);
//        }
//
//                saveScoreButton.setOnClickListener(v -> {
//String playerName = playerNameInput.getText().toString().trim();
//            if (TextUtils.isEmpty(playerName)) {
//        Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
//            } else {
//saveHighScore(playerName, finalScore);
//viewHighScores();
//            }
//                    });


//private boolean isTop5(int score) {
//    // Get the top 5 scores from the database
//    List<HighscoreClass> topScores = dbHandler.top5Highscore();
//
//    // Check if the list has less than 5 scores or if the score qualifies as top 5
//    return topScores.size() < 5 || score > topScores.get(topScores.size() - 1).getHighscore();
//}
//
//private void saveHighScore(String name, int score) {
//    HighscoreClass newHighScore = new HighscoreClass(name, score);
//    dbHandler.addHighscore(newHighScore);
//    Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
//}
//
//private void viewHighScores() {
//    Intent intent = new Intent(this, HighScoreActivity.class);
//    startActivity(intent);
//    finish();
//}
