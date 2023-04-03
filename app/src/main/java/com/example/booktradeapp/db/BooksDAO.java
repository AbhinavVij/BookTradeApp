package com.example.booktradeapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BooksDAO {

    @Query("SELECT * FROM books ORDER BY date_modified desc")
    LiveData<List<Books>> getAll();

    @Query("SELECT * FROM books WHERE rowid = :bookId")
    Books getById(int bookId);

    @Insert
    void insert(Books... books);

    @Update
    void update(Books... books);

    @Delete
    void delete(Books... user);

    @Query("DELETE FROM books WHERE rowid = :bookId")
    void delete(int bookId);

}
