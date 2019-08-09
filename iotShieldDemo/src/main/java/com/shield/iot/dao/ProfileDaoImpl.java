package com.shield.iot.dao;

import com.shield.iot.model.Profile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileDaoImpl implements IProfileDao {

  private Map<String, Profile> profileMap = new HashMap<>();

  @Override
  public Profile load(String modelName) {
    return profileMap.get(modelName);
  }

  @Override
  public List<Profile> loadAll() {
    return new ArrayList(profileMap.values());
  }

  @Override
  public void create(Profile profile) {
    profileMap.put(profile.getModelName(), profile);
  }

  @Override
  public void update(Profile profile) {
    Profile p = profileMap.get(profile.getModelName());
    if (p == null) {
      create(profile);
    } else {
      profile.setWhiteList(profile.getWhiteList());
      profile.setBlackList(profile.getBlackList());
    }
  }

}
