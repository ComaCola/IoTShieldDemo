package com.shield.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Profile implements Serializable {

  @JsonProperty(value = "model_name")
  private String modelName;
  @JsonProperty(value = "default")
  private String defaultPolicy;   // allow, block
  @JsonProperty(value = "white_list")
  private List<String> whiteList; // urls
  @JsonProperty(value = "black_list")
  private List<String> blackList; // urls
  @JsonProperty(value = "timestamp")
  private Timestamp timestamp;
}
