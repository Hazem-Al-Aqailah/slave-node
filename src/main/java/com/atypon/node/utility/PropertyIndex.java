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

  protected static final Map<String, ArrayList<String>> dateIndex = new HashMap<>();

  public static void indexProperties() {
    indexName();
    indexSchema();
    indexDate();
  }

  private static void indexName() {
    addToIndexedHashMap(nameIndex, "author");
  }

  private static void indexSchema() {
    addToIndexedHashMap(schemaIndex, "schema");
  }

  private static void indexDate() {
    addToIndexedHashMap(dateIndex, "date");
  }

  private static void addToIndexedHashMap(
      Map<String, ArrayList<String>> propertyMap, String property) {
    propertyMap.clear();
    for (JsonNode j : dao.retrieveAll()) {
      try {
        String propertyValue = j.get(property).asText();
        String id = j.get("id").asText();
        if (propertyMap.containsKey(propertyValue)) {
          propertyMap.get(propertyValue).add(id);
        }
        addToListOfIndexedProperty(propertyValue, id, propertyMap);
      } catch (NullPointerException e) {

      }
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
      if (!itemsList.contains(item)) {
        itemsList.add(item);
      }
    }
  }
}
