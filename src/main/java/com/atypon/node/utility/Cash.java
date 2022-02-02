package com.atypon.node.utility;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;

public class Cash implements Utility {
    protected static final HashMap<String, List<JsonNode>> cashMap = new HashMap<>();

    private Cash() {
    }

    public static boolean isCashed(String property){
        return cashMap.containsKey(property);
    }

    public static void cashProperty(String property, List<JsonNode> listOfNodes){
        if (isCashed(property)){
            return;
        }
        cashMap.put(property,listOfNodes);
    }

    public static List<JsonNode> getCashedValue(String property){
        return cashMap.get(property);
    }

    public static void flushCash(){
        cashMap.clear();
    }

}
