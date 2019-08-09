package com.shield.iot.dao;

import com.shield.iot.model.Response;
import java.util.List;

public interface IRequestResponseDao {

  void save(Response response);

  List<Response> loadAll();

}
