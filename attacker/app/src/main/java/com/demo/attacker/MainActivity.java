package com.demo.attacker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String VICTIM_PACKAGE = "com.demo.victim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * First time the attacker app is launched, it moves to the background.
         * This prepares the demo.
         */
        if (savedInstanceState == null) {
            moveTaskToBack(true);
        }

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(view -> {
            /*
             * Safe educational behavior:
             * We do NOT read, store, log, upload, or display the entered username/password.
             * Dummy input is discarded.
             */
            Toast.makeText(
                    MainActivity.this,
                    "Demo only: dummy input discarded",
                    Toast.LENGTH_SHORT
            ).show();

            openVictimApp();
            finish();
        });
    }

    private void openVictimApp() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(VICTIM_PACKAGE);

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        } else {
            Toast.makeText(
                    this,
                    "Victim app is not installed",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}