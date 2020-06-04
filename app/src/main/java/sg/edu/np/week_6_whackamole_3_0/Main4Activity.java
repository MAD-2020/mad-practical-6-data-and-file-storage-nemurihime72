package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Main4Activity extends AppCompatActivity {

    /* Hint:
        1. This creates the Whack-A-Mole layout and starts a countdown to ready the user
        2. The game difficulty is based on the selected level
        3. The levels are with the following difficulties.
            a. Level 1 will have a new mole at each 10000ms.
                - i.e. level 1 - 10000ms
                       level 2 - 9000ms
                       level 3 - 8000ms
                       ...
                       level 10 - 1000ms
            b. Each level up will shorten the time to next mole by 100ms with level 10 as 1000 second per mole.
            c. For level 1 ~ 5, there is only 1 mole.
            d. For level 6 ~ 10, there are 2 moles.
            e. Each location of the mole is randomised.
        4. There is an option return to the login page.
     */
    private static final String FILENAME = "Main4Activity.java";
    private static final String TAG = "Whack-A-Mole3.0!";
    CountDownTimer readyTimer;
    CountDownTimer newMolePlaceTimer;
    MyDBHandler dbHandler;
    TextView advScore;
    String username;
    Integer level;
    Integer levelSpeed;
    Button backButton;

    private void readyTimer() {
        /*  HINT:
            The "Get Ready" Timer.
            Log.v(TAG, "Ready CountDown!" + millisUntilFinished/ 1000);
            Toast message -"Get Ready In X seconds"
            Log.v(TAG, "Ready CountDown Complete!");
            Toast message - "GO!"
            belongs here.
            This timer countdown from 10 seconds to 0 seconds and stops after "GO!" is shown.
         */
        readyTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, FILENAME + "Ready countdown: " + millisUntilFinished/1000);
                Toast.makeText(getApplicationContext(), "Get ready in " + millisUntilFinished/1000 + " seconds!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish() {
                Log.v(TAG, FILENAME + "Ready countdown complete");
                Toast.makeText(getApplicationContext(), "Go!", Toast.LENGTH_SHORT).show();
                placeMoleTimer();
                readyTimer.cancel();
            }
        };
        readyTimer.start();
    }

    private void placeMoleTimer() {
        /* HINT:
           Creates new mole location each second.
           Log.v(TAG, "New Mole Location!");
           setNewMole();
           belongs here.
           This is an infinite countdown timer.
         */
        levelSpeed = (11 - level)*1000;
        Log.v(TAG, FILENAME + ": lvl speed: " + levelSpeed);
        newMolePlaceTimer = new CountDownTimer(levelSpeed, levelSpeed) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v(TAG, FILENAME + ": New mole location!");
                setNewMole();
            }

            @Override
            public void onFinish() {
                start();
            }
        }.start();
    }

    private static final int[] BUTTON_IDS = {
            /* HINT:
                Stores the 9 buttons IDs here for those who wishes to use array to create all 9 buttons.
                You may use if you wish to change or remove to suit your codes.*/
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        /*Hint:
            This starts the countdown timers one at a time and prepares the user.
            This also prepares level difficulty.
            It also prepares the button listeners to each button.
            You may wish to use the for loop to populate all 9 buttons with listeners.
            It also prepares the back button and updates the user score to the database
            if the back button is selected.
         */

        dbHandler = new MyDBHandler(this, null, null, 3);
        advScore = findViewById(R.id.advancedScore);
        Intent getIntent = getIntent();
        username = getIntent.getStringExtra("username");
        level = getIntent.getExtras().getInt("level");

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserScore();
                Intent intentBack= new Intent(Main4Activity.this, Main3Activity.class);
                intentBack.putExtra("username", username);
                startActivity(intentBack);
            }
        });

        for (final int id : BUTTON_IDS) {
            /*  HINT:
            This creates a for loop to populate all 9 buttons with listeners.
            You may use if you wish to remove or change to suit your codes.
            */
            final Button btn = findViewById(id);
            btn.setText("O");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    doCheck(btn);
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        readyTimer();
        levelSpeed = 11 - level;
    }

    private void doCheck(Button checkButton) {
        /* Hint:
            Checks for hit or miss
            Log.v(TAG, FILENAME + ": Hit, score added!");
            Log.v(TAG, FILENAME + ": Missed, point deducted!");
            belongs here.
        */
        final TextView advancedScore = (TextView) findViewById(R.id.advancedScore);
        if (checkButton.getText() == "*") {            //if correct button is pressed

            Log.v(TAG, "Hit, score added!");
            Log.v(TAG, advancedScore.getText().toString()); //for checking
            addScore();                             //add 1 to score
            Log.v(TAG, advancedScore.getText().toString());
        } else {                                      // if wrong button is pressed
            Log.v(TAG, "Missed, score deducted!");
            Log.v(TAG, advancedScore.getText().toString()); //for checking
            removeScore();                          //remove 1 from score
            Log.v(TAG, advancedScore.getText().toString());
        }
    }
    public void setNewMole ()
    {
        /* Hint:
        Clears the previous mole location and gets a new random location of the next mole location.
        Sets the new location of the mole. Adds additional mole if the level difficulty is from 6 to 10.
        */
        Random ran = new Random();
        int randomLocation = ran.nextInt(9);
        int randomLocation2 = ran.nextInt(9);
        ArrayList<Integer> ranList = new ArrayList<>();
        if (level < 6) {
            for (int i = 0; i<BUTTON_IDS.length; i++) {
                Button btn = findViewById(BUTTON_IDS[i]);
                btn.setText("O");
                Button mole = findViewById(BUTTON_IDS[randomLocation]);
                mole.setText("*");
            }
        } else {
            for (int i = 0; i<9; i++){
                ranList.add(i);
            }
            Collections.shuffle(ranList);
            Log.v(TAG, FILENAME + "no 1 and 2: " + ranList.get(0) + " " + ranList.get(1));
            for (int i = 0; i<BUTTON_IDS.length; i++) {
                Button btn = findViewById(BUTTON_IDS[i]);
                btn.setText("O");
                Button mole1 = findViewById(BUTTON_IDS[ranList.get(0)]);
                Button mole2 = findViewById(BUTTON_IDS[ranList.get(1)]);
                mole1.setText("*");
                mole2.setText("*");
            }
        }

    }

    private void updateUserScore ()
    {

        /* Hint:
        This updates the user score to the database if needed. Also stops the timers.
        Log.v(TAG, FILENAME + ": Update User Score...");
        */
        newMolePlaceTimer.cancel();
        readyTimer.cancel();
        UserData userData = dbHandler.findUser(username);
        int currentLevel = userData.getLevels().indexOf(level);
        int highScore = userData.getScores().get(currentLevel);
        String currentScore = advScore.getText().toString();
        Log.v(TAG, FILENAME + ": Current score: " + currentScore);
        if (Integer.parseInt(currentScore) > highScore) {
            Log.v(TAG, FILENAME + ": Update user score");
            dbHandler.updateHighScore(username, Integer.parseInt(currentScore), level);
        } else {
            Log.v(TAG, FILENAME + ": Did not beat high score");
        }

    }
    private void addScore () {
        final TextView advancedScore = (TextView) findViewById(R.id.advancedScore);
        int intScore = Integer.parseInt(advancedScore.getText().toString());    //gets int score from the textview after getting text and converting to string
        intScore += 1;  //adds 1 to score
        String score = Integer.toString(intScore);  //convert back to string
        advancedScore.setText(score);   //sets the printed textview to the new score
    }

    private void removeScore () {
        final TextView advancedScore = (TextView) findViewById(R.id.advancedScore);
        int intScore = Integer.parseInt(advancedScore.getText().toString());    //gets int score from the textview after getting text and converting to string
        if (intScore != 0) {    //if score is not 0
            intScore -= 1;        // score - 1
        } else {                  //if score is 0
            intScore = 0;         //score will remain 0
        }
        String score = Integer.toString(intScore);  //convert back to string
        advancedScore.setText(score);   //sets the printed textview to the new score
    }
}

