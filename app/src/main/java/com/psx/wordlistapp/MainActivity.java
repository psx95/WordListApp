package com.psx.wordlistapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.psx.wordlistapp.ViewModel.WordViewModel;
import com.psx.wordlistapp.entities.Word;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;
    private WordViewModel wordViewModel;
    final WordListAdapter wordListAdapter = new WordListAdapter();
    public static final int REQUEST_ADD_WORD = 1;
    private ItemTouchHelper itemTouchHelper;

    private static RecyclerViewClickListener recyclerViewClickListener;
    private static WordUpdateCallback wordUpdateCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, NewWordActivity.class), REQUEST_ADD_WORD);
            }
        });
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        addObserversForLiveData();
        recyclerView = findViewById(R.id.recyclerview);
        setupRecyclerViewClickListener();
        setupWordUpdateCallback();
        setupItemTouchHelper();
        setupRecyclerView();
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupWordUpdateCallback() {
        wordUpdateCallback = new WordUpdateCallback() {
            @Override
            public void updateWord(Word word) {
                wordViewModel.updateWords(word);
            }
        };
    }

    private void showDialog(Word word) {
        EditWordDialog editWordDialog = new EditWordDialog(MainActivity.this, word, wordUpdateCallback);
        editWordDialog.show();
    }

    private void setupRecyclerViewClickListener() {
        recyclerViewClickListener = new RecyclerViewClickListener() {
            @Override
            public void onWordClick(Word word) {
                showDialog(word);
                Toast.makeText(getApplicationContext(), "Updating word " + word.getWord(), Toast.LENGTH_SHORT).show();
            }
        };
        wordListAdapter.setRecyclerViewClickListener(recyclerViewClickListener);
    }

    private void addObserversForLiveData() {
        wordViewModel.allWords.observe(this, new Observer<PagedList<Word>>() {
            @Override
            public void onChanged(@Nullable PagedList<Word> words) {
                Log.d(TAG, "words are " + words);
                wordListAdapter.setWords(words);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(wordListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_all) {
            Toast.makeText(this, "Deleting all words from local database", Toast.LENGTH_SHORT).show();
            wordViewModel.deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_WORD && resultCode == RESULT_OK) {
            Log.d(TAG, "WORD entering");
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY));
            wordViewModel.insert(word);
        } else {
            Log.d(TAG, "Request Code " + requestCode + " result code " + resultCode);
            if (data != null)
                Log.d(TAG, "intent data " + data.toString());
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    private void setupItemTouchHelper() {
        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Word word = wordListAdapter.getWordAtPosition(position);
                Toast.makeText(getApplicationContext(), "Deleting " + word.getWord(), Toast.LENGTH_SHORT).show();
                wordViewModel.deleteWord(word);
            }
        });
    }

    public interface RecyclerViewClickListener {
        void onWordClick(Word word);
    }

    public interface WordUpdateCallback {
        void updateWord(Word word);
    }
}
