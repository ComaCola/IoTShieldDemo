package com.shield.iot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shield.iot.facade.IProfileFacade;
import com.shield.iot.facade.IRequestFacade;
import com.shield.iot.model.Request;
import com.shield.iot.model.Response;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shield.iot.facade.IResponseFacade;
import com.shield.iot.utils.Utils;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class InputController {

  private static final String INPUT_FILE = "input.json";

  private final IProfileFacade profileFacade;

  private final IResponseFacade responseFacade;

  private final IRequestFacade requestFacade;

  @Autowired
  public InputController(IProfileFacade profileFacade,
          IResponseFacade requestResponseFacade,
          IRequestFacade requestFacade) {
    this.profileFacade = profileFacade;
    this.responseFacade = requestResponseFacade;
    this.requestFacade = requestFacade;
  }

  /**
   * App takes data from file.
   *
   * @return
   */
  @RequestMapping(method = RequestMethod.GET, value = "/fromFile")
  public String analyzeFromFile() {

    List<String> list = null;
    try {
      list = Utils.readLinesFromFile(this.getClass(), INPUT_FILE);
    } catch (FileNotFoundException e) {
      return "no file found";
    }
    if (list == null) {
      return "no file found";
    }
    return analize(list);
  }

  /**
   * First analyzing all request in consistently, second analyzing all profiles
   * are collected, then all requests are analyzed.`
   *
   * @param list
   * @return
   */
  private String analize(List<String> list) {
    // ananlyze serial
    requestFacade.analyzeAllSerial(list);
    String statisticSerial = responseFacade.printStatistics();
    System.out.println(statisticSerial);

    // clearing all data
    resetAppData();

    // analyzing - first creating/updating profile, then analyzing all requests
    requestFacade.analyzeAllFirstProfileSecondRequest(list);
    String statisticFirstProfileSecondRequest = responseFacade.printStatistics();
    System.out.println("Data after profile collection and requests analyzing");
    System.out.println(statisticSerial);

    return statisticSerial.replaceAll("\n", "<br>") + "<br><br>Data after profile updates<br>"
            + statisticFirstProfileSecondRequest.replaceAll("\n", "<br>"); // for HTML
  }

  /**
   * Data is in simple plain text format.
   *
   * @param plainText Plain text, e. g. {model_name:"a"}{model_name:"b"}
   * @return
   */
  @PostMapping(value = "/fromPost")
  public String analyzeFromPost(@RequestBody String plainText) {
    List<String> list = Arrays.asList(plainText.split("}"));
    list = list.stream().map(item -> item + "}").collect(Collectors.toList());
    return analize(list);
  }

  /**
   * Outputs all requests.
   *
   * @return
   * @throws JsonProcessingException
   */
  @GetMapping("/allRequests")
  public List<Request> allRequest() throws JsonProcessingException {
    return requestFacade.loadAll();
  }

  /**
   * Outputs all responses.
   *
   * @return
   * @throws JsonProcessingException
   */
  @GetMapping(value = "/allResponses", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Response>> allRespons() throws JsonProcessingException {
    return new ResponseEntity<List<Response>>(responseFacade.loadAll(), HttpStatus.OK);
  }

  /**
   * Application data reset.
   *
   * @return
   */
  @GetMapping("/reset")
  public String resetAppData() {
    profileFacade.removeAll();
    requestFacade.removeAll();
    responseFacade.removeAll();
    return "Data reseted";
  }
}
