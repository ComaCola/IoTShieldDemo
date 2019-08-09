package com.shield.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    System.out.println("hello");
  }

  @RequestMapping
  public String ping() {
    return "service is running...";
  }

}
