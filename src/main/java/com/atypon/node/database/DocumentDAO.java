package com.atypon.node.database;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.atypon.node.utility.PropertyIndex.indexName;
import static com.atypon.node.utility.PropertyIndex.indexSchema;

@Repository
@Profile("database")
public class DocumentDAO implements DAOInterface {

  private static final HashMap<String, JsonNode> DB2 = new HashMap<>();

  private static final DocumentDAO dao = new DocumentDAO();

  @Autowired
  private DocumentDAO() {}

  public static DocumentDAO getInstance() {
    return dao;
  }

  @Override
  public void receiveDataFromApiAndIndex(List<JsonNode> list) {
    for (JsonNode j : list) {
      storeJson(j);
    }
    indexName();
    indexSchema();
  }

  @Override
  public void storeJson(JsonNode... jsons) {
    for (JsonNode j : jsons) {
      DB2.put(j.get("id").asText(), j);
    }
    indexName();
    indexSchema();
  }

  @Override
  public List<JsonNode> retrieveAll() {
    return new ArrayList<>(DB2.values());
  }

  @Override
  public JsonNode findById(String id) {
    return DB2.get(id);
  }
}
