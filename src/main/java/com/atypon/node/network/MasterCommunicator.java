package com.atypon.node.network;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class MasterCommunicator implements NetworkInterface {

  private MasterCommunicator() {}

  @PostConstruct
  public static void sendPort() throws URISyntaxException {
    // does not work since the website won't receive the port of this machine's ip
    // String herokuURL = "https://master-node-atypon.herokuapp.com/api/documents/receiver";
//    String localURL = "http://master:8080/api/documents/receiver";
    String localURL = "http://localhost:8080/api/documents/receiver";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    URI uri = new URI(localURL);
    HttpEntity<String> formEntity = new HttpEntity<>("sending port to the master", headers);
    RestTemplate template = new RestTemplate();
    template.exchange(uri, HttpMethod.POST, formEntity, String.class);
  }
}
