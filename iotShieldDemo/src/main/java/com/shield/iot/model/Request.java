package com.shield.iot.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request implements Serializable {

  private String requestId;
  private String modelName;
  private String deviceId;
  private String url;
  private Date date;

}
