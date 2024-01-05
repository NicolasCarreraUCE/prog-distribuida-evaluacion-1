package com.distribuida.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class JPAConfig {
    private EntityManagerFactory entityManagerFactory;
    @PostConstruct
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("pu-distribuida");
    }
    @Produces
    public EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }
}