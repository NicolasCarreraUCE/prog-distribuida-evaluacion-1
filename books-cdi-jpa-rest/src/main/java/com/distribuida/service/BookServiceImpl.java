package com.distribuida.service;

import com.distribuida.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements IBookService {
    @Inject
    private EntityManager em;
    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b").getResultList();
    }

    @Override
    public void insert(Book book) {
        em.persist(book);
    }

    @Override
    public Book findById(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public Book update(Book book) {
        return em.merge(book);
    }

    @Override
    public boolean deleteById(Integer id) {
        Book book = findById(id);
        if (book != null) {
            em.remove(book);
            return true;
        }
        return false;
    }
}
