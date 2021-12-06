package com.agenatech.test.vetathome.utils;

import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import org.springframework.stereotype.Service;

@Service
public class UriUtils {

    public String getIdFromLink(String link){
        return link.substring(link.lastIndexOf("/") +1);
    }

    public String getSelfLinkFromUser(UserProfile profile){
        return profile.get_links().getSelf().get("href");
    }

    public String getPetLinkFromPet(PetProfile profile){
        return profile.get_links().getPet().get("href");
    }
}
