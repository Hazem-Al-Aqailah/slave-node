package com.atypon.node.utility;

import com.atypon.node.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropertyIndex implements DataBaseUtility {
  private PropertyIndex() {}

  static DocumentDAO dao = DocumentDAO.getInstance();

  protected static final Map<String, ArrayList<String>> schemaIndex = new HashMap<>();

  protected static final Map<String, ArrayList<String>> nameIndex = new HashMap<>();

  private static void indexName() {
    nameIndex.clear();
    for (JsonNode j : dao.retrieveAll()) {
      String name = j.get("author").asText();
      String id = j.get("id").asText();
      if (nameIndex.containsKey(name)) {
        nameIndex.get(name).add(id);
      }
      addToListOfIndexedProperty(name, id, nameIndex);
    }
  }

  private static void indexSchema() {
    schemaIndex.clear();
    for (JsonNode j : dao.retrieveAll()) {
      String schema = j.get("schema").asText();
      String id = j.get("id").asText();
      if (schemaIndex.containsKey(schema)) {
        schemaIndex.get(schema).add(id);
      }
      addToListOfIndexedProperty(schema, id, schemaIndex);
    }
  }

  /**
   * used in indexName and indexSchema, where it adds to the list if it exists or create one and add
   * it if it does not *
   */
  private static void addToListOfIndexedProperty(
          String indexedValue, String item, Map<String, ArrayList<String>> indexedMap) {
    ArrayList<String> itemsList = indexedMap.get(indexedValue);
    // if list does not exist create it
    if (itemsList == null) {
      itemsList = new ArrayList<>();
      itemsList.add(item);
      indexedMap.put(indexedValue, itemsList);
    } else {
      // add if item is not already in list
      if (!itemsList.contains(item)) itemsList.add(item);
    }
  }

  public static void indexProperties(){
    indexName();
    indexSchema();
  }


}
