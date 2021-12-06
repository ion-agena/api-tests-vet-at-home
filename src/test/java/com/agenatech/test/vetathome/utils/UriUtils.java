package com.agenatech.test.vetathome.utils;

import org.springframework.stereotype.Service;

@Service
public class UriUtils {

    public String getIdFromLink(String link){
        return link.substring(link.lastIndexOf("/") +1);
    }


}
