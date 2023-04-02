package com.example.booktradeapp.db;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class BooksViewModel extends AndroidViewModel {

    private LiveData<List<Books>> books;

    public BooksViewModel (Application application) {
        super(application);
        books = BooksDatabase.getDatabase(getApplication()).booksDAO().getAll();
    }

    public void filterBooks() {
        books = BooksDatabase.getDatabase(getApplication()).booksDAO().getAll();
    }

    public LiveData<List<Books>> getAllBooks() {
        return books;
    }
}
