package com.preparing.job.interview.lesson5hibernate;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Dao<T, N> {

  private Class clazz;
  public static EntityManagerFactory factory;

  static {
    factory = CustomEntityFactory.getEntityFactory();
  }

  public Dao(Class clazz) {
    this.clazz = clazz;
  }

  @SuppressWarnings("unchecked")
  public T getById(N id) {
    EntityManager em = factory.createEntityManager();
    return (T) em.find(clazz, id);
  }

  public T create(T t) {
    EntityManager em = factory.createEntityManager();
    em.getTransaction().begin();
    em.persist(t);
    em.getTransaction().commit();
    return t;
  }

  public T update(T t) {
    EntityManager em = factory.createEntityManager();
    em.getTransaction().begin();
    T newT = em.merge(t);
    em.getTransaction().commit();
    return newT;
  }

  public void delete(T t) {
    EntityManager em = factory.createEntityManager();
    em.getTransaction().begin();
    em.remove(t);
    em.getTransaction().commit();
  }

  @SuppressWarnings("unchecked")
  public List<T> getAll() {
    String className = clazz.getName();
    EntityManager em = factory.createEntityManager();
    return em.createQuery("SELECT t FROM " + className + " t", clazz)
        .getResultList();
  }
}
