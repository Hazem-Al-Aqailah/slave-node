package com.atypon.node.network;

import com.atypon.node.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.lang.reflect.Type;
import java.util.List;

@Service
public class DatabaseReceiver implements NetworkInterface {

  static DocumentDAO dao = DocumentDAO.getInstance();

  private DatabaseReceiver() {}

  @PostConstruct
  public static void consume() {
    /**
     * works great with the database receiving
     * herokuURL
     * "https://master-node-atypon.herokuapp.com/api/documents";
     * localURL for the docker container
     * "http://master:8080/api/documents"; 
     */
    String localURL = "http://localhost:8080/api/documents";

    RestTemplate template = new RestTemplate();
    ResponseEntity<List<JsonNode>> data =
        template.exchange(
            localURL,
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<List<JsonNode>>() {
              @Override
              public Type getType() {
                return super.getType();
              }
            });
    List<JsonNode> list = data.getBody();
    try {
      dao.receiveDataFromApiAndIndex(list);
    } catch (Exception e) {
      System.out.println("error communicating with the server");
    }
  }
}
