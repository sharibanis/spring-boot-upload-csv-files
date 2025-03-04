package com.sharib.spring.files.csv.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sharib.spring.files.csv.service.CSVService;
import com.sharib.spring.files.csv.helper.CSVHelper;
import com.sharib.spring.files.csv.message.ResponseMessage;
import com.sharib.spring.files.csv.model.Tutorial;

@CrossOrigin("http://localhost:8081")
@Controller
@RequestMapping("/api/csv")
public class CSVController {

  @Autowired
  CSVService fileService;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    System.out.println("Received file: "+ file.getOriginalFilename());
    if (CSVHelper.hasCSVFormat(file)) {
      try {
   	    System.out.println("Uploading CSV file: "+ file.getOriginalFilename());
        fileService.save(file);
        message = "Uploaded the file successfully: " + file.getOriginalFilename();
   	    System.out.println("Uploaded CSV file successfully: "+ file.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      } catch (Exception e) {
   	    System.out.println("Could not upload the CSV file: "+ file.getOriginalFilename());
        message = "Could not upload the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      }
    } else {
        System.out.println("Received file not CSV file: "+ file.getOriginalFilename());
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/files")
  public ResponseEntity<List<Tutorial>> getAllFiles() {
    try {
       System.out.println("Getting all files...");
      List<Tutorial> tutorials = fileService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
