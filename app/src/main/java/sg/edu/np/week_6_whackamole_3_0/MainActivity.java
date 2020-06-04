package sg.edu.np.week_6_whackamole_3_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /*
        1. This is the main page for user to log in
        2. The user can enter - Username and Password
        3. The user login is checked against the database for existence of the user and prompts
           accordingly via Toastbox if user does not exist. This loads the level selection page.
        4. There is an option to create a new user account. This loads the create user page.
     */
    private static final String FILENAME = "MainActivity.java";
    private static final String TAG = "Whack-A-Mole3.0!";

    private TextView newUser;
    private Button loginButton;

    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Hint:
            This method creates the necessary login inputs and the new user creation ontouch.
            It also does the checks on button selected.
            Log.v(TAG, FILENAME + ": Create new user!");
            Log.v(TAG, FILENAME + ": Logging in with: " + etUsername.getText().toString() + ": " + etPassword.getText().toString());
            Log.v(TAG, FILENAME + ": Valid User! Logging in");
            Log.v(TAG, FILENAME + ": Invalid user!");

        */

        newUser = findViewById(R.id.textView_newUser);
        newUser.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            }
        });

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etUsername = findViewById(R.id.etUserName);
                EditText etPassword = findViewById(R.id.etPassword);
                Log.v(TAG, FILENAME + " Login with info: " + etUsername.getText().toString());
                Log.v(TAG, FILENAME + " Login with info: " + etPassword.getText().toString());

                if (isValidUser(etUsername.getText().toString(), etPassword.getText().toString())) {
                    Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                    intent.putExtra("username", etUsername.getText().toString());
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Login valid");
                }
                else {
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    Log.v(TAG, FILENAME + ": Login invalid");
                }
            }
        });

    }

    protected void onStop(){
        super.onStop();
        finish();
    }

    public boolean isValidUser(String userName, String password){

        /* HINT:
            This method is called to access the database and return a true if user is valid and false if not.
            Log.v(TAG, FILENAME + ": Running Checks..." + dbData.getMyUserName() + ": " + dbData.getMyPassword() +" <--> "+ userName + " " + password);
            You may choose to use this or modify to suit your design.
         */
        UserData dbData = dbHandler.findUser(userName);
        if (dbData == null) {
            Log.v(TAG, FILENAME + ": User does not exist");
            return false;
        }
        Log.v(TAG, FILENAME + ": Running checks... " + dbData.getMyUserName() + ": " + dbData.getMyPassword() + " <--> " + userName + " " + password);
        if (dbData.getMyUserName().equals(userName) && dbData.getMyPassword().equals(password)) {
            Log.v(TAG, FILENAME + ": Logging in");
            return true;
        } else {
            Log.v(TAG, FILENAME + ": invalid user");
            return false;
        }
    }

}
