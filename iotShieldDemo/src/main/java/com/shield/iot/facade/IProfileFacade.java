package com.shield.iot.facade;

import com.shield.iot.model.Profile;
import java.util.List;

public interface IProfileFacade {

  Profile load(String modelName);

  List<Profile> loadAll();

  void create(Profile profile);

  void update(Profile profile);

}
