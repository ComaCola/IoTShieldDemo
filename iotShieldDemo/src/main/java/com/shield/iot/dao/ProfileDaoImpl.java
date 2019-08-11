package com.shield.iot.dao;

import com.shield.iot.model.Profile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * For profile CRUD operations. For storage too.
 */
@Repository
public class ProfileDaoImpl implements IProfileDao {

  // data storage
  private Map<String, Profile> profileMap = new LinkedHashMap<>();

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
      if (profile.getWhiteList() != null) {
        p.setWhiteList(profile.getWhiteList());
      }
      if (profile.getBlackList() != null) {
        p.setBlackList(profile.getBlackList());
      }
    }
  }

  @Override
  public void removeAll() {
    profileMap.clear();
  }

}
