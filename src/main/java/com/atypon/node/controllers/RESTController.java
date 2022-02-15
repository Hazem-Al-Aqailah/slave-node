package com.atypon.node.controllers;

import static com.atypon.node.utility.Cash.*;

import com.atypon.node.network.MasterCommunicator;
import com.atypon.node.utility.FindBy;
import com.atypon.node.database.DocumentDAO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RestController
@RequestMapping("/api/documents")
public class RESTController {

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

  @GetMapping("bydate/{date}")
  public ResponseEntity<List<JsonNode>> findByDate(@PathVariable String date) {
    List<JsonNode> nodes;
    if (isCashed(date)) {
      nodes = getCashedValue(date);
    } else {
      nodes = FindBy.date(date);
      if (nodes == null) {
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
      }
      cashProperty(date, nodes);
    }
    return ResponseEntity.ok(nodes);
  }

  @GetMapping("/{id}")
  public ResponseEntity<JsonNode> findById(@PathVariable String id) {
    try {
      JsonNode node = dao.findById(id);
      return ResponseEntity.ok(node);
    } catch (NullPointerException e) {
      e.printStackTrace();
      System.out.println("ID not found");
    }
    return new ResponseEntity(null, HttpStatus.NOT_FOUND);
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateData(@RequestBody String serverRequest) {
    return MasterCommunicator.nodeResponse(serverRequest);
  }
}
