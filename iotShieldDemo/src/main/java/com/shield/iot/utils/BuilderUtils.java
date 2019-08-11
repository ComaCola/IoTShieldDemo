package com.shield.iot.utils;

import com.shield.iot.model.Profile;
import com.shield.iot.model.Request;
import java.sql.Timestamp;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class BuilderUtils {

  public static Profile buildProfile(JSONObject o) {
    return Profile.builder()
            .modelName((String) o.get("model_name"))
            .defaultPolicy((String) o.get("default"))
            .whiteList((JSONArray) o.get("whitelist"))
            .blackList((JSONArray) o.get("blacklist"))
            .timestamp(new Timestamp(Long.parseLong(o.get("timestamp").toString())))
            .build();
  }

  public static Request buildRequest(JSONObject o) {
    return Request.builder()
            .modelName((String) o.get("model_name"))
            .deviceId((String) o.get("device_id"))
            .requestId((String) o.get("request_id"))
            .timestamp(new Timestamp(Long.parseLong(o.get("timestamp").toString())))
            .url((String) o.get("url"))
            .build();
  }
}
