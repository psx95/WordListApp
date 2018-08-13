package com.psx.wordlistapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.psx.wordlistapp.dao.WordDAO;
import com.psx.wordlistapp.WordRoomDatabase;
import com.psx.wordlistapp.entities.Word;

import java.util.List;

import androidx.lifecycle.LiveData;

/**
 * A Repository	is a class	that	abstracts	access	to	multiple	data	sources.
 * The	Repository	is	not part	of	the	Architecture	Components	libraries,
 * but	is	a	suggested	best	practice	for	code separation	and	architecture.
 * A Repository	class handles data operations.
 * It provides a clean	API	to	the	rest of	the	app	for	app	data.
 */
public class WordRepository {
    private WordDAO wordDAO;
    private LiveData<List<Word>> allWords;

    public WordRepository(Application application) {
        WordRoomDatabase wordRoomDatabase = WordRoomDatabase.getDatabase(application);
        wordDAO = wordRoomDatabase.getWordDAO();
        allWords = wordDAO.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        new insertAsyncTask(wordDAO).execute(word);
    }

    private static class insertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDAO asyncTaskDao;

        insertAsyncTask(WordDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
