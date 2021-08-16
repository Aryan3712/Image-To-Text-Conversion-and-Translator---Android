package com.aryanwalia.imagetotextconversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class mailActivity extends AppCompatActivity {

    private EditText edit_text_to;
    private EditText edit_text_subject;
    private EditText edit_text_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        edit_text_to = findViewById(R.id.edit_text_to);
        edit_text_subject = findViewById(R.id.edit_text_subject);
        edit_text_message = findViewById(R.id.edit_text_message);

        Button send_btn = findViewById(R.id.btn_send);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    private void sendMail(){

        String recipients[] = {"sckumain1@gmail.com"};
        String subject = edit_text_subject.getText().toString();
        String message = edit_text_message.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose One Client"));

    }
}