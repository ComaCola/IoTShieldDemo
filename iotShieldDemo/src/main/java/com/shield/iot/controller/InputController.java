package com.shield.iot.controller;

import com.shield.iot.facade.IProfileFacade;
import com.shield.iot.facade.IRequestFacade;
import com.shield.iot.utils.BuilderUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController("/input")
public class InputController {

  private final IProfileFacade profileFacade;

  private final IRequestFacade requestFacade;

  @Autowired
  public InputController(IProfileFacade profileFacade, IRequestFacade requestFacade) {
    this.profileFacade = profileFacade;
    this.requestFacade = requestFacade;
  }

  @GetMapping("/scan")
  public String fromFile() {
    URL url = this.getClass().getResource("/input2.json");
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File(url.toURI())));
    } catch (FileNotFoundException | URISyntaxException e) {
      e.printStackTrace();
    }
    if (reader == null) {
      return "input file is not found.";
    }

    String line;
    JSONObject o;
    JSONParser p = new JSONParser();
    try {
      while ((line = reader.readLine()) != null) {
        try {
          o = (JSONObject) p.parse(line);
          analyzeRequest(o);
        } catch (ParseException e) {
          System.out.println(e.getMessage());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("------INPUT----------------");
    profileFacade.loadAll().stream().forEach(item -> System.out.println(item));
    System.out.println("--------OUTPUT--------------");
    requestFacade.loadAll().stream().forEach(item -> System.out.println(item));
    System.out.println("----------------------");
    return "from input loaded";
  }

  @PostMapping(value = "/scan", params = "json", consumes = "application/json")
  public String fromPost(@RequestParam List<String> json) {
    try {
      JSONParser parser = new JSONParser();
      for (String jsonItem : json) {
        Object o = parser.parse(jsonItem);
        JSONObject jsonObject = (JSONObject) o;
        jsonObject.get("type");
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return "from post loaded";
  }

  @PostMapping(value = "/scan", params = "jsonlist", consumes = "application/json")
  public String fromPostList(@RequestParam List<String> json) {
    try {
      JSONParser parser = new JSONParser();
      for (String jsonItem : json) {
        Object o = parser.parse(jsonItem);
        JSONObject jsonObject = (JSONObject) o;
        jsonObject.get("type");
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return "from post loaded";
  }

  private void analyzeRequest(JSONObject o) {
    //System.out.println(o);
    if (o.get("type").equals("profile_create")) {
      profileFacade.create(BuilderUtils.buildProfile(o));
    } else if (o.get("type").equals("profile_update")) {
      profileFacade.update(BuilderUtils.buildProfile(o));
    } else if (o.get("type").equals("request")) {
      requestFacade.analyze(BuilderUtils.buildRequest(o));
    }
  }

}
