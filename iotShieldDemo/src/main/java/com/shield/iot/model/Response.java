package com.shield.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class Response implements Serializable {

  @JsonProperty(value = "action")
  private String action;  // allow, block, quarantine
}
