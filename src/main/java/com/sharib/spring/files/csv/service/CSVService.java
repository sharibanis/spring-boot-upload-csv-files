package com.sharib.spring.files.csv.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sharib.spring.files.csv.helper.CSVHelper;
import com.sharib.spring.files.csv.model.Tutorial;
import com.sharib.spring.files.csv.repository.TutorialRepository;

@Service
@Transactional
public class CSVService {
  @Autowired
  TutorialRepository repository;
  
  @Autowired
  private Environment env;
  
  @Transactional
  public void save(MultipartFile file) {
      Connection con = null;
    try {
      List<Tutorial> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
      //System.out.println("tutorials.size(): " + tutorials.size());
      Class.forName("com.mysql.cj.jdbc.Driver");  
      con = DriverManager.getConnection(
	  env.getProperty("spring.datasource.url"),
	  env.getProperty("spring.datasource.username"),env.getProperty("spring.datasource.password"));  
      Statement stmt = con.createStatement();  
      con.setAutoCommit(false);
      for (Tutorial tutorial : tutorials) {
     	  String id, login, name;
     	  BigDecimal salary;
    	  id = tutorial.getId();
    	  login = tutorial.getLogin();
    	  name = tutorial.getName();
    	  salary = tutorial.getSalary();
          String sql = "INSERT INTO employees VALUES ('" + id + "', '" + login +"', '" + name + 
        		  "', " + salary + ") "
            		+ "ON DUPLICATE KEY UPDATE login = '" + login +"', " 
            		+ "name = '" + name + "',  salary = " + salary ;
            
          //System.out.println("INSERTing: " + sql);
          stmt.executeUpdate(sql);
          con.commit();
          //System.out.println("INSERTed: " + sql);
      }
      con.close();  
    } catch (IOException e) {
        System.out.println(e);
        throw new RuntimeException("fail to store csv data: " + e.getMessage());
    } catch (SQLException e1) {
        System.out.println(e1);
        if (con != null) {
        	try {
        		System.err.print("Transaction is being rolled back.");
                con.rollback();
        	} catch (SQLException e2) {
                System.out.println(e2);
        	}
        }
      throw new RuntimeException("fail to store csv data: " + e1.getMessage());
    } catch (ClassNotFoundException e3) {
	      System.out.println(e3);
	    throw new RuntimeException("fail to store csv data: " + e3.getMessage());
	  }
  }

  public ByteArrayInputStream load() {
    List<Tutorial> tutorials = repository.findAll();

    ByteArrayInputStream in = CSVHelper.tutorialsToCSV(tutorials);
    return in;
  }

  public List<Tutorial> getAllTutorials() {
    return repository.findAll();
  }
}
