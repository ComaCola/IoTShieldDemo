package com.shield.iot.dao;

import com.shield.iot.model.Response;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RequestResponseDaoImpl implements IRequestResponseDao {

  private List<Response> responseList = new ArrayList<>();

  @Override
  public void save(Response response) {
    responseList.add(response);
  }

  @Override
  public List<Response> loadAll() {
    return responseList;
  }

}
