package com.psx.wordlistapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.psx.wordlistapp.entities.Word;
import com.psx.wordlistapp.repository.WordRepository;

import java.util.List;


public class WordViewModel extends AndroidViewModel {

    private WordRepository wordRepository;
    private LiveData<List<Word>> allWords;

    public WordViewModel(Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        allWords = wordRepository.getAllWords();
    }

    public LiveData<List<Word>> getAllWords() {
        return allWords;
    }

    public void insert(Word word) {
        wordRepository.insert(word);
    }

    public void deleteAll() {
        wordRepository.deleteAll();
    }

    public void deleteWord(Word word) {
        wordRepository.deleteWord(word);
    }
}
