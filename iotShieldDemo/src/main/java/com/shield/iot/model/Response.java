package com.shield.iot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

  private String responseType; // for request or device
  private String id;
  private String action;  // llow, block, quarantine

}
