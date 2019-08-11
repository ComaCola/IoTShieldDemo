package com.shield.iot.facade;

import com.shield.iot.dao.IResponseDao;
import com.shield.iot.model.Profile;
import com.shield.iot.model.Request;
import com.shield.iot.model.Response;
import com.shield.iot.model.ResponseForDevice;
import com.shield.iot.model.ResponseForRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Main business logic facade.
 */
@Service
public class ResponseFacadeImpl implements IResponseFacade {

  // for request/response logic - save or load good/bad requests
  private final IResponseDao responseDao;

  // for profile logic - create, update, load, etc.
  private final IProfileFacade profileFacade;

  @Autowired
  public ResponseFacadeImpl(IResponseDao responseDao, IProfileFacade profileFacade) {
    this.responseDao = responseDao;
    this.profileFacade = profileFacade;
  }

  /**
   * Request analysis. Requests are analyzed by profiles. Good requests are
   * registered as allowed. Bad requests are registered as blocked and devices
   * are quarantined.
   *
   * @param request
   */
  @Override
  public void analyze(Request request) {

    Profile profile = profileFacade.load(request.getModelName());

    // if profile does not exist, request is blocked, device quarantined
    if (profile == null) {
      block(request);
      return;
    }

    if (profile.getDefaultPolicy().equals("allow")) {
      // if url is not in black list, then, by default policy, url is allowed
      if (!profile.getBlackList().contains(request.getUrl())) {
        allow(request);
      } else {
        // if url is in black list, this request is blocked and device is quarantined
        block(request);
      }

    } else if (profile.getDefaultPolicy().equals("block")) {
      // if url is not in white list, then, by default policy, this url is blocked, device - quarantined
      if (!profile.getWhiteList().contains(request.getUrl())) {
        block(request);
      } else {
        allow(request);
      }
    }
  }

  /**
   * Request allowing. Request is allowing (data is registered to storage).
   * Quarantined device (if exists one) is removed from list.
   *
   * @param request Request object.
   */
  private void allow(Request request) {
    ResponseForRequest response = ResponseForRequest.builder()
            .action("allow")
            .requestId(request.getRequestId())
            .build();
    responseDao.save(response);
  }

  /**
   * Request blocking. Request is blocked (data registered to storage). Device,
   * generated request, is added to quarantine list.
   *
   * @param request Request object.
   */
  private void block(Request request) {
    ResponseForRequest response = ResponseForRequest.builder()
            .action("block")
            .requestId(request.getRequestId())
            .build();

    ResponseForDevice responseDevice = ResponseForDevice.builder()
            .action("quarantine")
            .deviceId(request.getDeviceId())
            .build();

    responseDao.save(response);
    responseDao.save(responseDevice);
  }

  @Override
  public void save(Response response) {
    responseDao.save(response);
  }

  @Override
  public List<Response> loadAll() {
    return responseDao.loadAll();
  }

  @Override
  public String printStatistics() {
    int allowed = 0, blocked = 0, quarantined = 0;
    for (Response resp : loadAll()) {
      switch (resp.getAction()) {
        case "allow":
          allowed++;
          break;
        case "block":
          blocked++;
          break;
        case "quarantine":
          quarantined++;
          break;
      }
    }

    return String.format("Allowed requests: %d\nBlocked requests: %d\nQuarantined devices: %d", allowed, blocked, quarantined);
  }

  @Override
  public void analyzeAll(List<Request> requestList) {
    for (Request request : requestList) {
      analyze(request);
    }
  }

  @Override
  public void removeAll() {
    responseDao.removeAll();
  }

}
