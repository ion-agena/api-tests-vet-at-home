package com.agenatech.test.vetathome;

import com.agenatech.test.vetathome.payload.response.AuthResponse;
import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import com.agenatech.test.vetathome.service.DataManager;
import com.agenatech.test.vetathome.service.GatewayService;
import com.agenatech.test.vetathome.service.KeycloakService;
import com.agenatech.test.vetathome.utils.UriUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	@Value("${test.test-user-id}")
	private String DEFAULT_USER_ID;

	@BeforeAll
	void createDefaultProfile(){
		UserProfile generatedProfile = dataManager.generateUserProfile();
		log.debug("-------generated profile {}", generatedProfile);

		try {
			UserProfile retrievedProfile = gatewayService.putProfile(DEFAULT_USER_ID, generatedProfile);
		} catch (Exception exception){
			log.debug("user already exists");
		}
	}


	@Test
	public void t1(){
		AuthResponse response = keycloakService.defaultLogin();
		log.debug("------- res {}", response.accessToken());
		assertFalse(response.accessToken().isEmpty());
	}

	@Test
	public void getMe(){
		UserProfile myProfile = gatewayService.getMyProfile();
		log.debug("------- profile {}", myProfile);

		assertTrue(DEFAULT_USER_ID.equals(uriUtils.getIdFromLink(uriUtils.getSelfLinkFromUser(myProfile))));
	}

	@Test
	public void putProfile(){
		UserProfile generatedProfile = dataManager.generateUserProfile();
		log.debug("-------generated profile {}", generatedProfile);

		UserProfile retrievedProfile = gatewayService.putProfile(generatedProfile.getAvatarUrl(), generatedProfile);
		log.debug("-------retrieved profile {}", retrievedProfile);

		assertTrue(retrievedProfile.getEmail().equals(generatedProfile.getEmail()));
	}



	@Test
	public void saveAndLink(){
		PetProfile generatedPetProfile = dataManager.generatePetProfile();

		PetProfile retrievedPet = gatewayService.saveAndLink(generatedPetProfile);
		log.debug("-------retrievedPet {}", retrievedPet);

		String petLink = uriUtils.getPetLinkFromPet(retrievedPet);
				log.debug("-------href {}", petLink);

		String petId = uriUtils.getIdFromLink(petLink);

		log.debug("-------petId {}", petId);

		UserProfile owner = gatewayService.getThePetOwner(petId);
		String ownerLink = uriUtils.getSelfLinkFromUser(owner);

		assertTrue(DEFAULT_USER_ID.equals(uriUtils.getIdFromLink(ownerLink)));
	}


}
