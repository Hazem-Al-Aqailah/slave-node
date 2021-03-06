package com.atypon.node.utility;

import com.atypon.node.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atypon.node.utility.PropertyIndex.*;

public class FindBy implements DataBaseUtility {
  static DocumentDAO dao = DocumentDAO.getInstance();

  private FindBy() {}

  public static List<JsonNode> name(String name) {
    return getJsonNodes(name, nameIndex);
  }

  public static List<JsonNode> schema(String schema) {
    return getJsonNodes(schema, schemaIndex);
  }

  public static List<JsonNode> date(String date) {
    return getJsonNodes(date, dateIndex);
  }

  // used to get the list of references for the methods above
  private static List<JsonNode> getJsonNodes(
      String indexedValue, Map<String, ArrayList<String>> indexedMap) {
    ArrayList<JsonNode> output = new ArrayList<>();
    try {
      ArrayList<String> idList = indexedMap.get(indexedValue);
      for (String i : idList) {
        output.add(dao.findById(i));
      }
      return output;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("no such schema or name");
    }
    return output;
  }
}
