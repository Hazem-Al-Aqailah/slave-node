package com.atypon.node.network;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

import static com.atypon.node.utility.Cash.flushCash;

@Service
public class MasterCommunicator implements NetworkUtility {

  private MasterCommunicator() {}

  @PostConstruct
  public static void sendPort() throws URISyntaxException {
    /**
     * does not work since the website won't receive the port of this machine's ip
     * HerokuURL:"https://master-node-atypon.herokuapp.com/api/documents/receiver" Docker network
     * URL:"http://master:8080/api/documents/receiver" local machine URL:
     * "http://localhost:8080/api/documents/receiver"
     */
    String URL = "http://localhost:8080/api/documents/receiver";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    URI uri = new URI(URL);
    HttpEntity<String> formEntity = new HttpEntity<>("sending port to the master", headers);
    RestTemplate template = new RestTemplate();
    template.exchange(uri, HttpMethod.POST, formEntity, String.class);
  }

  public static ResponseEntity<String> getResponseFromNode(String serverRequest) {
    if (serverRequest.equals("update")) {
      return updateResponse(serverRequest);
    } else if (serverRequest.equals("check")) {
      return healthCheckResponse(serverRequest);
    }
    return new ResponseEntity(null, HttpStatus.NOT_FOUND);
  }

  private static ResponseEntity<String> healthCheckResponse(String serverRequest) {
    return ResponseEntity.ok("im alive");
  }

  private static ResponseEntity<String> updateResponse(String serverRequest) {
    DatabaseReceiver.consume();
    flushCash();
    return ResponseEntity.ok("updated");
  }
}
