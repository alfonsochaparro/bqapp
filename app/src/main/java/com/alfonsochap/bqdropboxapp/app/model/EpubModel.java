package com.alfonsochap.bqdropboxapp.app.model;

import com.dropbox.client2.DropboxAPI.Entry;

import nl.siegmann.epublib.domain.Book;

/**
 * Created by Alfonso on 15/12/2015.
 */
public class EpubModel {

    Entry entry;
    Book book;

    public EpubModel(Entry entry, Book book) {
        this.entry = entry;
        this.book = book;
    }

    public Entry getEntry() { return entry; }
    public Book getBook() { return book; }

    public void setEntry(Entry entry) { this.entry = entry; }
    public void setBook(Book book) { this.book = book; }

}
