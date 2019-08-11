package com.shield.iot.dao;

import com.shield.iot.model.Request;
import java.util.List;

public interface IRequestDao {

  void save(Request request);

  List<Request> loadAll();

  void removeAll();
}
