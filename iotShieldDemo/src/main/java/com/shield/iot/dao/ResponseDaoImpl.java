package com.shield.iot.dao;

import com.shield.iot.model.Response;
import com.shield.iot.model.ResponseForDevice;
import com.shield.iot.model.ResponseForRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ResponseDaoImpl implements IResponseDao {

  // For data storage.
  private Map<String, Response> responseMap = new LinkedHashMap<>();

  @Override
  public void save(Response response) {
    String id = response instanceof ResponseForRequest
            ? ((ResponseForRequest) response).getRequestId()
            : ((ResponseForDevice) response).getDeviceId();
    responseMap.put(id, response);
  }

  @Override
  public List<Response> loadAll() {
    return new ArrayList<>(responseMap.values());
  }

  @Override
  public void removeAll() {
    responseMap.clear();
  }

}
