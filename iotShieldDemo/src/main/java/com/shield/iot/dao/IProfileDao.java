package com.shield.iot.dao;

import com.shield.iot.model.Profile;
import java.util.List;

public interface IProfileDao {

  Profile load(String modelName);

  List<Profile> loadAll();

  void create(Profile profile);

  void update(Profile profile);

  void removeAll();
}
