package com.shield.iot.facade;

import com.shield.iot.model.Request;
import java.util.List;

public interface IRequestFacade {

  void save(Request request);

  List<Request> loadAll();

  void removeAll();

  void analyzeAllSerial(List<String> list);

  void analyzeAllFirstProfileSecondRequest(List<String> list);
}
