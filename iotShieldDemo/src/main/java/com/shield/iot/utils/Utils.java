package com.shield.iot.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

  /**
   * Return file reader.
   *
   * @return
   */
  private static BufferedReader getReader(Class clazz, String inputFile) {
    URL url = clazz.getResource("/" + inputFile);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File(url.toURI())));
    } catch (FileNotFoundException | URISyntaxException e) {
      e.printStackTrace();
    }
    return reader;
  }

  public static List<String> readLinesFromFile(Class clazz, String inputFile) throws FileNotFoundException {
    BufferedReader reader = getReader(clazz, inputFile);
    if (reader == null) {
      throw new FileNotFoundException();
    }

    List<String> list = new ArrayList<>();
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        list.add(line);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return list;
  }
}
