package com.example.myapplication.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.Card;
import com.example.myapplication.R;
import com.example.myapplication.recyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.example.myapplication.pinDB.PinManager;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addButton;
    private List<Card> cardList;
    private RecyclerView cardView;
    private Button deleteAllButton;
    private PinManager pinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardList = new ArrayList<>();
        init();
        pinManager = new PinManager(this);
        addButton.setOnClickListener(addPinButtonClickListener);
        deleteAllButton.setOnClickListener(deleteAllButtonListener);
    }

    private void init(){
        addButton = findViewById(R.id.addButton);
        cardView = findViewById(R.id.cardView);
        deleteAllButton = findViewById(R.id.deleteAllButton);
    }

    View.OnClickListener deleteAllButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pinManager.deleteFromDB();
            setAdapter();
        }
    };

    View.OnClickListener addPinButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Добавить карту");
            View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
            builder.setView(view);
            EditText nameCardText = view.findViewById(R.id.nameCardText);
            EditText pinCardText = view.findViewById(R.id.pinCardText);
            Button addButton = view.findViewById(R.id.addButton);

            AlertDialog dialog = builder.create();
            dialog.show();
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(nameCardText.getText().length() != 0 && pinCardText.getText().length() == 4) {
                        sendDialogDataToActivity(nameCardText.getText().toString(), pinCardText.getText().toString());
                        dialog.dismiss();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Поля заполнены не полностью", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void setAdapter(){
        cardList = pinManager.readFromDb();
        recyclerAdapter recyclerAdapter = new recyclerAdapter(cardList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        cardView.setLayoutManager(layoutManager);
        cardView.setItemAnimator(new DefaultItemAnimator());
        cardView.setAdapter(recyclerAdapter);
    }

    private void sendDialogDataToActivity(String name, String pin) {
        try {
            String sName = encrypt(name, "codeWord");
            Toast.makeText(this, sName, Toast.LENGTH_SHORT).show();

            String sPin = encrypt(pin, "codeWord");
            Toast.makeText(this, sPin, Toast.LENGTH_SHORT).show();

            pinManager.insertToDb(sName, sPin);
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String encrypt(String name, String codeWord) throws Exception{
        SecretKey key = generateKey(codeWord);
        Cipher cipher = Cipher.getInstance("AES"); //Инициализация класса алгоритма шифрования с заданием системы защиты
        cipher.init(Cipher.ENCRYPT_MODE, key); //Инициализация функционала
        byte[] encValue = cipher.doFinal(name.getBytes()); //Проход по зашифрованным данным и дешифровка
        String encrypt = Base64.encodeToString(encValue, Base64.DEFAULT); //байтовый массив дл яхранения результата
        return encrypt;
    }

    private SecretKey generateKey(String codeWord) throws Exception{//Функция генерации ключа для расшифровки по кодовому слову
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = codeWord.getBytes(StandardCharsets.UTF_8);
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    @Override
    protected void onResume() { //Переопределенный метод, вызываемый при создании активити
        super.onResume();
        pinManager.openDb(); //Открытие бд
        setAdapter();//Загрузка данных в ресайклер
    }

    @Override
    protected void onDestroy() { //Переопределенный метод, вызываемый при уничтожении активити
        super.onDestroy();
        pinManager.closeDb(); //Закрытие бд
    }

}