package com.distribuida.service;

import com.distribuida.model.Book;

import java.util.List;

public interface IBookService {
    List<Book> findAll();
    void insert(Book book);
    Book findById(Integer id);
    Book update(Book book);
    boolean deleteById(Integer id);
}
