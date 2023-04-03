package com.example.booktradeapp.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.concurrent.TimeUnit;

@Entity(tableName="books")
public class Books {

    public Books(int id, String title, String author, String condition, String publication, /* byte[] image,*/ String date_modified) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.condition = condition;
        this.publication = publication;
        //this.image = image;
        this.date_modified = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
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

//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
//    public byte[] image;

    @ColumnInfo(name = "date_modified")
    public String date_modified;
}


