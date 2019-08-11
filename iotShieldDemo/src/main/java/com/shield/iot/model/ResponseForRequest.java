package com.shield.iot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ResponseForRequest extends Response implements Serializable {

  @JsonProperty(value = "request_id")
  private String requestId;
}
