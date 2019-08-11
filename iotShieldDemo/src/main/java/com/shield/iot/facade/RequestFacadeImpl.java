package com.shield.iot.facade;

import com.shield.iot.dao.IRequestDao;
import com.shield.iot.model.Request;
import com.shield.iot.utils.BuilderUtils;
import java.util.List;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestFacadeImpl implements IRequestFacade {

  private final IRequestDao requestDao;

  private final IProfileFacade profileFacade;

  private final IResponseFacade responseFacade;

  @Autowired
  public RequestFacadeImpl(IRequestDao requestDao, IProfileFacade profileFacade,
          IResponseFacade responseFacade) {
    this.requestDao = requestDao;
    this.profileFacade = profileFacade;
    this.responseFacade = responseFacade;
  }

  @Override
  public void save(Request request) {
    requestDao.save(request);
  }

  @Override
  public List<Request> loadAll() {
    return requestDao.loadAll();
  }

  @Override
  public void removeAll() {
    requestDao.removeAll();
  }

  /**
   * Analyzing item by item from plain text list.
   *
   * @param list
   */
  @Override
  public void analyzeAllSerial(List<String> list) {
    JSONObject o;
    JSONParser p = new JSONParser();
    for (String item : list) {
      try {
        o = (JSONObject) p.parse(item);
        analyzeRequest(o);
      } catch (ParseException | NumberFormatException | ClassCastException e) {
        e.printStackTrace();
      }
    }

  }

  /**
   * First registering all profiles data, then analyzing all requests.
   *
   * @param list
   */
  @Override
  public void analyzeAllFirstProfileSecondRequest(List<String> list) {
    JSONObject o;
    JSONParser p = new JSONParser();
    for (String item : list) {
      try {
        o = (JSONObject) p.parse(item);
        registerData(o);
      } catch (ParseException | NumberFormatException | ClassCastException e) {
        e.printStackTrace();
      }
    }
    responseFacade.analyzeAll(loadAll());
  }

  /**
   * Registering profile's data or analyzing request.
   *
   * @param o
   */
  private void analyzeRequest(JSONObject o) {
    switch (o.get("type").toString()) {

      case "profile_create":
        profileFacade.create(BuilderUtils.buildProfile(o));
        break;

      case "profile_update":
        profileFacade.update(BuilderUtils.buildProfile(o));
        break;

      case "request":
        Request request = BuilderUtils.buildRequest(o);
        save(request);  // every request's data is saved
        responseFacade.analyze(request);  // request analysis
        break;
    }
  }

  /**
   * Registers all data (profile creating/updating, requests saving), but no
   * analysis is done.
   *
   * @param o
   */
  private void registerData(JSONObject o) {
    switch (o.get("type").toString()) {

      case "profile_create":
        profileFacade.create(BuilderUtils.buildProfile(o));
        break;

      case "profile_update":
        profileFacade.update(BuilderUtils.buildProfile(o));
        break;

      case "request":
        save(BuilderUtils.buildRequest(o));  // every request's data is saved, but request is not analyzed
        break;
    }
  }
}
