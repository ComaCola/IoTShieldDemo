package com.shield.iot.facade;

import com.shield.iot.dao.IRequestResponseDao;
import com.shield.iot.model.Profile;
import com.shield.iot.model.Request;
import com.shield.iot.model.Response;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestFacadeImpl implements IRequestFacade {

  private final IRequestResponseDao dao;

  private final IProfileFacade profileFacade;

  @Autowired
  public RequestFacadeImpl(IRequestResponseDao dao, IProfileFacade profileFacade) {
    this.dao = dao;
    this.profileFacade = profileFacade;
  }

  @Override
  public void analyze(Request request) {
    System.out.println(request.getModelName());
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

  private void allow(Request request) {
    Response response = Response.builder()
            .action("allow")
            .id(request.getRequestId())
            .responseType("request")
            .build();
    dao.save(response);
  }

  private void block(Request request) {
    Response response = Response.builder()
            .action("block")
            .id(request.getRequestId())
            .responseType("request")
            .build();
    Response responseDevice = Response.builder()
            .action("quarantine")
            .id(request.getDeviceId())
            .responseType("device")
            .build();
    dao.save(response);
    dao.save(responseDevice);
  }

  @Override
  public void save(Response response) {
    dao.save(response);
  }

  @Override
  public List<Response> loadAll() {
    return dao.loadAll();
  }

}
