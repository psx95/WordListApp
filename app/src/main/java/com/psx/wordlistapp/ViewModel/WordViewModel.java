package com.psx.wordlistapp.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.psx.wordlistapp.entities.Word;
import com.psx.wordlistapp.repository.WordRepository;

import java.util.List;

/**
 * Provides data to the UI.
 * Acts as a communication center between the repository and the UI.
 * ViewModel Instances survive the configuration changes.
 *
 * NOTE : Never pass context in the ViewModel Classes for it may point to an Activity instance that has been destroyed.
 * If Context needs to be used, Use AndroidViewModel instead of ViewModel.
 */
public class WordViewModel extends AndroidViewModel {

    private WordRepository wordRepository;
    /**
     * LiveData - a data holder class that follows the Observer pattern, thus can be observed.
     * Alwasys caches/holds the latest version of the data. Is Lifecycle Aware.
     */
    private LiveData<PagedList<Word>> allWords;

    public WordViewModel(Application application) {
        super(application);
        wordRepository = new WordRepository(application);
        allWords = wordRepository.getAllWords();
    }

    public LiveData<PagedList<Word>> getAllWords() {
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

    public void updateWords(Word... words) {
        wordRepository.updateMultiple(words);
    }
}
