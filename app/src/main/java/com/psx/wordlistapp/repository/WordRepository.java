package com.psx.wordlistapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.psx.wordlistapp.dao.WordDAO;
import com.psx.wordlistapp.WordRoomDatabase;
import com.psx.wordlistapp.entities.Word;

import java.util.List;


/**
 * A Repository	is a class	that	abstracts	access	to	multiple	data	sources.
 * The	Repository	is	not part	of	the	Architecture	Components	libraries,
 * but	is	a	suggested	best	practice	for	code separation	and	architecture.
 * A Repository	class handles data operations.
 * It provides a clean	API	to	the	rest of	the	app	for	app	data.
 * <p>
 * Maps multiple data sources such as From local storage as well as network calls made to an external API.
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

    public void deleteAll() {
        new DeleteAllWordsAsyncTask(wordDAO).execute();
    }

    public void deleteWord(Word word) {
        new DeleteWordAsyncTask(wordDAO).execute(word);
    }

    public void insert(Word word) {
        new InsertAsyncTask(wordDAO).execute(word);
    }

    public void updateMultiple(Word... words) {
        new UpdateWordsAsyncTask(wordDAO).execute(words);
    }

    private static class InsertAsyncTask extends AsyncTask<Word, Void, Void> {
        private WordDAO asyncTaskDao;

        InsertAsyncTask(WordDAO dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            asyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class DeleteAllWordsAsyncTask extends AsyncTask<Void, Void, Void> {

        private WordDAO deleteAllTaskDao;

        DeleteAllWordsAsyncTask(WordDAO wordDAO) {
            deleteAllTaskDao = wordDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            deleteAllTaskDao.deleteAll();
            return null;
        }
    }

    private static class DeleteWordAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDAO deleteWordDao;

        public DeleteWordAsyncTask(WordDAO wordDAO) {
            deleteWordDao = wordDAO;
        }

        @Override
        protected Void doInBackground(final Word... words) {
            deleteWordDao.deleteWord(words[0]);
            return null;
        }
    }

    private static class UpdateWordsAsyncTask extends AsyncTask<Word, Void, Void> {

        private WordDAO updateWordDao;

        public UpdateWordsAsyncTask(WordDAO wordDAO) {
            updateWordDao = wordDAO;
        }

        @Override
        protected Void doInBackground(Word... words) {
            updateWordDao.update(words);
            return null;
        }
    }
}
