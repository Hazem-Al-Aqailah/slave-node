package com.atypon.node.utility;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;

public class Cash implements DataBaseUtility {
  protected static final HashMap<String, List<JsonNode>> cashMap = new HashMap<>();

  private Cash() {}

  public static void cashProperty(String property, List<JsonNode> listOfNodes) {
    if (isCashed(property)) {
      return;
    }
    cashMap.put(property, listOfNodes);
  }

  public static boolean isCashed(String property) {
    return cashMap.containsKey(property);
  }

  public static List<JsonNode> getCashedValue(String property) {
    System.out.println("cash has been used");
    return cashMap.get(property);
  }

  public static void flushCash() {
    cashMap.clear();
  }
}
