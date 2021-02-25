package com.cherniak.thymeleaf.repository;

import com.cherniak.thymeleaf.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

//    @Query("SELECT p FROM Product p WHERE p. cost = (SELECT MIN (p.cost) FROM Product p)")
//    List<Product> findAllByCostIsMin();
//
//    @Query("SELECT p FROM Product p WHERE p. cost = (SELECT MAX (p.cost) FROM Product p)")
//    List<Product> findAllByCostIsMax();
//
//    @Query("SELECT p FROM Product p WHERE p. cost = (SELECT MIN (p.cost) FROM Product p) " +
//            "OR p.cost = (SELECT MAX (p.cost) FROM Product p) ORDER BY p.cost")
//    List<Product> findAllByCostIsMinMax();
//
//    List<Product> getProductByCostGreaterThanEqualAndCostLessThanEqual(Integer min, Integer max, Sort sort);

}
