package com.example.booktradeapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.TimeUnit;

@Entity(tableName="books")
public class Books {

    public Books()
    {

    }
    public Books(int id, String title, String author, String condition, String publication, String email, /* byte[] image,*/ String date_modified) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.publication = publication;
        this.email = email;
        //this.image = image;
        this.date_modified = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCondition() {
        return condition;
    }

    public String getPublication() {
        return publication;
    }

    public String getEmail() {
        return email;
    }

    public String getDate_modified() {
        return date_modified;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "author")
    public String author;

    @ColumnInfo(name = "condition")
    public String condition;

    @ColumnInfo(name = "publication")
    public String publication;

    @ColumnInfo(name = "email")
    public String email;

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    public byte[] image;

    @ColumnInfo(name = "date_modified")
    public String date_modified;
}


