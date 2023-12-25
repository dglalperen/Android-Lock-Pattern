package com.example.androidlockpattern;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PatternLockView patternLockView;
    private Button btnSetPattern, btnResetPattern;
    private List<Integer> savedPattern;
    private long savedPatternTime;
    private boolean isPatternSetMode = true;
    private boolean isUnlockMode = false;
    private static final double SUSPICION_THRESHOLD = 1.5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        patternLockView = findViewById(R.id.patternLockView);
        btnSetPattern = findViewById(R.id.btnSetPattern);
        btnResetPattern = findViewById(R.id.btnResetPattern);
        Button btnUnlockMode = findViewById(R.id.btnUnlockMode);

        btnUnlockMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUnlockMode = true;
                isPatternSetMode = false;
                btnSetPattern.setText("Unlock");
                Toast.makeText(MainActivity.this, "Unlock Mode. Draw the pattern to unlock.", Toast.LENGTH_SHORT).show();
            }
        });

        btnResetPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternLockView.resetPattern();
                Toast.makeText(MainActivity.this, "Pattern Reset", Toast.LENGTH_SHORT).show();
            }
        });

        btnSetPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUnlockMode) {
                    handleUnlockAttempt();
                } else if (isPatternSetMode) {
                    // Handle pattern setting
                    handlePatternSetting();
                } else {
                    // Handle pattern confirmation
                    handlePatternConfirmation();
                }
            }
        });

        updateUIForPatternSetting();

    }

    private void handlePatternSetting() {
        savedPattern = patternLockView.getPattern();
        savedPatternTime = patternLockView.getDrawingTime();
        if (!savedPattern.isEmpty()) {
            Toast.makeText(MainActivity.this, "Pattern Set. Please confirm the pattern.", Toast.LENGTH_SHORT).show();
            updateUIForPatternConfirmation();
        } else {
            Toast.makeText(MainActivity.this, "No pattern drawn", Toast.LENGTH_SHORT).show();
        }
        patternLockView.resetPattern();
    }

    private void handlePatternConfirmation() {
        List<Integer> confirmPattern = patternLockView.getPattern();
        if (confirmPattern.equals(savedPattern)) {
            Toast.makeText(MainActivity.this, "Pattern Confirmed", Toast.LENGTH_SHORT).show();
            // TODO: Save the pattern to shared preferences
            updateUIForPatternSetting();
        } else {
            Toast.makeText(MainActivity.this, "Pattern does not match. Please try again.", Toast.LENGTH_SHORT).show();
        }
        patternLockView.resetPattern();
    }

    private void updateUIForPatternSetting() {
        btnSetPattern.setText("Set Pattern");
        isPatternSetMode = true;
        isUnlockMode = false;
    }

    private void updateUIForPatternConfirmation() {
        btnSetPattern.setText("Confirm Pattern");
        isPatternSetMode = false;
    }

    private void handleUnlockAttempt() {
        List<Integer> attemptPattern = patternLockView.getPattern();
        long attemptTime = patternLockView.getDrawingTime();

        if (attemptPattern.equals(savedPattern)) {
            if (isSuspicious(attemptTime)) {
                Toast.makeText(this, "Pattern correct but timing suspicious", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unlocked Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Incorrect Pattern", Toast.LENGTH_SHORT).show();
        }
        isUnlockMode = false;
    }

    private boolean isSuspicious(long currentTime) {
        return currentTime > savedPatternTime * SUSPICION_THRESHOLD;
    }
}