package com.atypon.node.database;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface DAOInterface {

  void receiveDataFromApiAndIndex(List<JsonNode> list);

  List<JsonNode> retrieveAll();

  JsonNode findById(String id);
}
