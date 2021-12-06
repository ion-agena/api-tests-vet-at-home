package com.agenatech.test.vetathome;

import com.agenatech.test.vetathome.payload.response.AuthResponse;
import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import com.agenatech.test.vetathome.service.DataManager;
import com.agenatech.test.vetathome.service.GatewayService;
import com.agenatech.test.vetathome.service.KeycloakService;
import com.agenatech.test.vetathome.utils.UriUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.mediatype.hal.HalLinkDiscoverer;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class VetAtHomeApplicationTests {
	@Autowired
	private KeycloakService keycloakService;
	@Autowired
	private GatewayService gatewayService;
	@Autowired
	private DataManager dataManager;
	@Autowired
	private UriUtils uriUtils;

	private static final String DEFAULT_USER_ID = "04d89ae7-d834-4327-a769-44cfe445109b";




	@Test
	public void t1(){
		AuthResponse response = keycloakService.defaultLogin();
		log.debug("------- res {}", response.accessToken());
		assertTrue(true);
	}

	@Test
	public void getMe(){
		UserProfile myProfile = gatewayService.getMyProfile();
		log.debug("------- profile {}", myProfile);

		assertFalse(myProfile.getEmail().isEmpty());
	}

	@Test
	public void putProfile(){
		UserProfile generatedProfile = dataManager.generateUserProfile();

		log.debug("-------generated profile {}", generatedProfile);

		UserProfile retrievedProfile = gatewayService.putProfile(generatedProfile);

		log.debug("-------retrieved profile {}", retrievedProfile);

		assertTrue(retrievedProfile.getEmail().equals(generatedProfile.getEmail()));
	}

	@Test
	public void savePet(){
		PetProfile generatedPetProfile = dataManager.generatePetProfile();

		PetProfile retrievedPet = gatewayService.savePet(generatedPetProfile);
		log.debug("-------retrievedPet {}", retrievedPet);
//		todo check empty owners

		assertTrue(retrievedPet.getAvatarUrl().equals(generatedPetProfile.getAvatarUrl()));
	}


	@Test
	public void saveAndLink(){
		PetProfile generatedPetProfile = dataManager.generatePetProfile();

		PetProfile retrievedPet = gatewayService.saveAndLink(generatedPetProfile);
		log.debug("-------retrievedPet {}", retrievedPet);

		String petLink = retrievedPet.get_links().getPet().get("href");
		log.debug("-------href {}", petLink);

		String petId = uriUtils.getIdFromLink(petLink);

		log.debug("-------petId {}", petId);

		List<UserProfile> owners = gatewayService.searchProfilesByPetIds(Arrays.asList(petId)).get_embedded().getProfiles();

		boolean isOwnerPresent = owners.stream().map(owner -> owner.get_links().getSelf().get("href") )
				.map(link -> uriUtils.getIdFromLink(link)).collect(Collectors.toList())
				.contains(DEFAULT_USER_ID);


		assertTrue(isOwnerPresent);
	}

	@Test
	public void link(){
		PetProfile generatedPetProfile = dataManager.generatePetProfile();
		PetProfile retrievedPet = gatewayService.savePet(generatedPetProfile);

//		gatewayService.link(retrievedPet.getId().toString());

//		todo request and check the link

		assertTrue(true);
	}


}
