package com.example.booktradeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.booktradeapp.db.Books;
import com.example.booktradeapp.db.BooksDatabase;

import java.util.concurrent.TimeUnit;

public class AddActivity extends AppCompatActivity {
    private int book_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_sell);
        setSupportActionBar(findViewById(R.id.my_toolbar));

        book_id = getIntent().getIntExtra("book_id", -1);

        // Note: that we do not want to lose the state if the activity is being
        // recreated
        if (savedInstanceState == null) {
            if (book_id != -1) {
                BooksDatabase.getBooks(book_id, books -> {
                    ((EditText) findViewById(R.id.txtEditTitle)).setText(books.title);
                    ((EditText) findViewById(R.id.txtEditAuthor)).setText(books.author);
                    ((EditText) findViewById(R.id.txtEditPublication)).setText(books.publication);
                   // ((EditText) findViewById(R.id.txtEditImage)).setText(Books.image);
                    ((EditText) findViewById(R.id.txtEditCondition)).setText(books.condition);

                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add, menu);
        if (book_id == -1) {
            menu.getItem(1).setIcon(R.drawable.ic_cancel);
            menu.getItem(1).setTitle(R.string.menu_cancel);
            setTitle("Add Book Details");
        }
        else {
            setTitle("Edit Book Details");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                updateDatabase();
                return true;
            case R.id.menu_delete:
                if (book_id != -1) {
                    ConfirmDeleteDialog confirmDialog = new ConfirmDeleteDialog();
                    confirmDialog.show(getSupportFragmentManager(), "deletionConfirmation");
                }
                else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDatabase() {
        Books books = new Books(book_id == -1?0:book_id,
                ((EditText) findViewById(R.id.txtEditTitle)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditAuthor)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditCondition)).getText().toString(),
               // ((EditText) findViewById(R.id.txtEditImage)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditPublication)).getText().toString(),
                String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
        if (book_id == -1) {
            BooksDatabase.insert(books);
        } else {
            BooksDatabase.update(books);
        }
        finish(); // Quit activity
    }

    public void deleteRecord() {
        BooksDatabase.delete(book_id);
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putBoolean("liked", liked);
//    }

    public static class ConfirmDeleteDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Delete this Book ?")
                    .setMessage("You will not be able to undo the deletion!")
                    .setPositiveButton("Delete",
                            (dialog,id) -> {
                                ((AddActivity) getActivity()).deleteRecord();
                                getActivity().finish();
                            })
                    .setNegativeButton("Return to Book list",
                            (dialog, id) -> getActivity().finish());
            return builder.create();
        }
    }

}
