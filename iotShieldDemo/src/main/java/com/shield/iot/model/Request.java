package com.shield.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Request implements Serializable {

  @JsonProperty(value = "request_id")
  private String requestId;
  @JsonProperty(value = "model_name")
  private String modelName; // by which profile to analyze
  @JsonProperty(value = "device_id")
  private String deviceId;
  @JsonProperty(value = "url")
  private String url;       // device's generated url
  @JsonProperty(value = "timestamp")
  private Timestamp timestamp;
}
