package com.example.myapplication.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.Dialog.MyDialogFragment;
import com.example.myapplication.R;
import com.example.myapplication.authdb.AuthDbManager;


public class AuthorizationActivity extends AppCompatActivity implements View.OnClickListener {
    //Создание пременных объектов активити
    private Button one;
    private Button two;
    private Button three;
    private Button four;
    private Button five;
    private Button six;
    private Button seven;
    private Button eight;
    private Button nine;
    private Button zero;
    private ImageButton deleteButton;
    private Button passButton;

    private EditText passwordText;
    private TextView introText;

    private AuthDbManager authDbManager;
    private String password;
    private boolean passwordStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autiruzation);

        init();
        initListener();

        authDbManager = new AuthDbManager(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        authDbManager.openDb();
        checkPassFromDb();
    }

    @SuppressLint("WrongViewCast")
    private void init(){
        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        deleteButton = findViewById(R.id.deleteButton);
        passButton = findViewById(R.id.passButton);

        passwordText = findViewById(R.id.passwordText);
        introText = findViewById(R.id.introText);
    }

    private void initListener(){
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);

        deleteButton.setOnClickListener(deleteButtonClickListener);
        passButton.setOnClickListener(passButtonClickListener);
    }

    View.OnClickListener deleteButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String deleteText = String.valueOf(passwordText.getText());
            passwordText.setText(removeLastChar(deleteText));
        }
    };

    View.OnClickListener passButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPassFromDb();
            FragmentManager manager = getSupportFragmentManager();
            MyDialogFragment myDialogFragment = new MyDialogFragment(password);
            myDialogFragment.show(manager, "myDialog");
        }
    };


    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length()-1);
    }

    @Override
    public void onClick(View view) {
        int btnClicked = view.getId();
        switch (btnClicked){
            case R.id.one:{
                String text = passwordText.getText() + "1";
                passwordText.setText(text);

            }break;
            case R.id.two:{
                String text = passwordText.getText() + "2";
                passwordText.setText(text);

            }break;
            case R.id.three:{
                String text = passwordText.getText() + "3";
                passwordText.setText(text);

            }break;
            case R.id.four:{
                String text = passwordText.getText() + "4";
                passwordText.setText(text);

            }break;
            case R.id.five:{
                String text = passwordText.getText() + "5";
                passwordText.setText(text);

            }break;
            case R.id.six:{
                String text = passwordText.getText() + "6";
                passwordText.setText(text);

            }break;
            case R.id.seven:{
                String text = passwordText.getText() + "7";
                passwordText.setText(text);

            }break;
            case R.id.eight:{
                String text = passwordText.getText() + "8";
                passwordText.setText(text);

            }break;
            case R.id.nine:{
                String text = passwordText.getText() + "9";
                passwordText.setText(text);

            }break;
            case R.id.zero:{
                String text = passwordText.getText() + "0";
                passwordText.setText(text);

            }break;
        }
        if(passwordStatus == false){
            createPassword();
        }
        else{
            checkPassword();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void checkPassword(){
        if(passwordText.getText().length() == 4 && passwordText.getText().toString().equals(password)){
            passwordText.setText("");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else if(passwordText.getText().length() == 4 && !passwordText.getText().toString().equals(password)){
            passwordText.setTextColor(R.color.red);
            Handler handler = new Handler();
            handler.postDelayed(() -> {

                passwordText.setText("");
                passwordText.setTextColor(R.color.white);
            }, 1000);
            Toast.makeText(getApplicationContext(), "Неверный пароль!", Toast.LENGTH_SHORT).show();
        }
    }

    private void createPassword(){
        if(passwordText.getText().length() == 4){
            authDbManager.insertToDb(passwordText.getText().toString());
            passwordText.setText("");
            checkPassFromDb();
            introText.setText("Повторите пароль");
        }
    }

    private void checkPassFromDb(){
        password = authDbManager.readFromDb();
        if (password.isEmpty()){
            passwordStatus = false;
            introText.setText("Придумайте пароль для входа");
        }
        else{
            passwordStatus = true; //Статус пароля
            introText.setText("Введите пароль");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        authDbManager.closeDb();
    }
}
