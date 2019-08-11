package com.shield.iot.dao;

import com.shield.iot.model.Request;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class RequestDaoImpl implements IRequestDao {

  // Data storage
  private Map<String, Request> requestMap = new LinkedHashMap<>();

  @Override
  public void save(Request request) {
    requestMap.put(request.getRequestId(), request);
  }

  @Override
  public List<Request> loadAll() {
    return new ArrayList<>(requestMap.values());
  }

  @Override
  public void removeAll() {
    requestMap.clear();
  }

}
