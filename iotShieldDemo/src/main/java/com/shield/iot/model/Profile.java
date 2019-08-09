package com.shield.iot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile implements Serializable {

  private String modelName;
  private String defaultPolicy;
  private List<String> whiteList;
  private List<String> blackList;
  private Date date;

}
