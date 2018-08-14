package com.psx.wordlistapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewWordActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.roomwordssample.REPLY";
    private EditText editText;
    private static final String TAG = NewWordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);
        editText = findViewById(R.id.edit_word);

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Clicked Save Button");
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Log.d("NewWordActivity",editText.getText().toString() + " is the word");
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    Log.d(TAG,"Word is"+editText.getText().toString());
                    String word = editText.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, word);
                    setResult(RESULT_OK,replyIntent);
                }
                finish();
            }
        });
    }
}
