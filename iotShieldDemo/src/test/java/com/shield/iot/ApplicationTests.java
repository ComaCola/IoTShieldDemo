package com.shield.iot;

import com.shield.iot.facade.IProfileFacade;
import com.shield.iot.facade.IRequestFacade;
import com.shield.iot.facade.IResponseFacade;
import com.shield.iot.model.Profile;
import com.shield.iot.model.Request;
import com.shield.iot.model.Response;
import com.shield.iot.model.ResponseForDevice;
import com.shield.iot.model.ResponseForRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

  @Autowired
  private IRequestFacade requestFacade;
  @Autowired
  private IProfileFacade profileFacade;

  @Autowired
  private IResponseFacade responseFacade;

  @Test
  public void contextLoads() {
    Assert.notNull(requestFacade, "requestFasade is not null");
    Assert.isTrue(profileFacade != null, "profileFacade is not null");
    Assert.notNull(responseFacade, "response facade is not null");
  }

  /**
   * Clears all data - profiles, requests, responses.s
   */
  private void clearAllStorageData() {
    profileFacade.removeAll();
    requestFacade.removeAll();
    responseFacade.removeAll();
  }

  /**
   * Only one request is analyzed. No any profile, so this one request should be
   * blocked and device quarantined.
   */
  @Test
  public void requestAndNoAnyProfileTest() {
    clearAllStorageData();

    Request request = Request.builder()
            .deviceId("111dev")
            .modelName("Apple")
            .requestId("111req")
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .url("delfi.lt")
            .build();
    responseFacade.analyze(request);
    List<Response> list = responseFacade.loadAll();
    analyseBadRequest(list);
  }

  private void analyseBadRequest(List<Response> list) {
    Assert.notNull(list, "response list is not null");
    Assert.notEmpty(list, "response list is not empty");
    Assert.isTrue(list.size() == 2, "response list has two items - for blocked request and quarantined device");

    Response response1 = list.get(0); // first item is block for request
    Response response2 = list.get(1); // second item is quarantine for device
    Assert.isInstanceOf(ResponseForRequest.class, response1);
    Assert.isInstanceOf(ResponseForDevice.class, response2);
    Assert.isTrue(response1.getAction().equals("block"), "response is blocked");
    Assert.isTrue(response2.getAction().equals("quarantine"), "response is blocked");
  }

  /**
   * Profile registered. Request analyzed with block action and device
   * quarantine. Profile updated thah bloecked request become allowed.
   */
  @Test
  public void analyzeBlockUpdateProfileTest() {
    clearAllStorageData();

    Profile profile = Profile.builder()
            .modelName("samsung")
            .defaultPolicy("block")
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .whiteList(Arrays.asList(new String[]{"delfi.lt"}))
            .blackList(Arrays.asList(new String[]{}))
            .build();
    profileFacade.create(profile);  // save profile to storage

    Request request = Request.builder()
            .deviceId("samung123")
            .modelName("samsung")
            .requestId("samsung_request_123")
            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
            .url("15min.lt")
            .build();
    requestFacade.save(request);  // save request to storage
    responseFacade.analyze(request);  // analyze request

    List<Response> list = responseFacade.loadAll(); // load responses
    analyseBadRequest(list);                        // make test for list of two items

    // profile update with new url for whitelist
    Profile profileUpdate = Profile.builder()
            .blackList(Arrays.asList(new String[]{"yandex.ru"}))
            .whiteList(Arrays.asList(new String[]{"15min.lt", "delfi.lt"}))
            .modelName("samsung")
            .build();
    profileFacade.update(profileUpdate);    // update profile
    responseFacade.analyzeAll(requestFacade.loadAll()); // reanalyze all requestes

    list = responseFacade.loadAll();  // new responses list

    Assert.notNull(list, "list is not null");
    Assert.notEmpty(list, "list is not empty");

    Response response = list.get(0);
    Assert.isInstanceOf(ResponseForRequest.class, response);
    Assert.isTrue(response.getAction().equals("allow"), "request must be allowed");

  }

}
