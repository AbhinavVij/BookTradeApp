package com.example.booktradeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;


import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.booktradeapp.db.Books;
import com.example.booktradeapp.db.BooksDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyPage extends AppCompatActivity {

    ArrayList<Books> mylist;

    RecyclerView recycler;
    DatabaseReference databaseReference;
    MyAdapter myadapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BuyPage.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_page);

        Toolbar mytoolbar = findViewById(R.id.toolbar2);
        mytoolbar.setTitle("BUY");
        setSupportActionBar(mytoolbar);


        RecyclerView recyclerView = findViewById(R.id.books_buy);
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        mylist = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //myadapter=new MyAdapter(this,mylist);
        // myadapter=new MyAdapter(this,mylist);
        //recyclerView.setAdapter(myadapter);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Books book = dataSnapshot.getValue(Books.class);
                    mylist.add(book);
                }
                myadapter = new MyAdapter(BuyPage.this, mylist);
                recyclerView.setAdapter(myadapter);
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_book_search, menu);


        MenuItem searchViewItem = menu.findItem(R.id.search_bar);


        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setQueryHint("Search People");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myadapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setIconified(false);

        return super.onCreateOptionsMenu(menu);
    }

    public void displaySetup(Books books) {

            Bundle args = new Bundle();
            args.putInt("books_id", books.id);
            args.putString("title", books.title);
            args.putString("author", books.author);
            args.putString("condition", books.condition);
            args.putString("publication", books.publication);


            BuyPage.DisplaySetupDialog setupDialog = new BuyPage.DisplaySetupDialog();
            setupDialog.setArguments(args);
            setupDialog.show(getSupportFragmentManager(), "setupDialog");

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

        Context context;
        ArrayList<Books> list;
        ArrayList<Books> listfull;

        public MyAdapter(Context context, ArrayList<Books> list) {
            this.context = context;
            this.listfull = list;
            this.list = new ArrayList<>(listfull);
        }


        private final Filter booksfilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Books> temp = new ArrayList<Books>();
                if (constraint == null || constraint.length() == 0) {
                    temp.addAll(listfull);
                } else {
                    String queryText = constraint.toString().toLowerCase().trim();
                    for (Books item : listfull) {
                        // checking if the entered string matched with any item of our recycler view.
                        if (item.getTitle().toLowerCase().contains(queryText) ||
                                item.getPublication().toLowerCase().contains(queryText) ||
                                item.getAuthor().toLowerCase().contains(queryText)) {
                            // if the item is matched we are
                            // adding it to our filtered list.
                            temp.add(item);
                        }
                    }
                }
                FilterResults filterresult = new FilterResults();
                filterresult.values = temp;
                filterresult.count = temp.size();

                return filterresult;


            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list.clear();
                list.addAll((ArrayList) results.values);
                notifyDataSetChanged();
            }
        };

        @NonNull
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {

            Books book = list.get(position);

            holder.title.setText(book.getTitle());
            holder.author.setText(book.getAuthor());
            holder.publication.setText(book.getPublication());
            holder.condition.setText(book.getCondition());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public Filter getFilter() {
            return booksfilter;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView title, author, publication, condition;
            private Books books;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.textView6);
                author = itemView.findViewById(R.id.textView7);
                publication = itemView.findViewById(R.id.textView8);
                condition = itemView.findViewById(R.id.textView9);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       TextView a=v.findViewById(R.id.textView7);
                        TextView t=v.findViewById(R.id.textView6);
                        TextView p=v.findViewById(R.id.textView8);
                        TextView c=v.findViewById(R.id.textView9);

                        // Todo
                        displaySetup(new Books(1,t.getText().toString(),a.getText().toString(),
                                c.getText().toString()
                        ,p.getText().toString(),"",""));
                    }
                });

            }
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
                    .setMessage("Author of Book:" + author + "\nCondition of Book:" + condition + "\nBook published by :" + publication)
//                    .setMessage("Condition of Book:" + condition)
//                    .setMessage("Book published by :"+ publication)
                    .setPositiveButton("Add to Cart",
                            (dialog, id) -> {
                            })
                    .setNegativeButton("Cancel",
                            (dialog, id) -> {
                            });
            return builder.create();
        }
    }
}
