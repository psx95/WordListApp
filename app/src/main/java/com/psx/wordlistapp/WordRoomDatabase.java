package com.psx.wordlistapp;

import android.content.Context;
import android.os.AsyncTask;

import com.psx.wordlistapp.dao.WordDAO;
import com.psx.wordlistapp.entities.Word;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Word.class}, version = 1)
public abstract class WordRoomDatabase extends RoomDatabase {

    private static WordRoomDatabase INSTANCE;

    public static WordRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WordRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Provide Abstract getter method for each Dao
    public abstract WordDAO getWordDAO();

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDBAsync(INSTANCE).execute();
        }
    };

    static class PopulateDBAsync extends AsyncTask<Void, Void, Void> {

        private WordDAO wordDAO;
        String words[] = {"fish", "fins", "dolphin"};

        public PopulateDBAsync(WordRoomDatabase wordRoomDatabase) {
            wordDAO = wordRoomDatabase.getWordDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDAO.deleteAll();
            for (String word1 : words) {
                Word word = new Word(word1);
                wordDAO.insert(word);
            }
            return null;
        }
    }
}
