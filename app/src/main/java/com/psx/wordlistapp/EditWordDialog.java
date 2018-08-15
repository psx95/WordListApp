package com.psx.wordlistapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.psx.wordlistapp.entities.Word;

public class EditWordDialog extends Dialog {

    private AppCompatEditText editText;
    private Button save, cancel;
    private Word currentWord;
    private MainActivity.WordUpdateCallback wordUpdateCallback;

    private EditWordDialog(@NonNull Context context) {
        super(context);
    }

    public EditWordDialog(@NonNull Context context, Word word, MainActivity.WordUpdateCallback wordUpdateCallback) {
        this(context);
        this.currentWord = word;
        this.wordUpdateCallback = wordUpdateCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_word_dialog);
        findViews();
        editText.clearFocus();
        initViews();
        setupListeners();
    }

    private void initViews() {
        editText.setText(currentWord.getWord());
    }

    private void setupListeners() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWord.setWord(editText.getText().toString());
                wordUpdateCallback.updateWord(currentWord);
                Toast.makeText(getContext(),"Updating Word", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void findViews() {
        editText = findViewById(R.id.edit_text_word_update);
        save = findViewById(R.id.save_edit);
        cancel = findViewById(R.id.cancel_edit);
    }
}
