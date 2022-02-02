package com.atypon.node.controllers;

import static com.atypon.node.utility.Cash.*;

import com.atypon.node.utility.FindBy;
import com.atypon.node.database.DocumentDAO;
import com.atypon.node.network.DatabaseReceiver;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.env.Environment;

import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController
@RequestMapping("/api/documents")
public class RESTController {

  @Autowired public Environment env; // Environment Object containts the port number
  private final DocumentDAO dao = DocumentDAO.getInstance();

  @GetMapping
  public List<JsonNode> all() {
    return dao.retrieveAll();
  }

  @GetMapping("byname/{name}")
  public ResponseEntity<List<JsonNode>> findByName(@PathVariable String name) {
    List<JsonNode> nodes;
    if (isCashed(name)) {
      nodes = getCashedValue(name);
    } else {
      nodes = FindBy.name(name);
      if (nodes == null) {
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
      }
      cashProperty(name, nodes);
    }
    return ResponseEntity.ok(nodes);
  }

  @GetMapping("byschema/{schema}")
  public ResponseEntity<List<JsonNode>> findBySchema(@PathVariable String schema) {
    List<JsonNode> nodes;
    if (isCashed(schema)) {
      nodes = getCashedValue(schema);
    } else {
      nodes = FindBy.schema(schema);
      if (nodes == null) {
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
      }
      cashProperty(schema, nodes);
    }
    return ResponseEntity.ok(nodes);
  }

  //  private ResponseEntity<List<JsonNode>> getListResponseEntity(@PathVariable String property) {
  //    List<JsonNode> nodes;
  //    if (isCashed(property)) {
  //      nodes = getCashedValue(property);
  //    } else {
  //      nodes = FindBy.name(property);
  //      if (nodes == null) {
  //        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
  //      }
  //      cashProperty(property,nodes);
  //    }
  //    return ResponseEntity.ok(nodes);
  //  }

  @GetMapping("/{id}")
  public ResponseEntity<JsonNode> findById(@PathVariable String id) {
    JsonNode node = dao.findById(id);
    if (node == null) {
      return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(node);
  }

  @GetMapping("/status")
  public ResponseEntity<String> status() {
    return ResponseEntity.ok(env.getProperty("local.server.port"));
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateData(@RequestBody String serverRequest) {
    try {
      if (serverRequest.equals("update")) {
        DatabaseReceiver.consume();
        flushCash();
        return ResponseEntity.ok("updated");
      } else if (serverRequest.equals("check")) {
        return ResponseEntity.ok("im alive");
      }
    } catch (Exception e) {
      System.out.println("error communicating with the server" + e);
    }
    return new ResponseEntity(null, HttpStatus.NOT_FOUND);
  }
}
