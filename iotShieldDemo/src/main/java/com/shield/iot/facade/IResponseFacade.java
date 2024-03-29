package com.shield.iot.facade;

import com.shield.iot.model.Request;
import com.shield.iot.model.Response;
import java.util.List;

public interface IResponseFacade {

  void analyze(Request request);

  void save(Response response);

  List<Response> loadAll();

  String printStatistics();

  void analyzeAll(List<Request> requestList);

  void removeAll();
}
