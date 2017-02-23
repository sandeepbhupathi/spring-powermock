package com.codecentric.sample.store.service.tools;

import java.io.IOException;
import java.util.List;

import com.codecentric.sample.store.model.Item;

public class StaticService {


    public static int getMultiplicator() {
        return 10;
    }

    public static String readFile(String fileName) throws IOException {

        // Read file here
        return "file content";
    }
    
    public static void sumValues(List<Item> itemList){
    	itemList.stream()
        .forEach(s -> s.setPriceInCents(s.getPriceInCents()*10));
    }
    
    public static void throwException(boolean testBoolean){
    	if(testBoolean)
    		throw new RuntimeException();
    }
}
