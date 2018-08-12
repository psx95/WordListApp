package com.psx.wordlistapp.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {

    @NonNull
    @PrimaryKey
    private String word;

    public Word(@NonNull String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }
}
