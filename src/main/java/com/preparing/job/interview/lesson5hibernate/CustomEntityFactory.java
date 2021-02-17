package com.preparing.job.interview.lesson5hibernate;

import javax.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;

public class CustomEntityFactory {

  private CustomEntityFactory() {}

  public static final EntityManagerFactory entityFactory;

  static {
    entityFactory = new Configuration()
        .configure("hibernate.cfg.xml")
        .buildSessionFactory();
  }

  public static EntityManagerFactory getEntityFactory() {
    return entityFactory;
  }
}
