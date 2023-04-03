package com.example.booktradeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktradeapp.db.Books;
import com.example.booktradeapp.db.BooksDatabase;
import com.example.booktradeapp.db.BooksViewModel;

import java.util.List;

public class SellPage extends AppCompatActivity {

    private BooksViewModel booksViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_page);
        Toolbar mytoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mytoolbar);

        RecyclerView recyclerView = findViewById(R.id.books_sell);
        BooksListAdapter adapter = new BooksListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        booksViewModel = new ViewModelProvider(this).get(BooksViewModel.class);
        booksViewModel.filterBooks();
        booksViewModel.getAllBooks().observe(this, adapter::setBooks);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent intent = new Intent(SellPage.this, AddActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_book_add, menu);
        setTitle("SELL");

        return true;
    }

    public void displaySetup(int id) {
        BooksDatabase.getBooks(id, books -> {
            Bundle args = new Bundle();
            args.putInt("books_id", books.id);
            args.putString("title", books.title);
            args.putString("author", books.author);
            args.putString("condition", books.condition);
            args.putString("publication", books.publication);


            DisplaySetupDialog setupDialog = new DisplaySetupDialog();
            setupDialog.setArguments(args);
            setupDialog.show(getSupportFragmentManager(), "setupDialog");
        });
    }

    public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BooksViewHolder> {

        // If the JokeListAdapter were an outer class, the JokeViewHolder could be
        // a static class.  We want to be able to get access to the MainActivity instance,
        // so we want it to be an inner class
        class BooksViewHolder extends RecyclerView.ViewHolder {
            private final TextView titleView;
            private final TextView authorView;
            private final TextView publicationView;
            private final TextView conditionView;

            private Books books;

            // Note that this view holder will be used for different items -
            // The callbacks though will use the currently stored item
            private BooksViewHolder(View itemView) {
                super(itemView);
                titleView = itemView.findViewById(R.id.textView6);
                authorView = itemView.findViewById(R.id.textView7);
                conditionView = itemView.findViewById(R.id.textView9);
                publicationView = itemView.findViewById(R.id.textView8);



                itemView.setOnLongClickListener(view -> {
                    // Note that we need a reference to the MainActivity instance
                    Intent intent = new Intent(SellPage.this, AddActivity.class);
                    // Note getItemId will return the database identifier
                    intent.putExtra("book_id", books.id);
                    // Note that we are calling a method of the MainActivity object
                    startActivity(intent);
                    return true;
                });

                itemView.setOnClickListener(view -> displaySetup(books.id));
//
//                likedView.setOnClickListener(view -> {
//                    joke.liked = !joke.liked;
//                    JokeDatabase.update(joke);
//                });
            }
        }

        private final LayoutInflater layoutInflater;
        private List<Books> books; // Cached copy of Books

        BooksListAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.recycler_view_row, parent, false);
            return new BooksViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull BooksViewHolder holder, int position) {
            if (books != null) {
                Books current = books.get(position);
                holder.books = current;
                holder.titleView.setText(current.title);
                holder.authorView.setText(current.author);
                holder.conditionView.setText(current.condition);
                holder.publicationView.setText(current.publication);

            } else {
                // Covers the case of data not being ready yet.
                holder.titleView.setText("...intializing...");
            }
        }

        @Override
        public int getItemCount() {
            if (books != null)
                return books.size();
            else return 0;
        }

        void setBooks(List<Books> books) {
            this.books = books;
            notifyDataSetChanged();
        }
    }


    public static class DisplaySetupDialog extends DialogFragment {
        int books_id;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            books_id = getArguments().getInt("books_id");
            final String title = getArguments().getString("title");
            final String author = getArguments().getString("author");
            final String condition = getArguments().getString("condition");
            final String publication = getArguments().getString("publication");

            builder.setTitle(title)
                    .setMessage("Author of Book:" + author + "\nCondition of Book:" + condition + "\nBook published by :"+ publication)
//                    .setMessage("Condition of Book:" + condition)
//                    .setMessage("Book published by :"+ publication)
                    .setPositiveButton("Add to Cart",
                            (dialog, id) -> {})
                    .setNegativeButton("Cancel",
                            (dialog, id) -> {});
            return builder.create();
        }


    }
}
