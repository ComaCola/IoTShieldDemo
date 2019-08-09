package com.shield.iot.facade;

import com.shield.iot.dao.IProfileDao;
import com.shield.iot.model.Profile;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileFacadeImpl implements IProfileFacade {

  private final IProfileDao profileDao;

  @Autowired
  public ProfileFacadeImpl(IProfileDao profileDao) {
    this.profileDao = profileDao;
  }

  @Override
  public Profile load(String modelName) {
    return profileDao.load(modelName);
  }

  @Override
  public List<Profile> loadAll() {
    return profileDao.loadAll();
  }

  @Override
  public void create(Profile profile) {
    profileDao.create(profile);
  }

  @Override
  public void update(Profile profile) {
    profileDao.update(profile);
  }

}
