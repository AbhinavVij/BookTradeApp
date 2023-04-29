package com.example.booktradeapp.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

    @Database(entities = {Books.class}, version = 1, exportSchema = false)
    public abstract class BooksDatabase extends RoomDatabase {

        public interface BooksListener {
            void onBookReturned(Books joke);
        }

        public abstract BooksDAO booksDAO();

        private static BooksDatabase INSTANCE;

        public static BooksDatabase getDatabase(final Context context) {
            if (INSTANCE == null) {
                synchronized (BooksDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        BooksDatabase.class, "books_database")
                                .addCallback(createBooksDatabaseCallback)
                                .build();
                    }
                }
            }
            return INSTANCE;
        }

        // Note this call back will be run
        private static RoomDatabase.Callback createBooksDatabaseCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                createBooksTable();
            }
        };

        private static void createBooksTable() {
            for (int i = 0; i < DefaultContent.TITLE.length; i++) {
                insert(new Books(0,
                        DefaultContent.TITLE[i],
                        DefaultContent.PUBLICATION[i],
                        DefaultContent.AUTHOR[i],
                        DefaultContent.CONDITION[i],
                        DefaultContent.EMAIL[i],
                        //DefaultContent.IMAGE[i][0],
                        DefaultContent.DATE_MODIFIED[i]));
            }
        }

        public static void getBooks(int id, BooksListener listener) {
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    listener.onBookReturned((Books) msg.obj);
                }
            };

            (new Thread(() -> {
                Message msg = handler.obtainMessage();
                msg.obj = INSTANCE.booksDAO().getById(id);
                handler.sendMessage(msg);
            })).start();
        }

        public static void insert(Books books) {
            (new Thread(()-> INSTANCE.booksDAO().insert(books))).start();
        }

        public static void delete(int bookId) {
            (new Thread(() -> INSTANCE.booksDAO().delete(bookId))).start();
        }


        public static void update(Books books) {
            (new Thread(() -> INSTANCE.booksDAO().update(books))).start();
        }
    }

