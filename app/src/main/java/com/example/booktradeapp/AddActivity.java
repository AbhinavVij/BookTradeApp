package com.example.booktradeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.booktradeapp.db.Books;
import com.example.booktradeapp.db.BooksDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class AddActivity extends AppCompatActivity {
    private int book_id;
    private String timestamp;

    private SharedPref sharedPreferences;
    DatabaseReference databaseUser;


    EditText title;
    EditText publication;
    EditText authorb;
    EditText condition;

    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = new SharedPref(this);

        //load theme preference
        if (sharedPreferences.loadNightModeState()) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book_sell);
        setSupportActionBar(findViewById(R.id.my_toolbar));

        book_id = getIntent().getIntExtra("book_id", -1);
        timestamp=getIntent().getStringExtra("timestamp");
        title=(EditText) findViewById(R.id.txtEditTitle);
        authorb=(EditText) findViewById(R.id.txtEditAuthor);
        condition= (EditText) findViewById(R.id.txtEditCondition);
        publication= (EditText) findViewById(R.id.txtEditPublication);
        email= (EditText) findViewById(R.id.txtEditImage);

        databaseUser= FirebaseDatabase.getInstance().getReference();
        // Note: that we do not want to lose the state if the activity is being
        // recreated
        if (savedInstanceState == null) {
            if (book_id != -1) {
                BooksDatabase.getBooks(book_id, books -> {
                    ((EditText) findViewById(R.id.txtEditTitle)).setText(books.title);
                    ((EditText) findViewById(R.id.txtEditAuthor)).setText(books.author);
                    ((EditText) findViewById(R.id.txtEditCondition)).setText(books.condition);
                    ((EditText) findViewById(R.id.txtEditPublication)).setText(books.publication);
                    ((EditText) findViewById(R.id.txtEditImage)).setText(books.email);

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

    private void UpdateData(Books books)
    {


        databaseUser.child("books").child(timestamp).removeValue();

        databaseUser.child("books").child(books.date_modified).setValue(books).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Book details updated",Toast.LENGTH_SHORT).show();
                        }


                    }
                });




    }
    private void InsertData()
    {
        String title1=title.getText().toString();
        String author1=authorb.getText().toString();
        String condition1=condition.getText().toString();
        String publication1=publication.getText().toString();
        String email1 = email.getText().toString();
        //String id = databaseUser.push().;

        Books book=new Books(book_id == -1?0:book_id,title1,author1,condition1,publication1,email1,
                String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));

        databaseUser.child("books").child(book.date_modified).setValue(book).
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Book Details Inserted",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    private void updateDatabase() {
        Books books = new Books(book_id == -1?0:book_id,
                ((EditText) findViewById(R.id.txtEditTitle)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditAuthor)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditCondition)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditPublication)).getText().toString(),
                ((EditText) findViewById(R.id.txtEditImage)).getText().toString(),
                String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())));
        if (book_id == -1) {
            BooksDatabase.insert(books);
            InsertData();
        } else {

            BooksDatabase.update(books);
            UpdateData(books);
        }
        finish(); // Quit activity
    }

    public void deleteRecord() {
        BooksDatabase.delete(book_id);
        deletefirebase(timestamp);

    }
    private void deletefirebase(String timestamp) {
        // creating a variable for our Database
        // Reference for Firebase.
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("books");
        // we are use add listerner
        // for event listener method
        // which is called with query.
        Query query=dbref.orderByChild("date_modified").equalTo(timestamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // remove the value at reference
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
