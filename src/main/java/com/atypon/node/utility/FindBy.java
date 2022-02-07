package com.atypon.node.utility;

import com.atypon.node.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.atypon.node.utility.PropertyIndex.nameIndex;
import static com.atypon.node.utility.PropertyIndex.schemaIndex;

public class FindBy implements DataBaseUtility {
  static DocumentDAO dao = DocumentDAO.getInstance();

  private FindBy() {}

  public static List<JsonNode> name(String name) {
    return getJsonNodes(name, nameIndex);
  }

  public static List<JsonNode> schema(String schema) {
    return getJsonNodes(schema, schemaIndex);
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
      System.out.println("no such schema or name");
      System.out.println(e);
    }
    return output;
  }
}
