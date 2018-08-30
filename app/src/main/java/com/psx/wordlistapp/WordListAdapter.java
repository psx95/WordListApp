package com.psx.wordlistapp;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.psx.wordlistapp.entities.Word;

import java.util.List;


public class WordListAdapter extends PagedListAdapter<Word, WordListAdapter.WordViewHolder> {

    private List<Word> words;
    private MainActivity.RecyclerViewClickListener recyclerViewClickListener;

    WordListAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        if (words != null) {
            final Word current = words.get(position);
            holder.wordItemView.setText(current.getWord());
            holder.wordItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickListener.onWordClick(current);
                }
            });
        } else {
            holder.wordItemView.setText("No Word");
        }
    }

    public void setWords(List<Word> words) {
        this.words = words;
        notifyDataSetChanged();
    }

    public void setRecyclerViewClickListener(MainActivity.RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    public Word getWordAtPosition(int position) {
        return words.get(position);
    }

    @Override
    public int getItemCount() {
        if (words != null) {
            Log.d("TAG", "words not null, Size " + words.size());
            return words.size();
        } else {
            Log.i("TAG", "words is null in the adapter, size 0.");
            return 0;
        }
    }

    class WordViewHolder extends RecyclerView.ViewHolder {

        private final TextView wordItemView;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
        }
    }

    private static final DiffUtil.ItemCallback<Word> DIFF_CALLBACK = new DiffUtil.ItemCallback<Word>() {
        @Override
        public boolean areItemsTheSame(Word oldItem, Word newItem) {
            Log.d("TAG", (oldItem.getId() == newItem.getId()) + "");
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Word oldItem, Word newItem) {
            Log.d("TAG", "old word " + oldItem.getWord() + " new word " + newItem.getWord());
            return oldItem.getWord().equals(newItem.getWord());
        }
    };
}
