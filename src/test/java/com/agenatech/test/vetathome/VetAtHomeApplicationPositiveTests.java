package com.agenatech.test.vetathome;

import com.agenatech.test.vetathome.payload.response.PetProfile;
import com.agenatech.test.vetathome.payload.response.UserProfile;
import com.agenatech.test.vetathome.service.DataManager;
import com.agenatech.test.vetathome.service.GatewayService;
import com.agenatech.test.vetathome.service.KeycloakService;
import com.agenatech.test.vetathome.utils.UriUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.UUID;

import static com.agenatech.test.vetathome.config.Constants.DEFAULT_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class VetAtHomeApplicationPositiveTests {
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
	public void getMe(){
		UserProfile myProfile = gatewayService.getDefaultProfile();
		log.debug("------- profile {}", myProfile);

		assertTrue(DEFAULT_USER_ID.equals(uriUtils.getIdFromLink(uriUtils.getSelfLinkFromUser(myProfile))));
	}

	@Test
	public void putProfile(){
		String email = UUID.randomUUID() + "@mail.com";
		keycloakService.signup(email, DEFAULT_PASSWORD);
		UserProfile generatedProfile = dataManager.generateUserProfile(email);

		gatewayService.putProfile(email, generatedProfile);

		UserProfile retrievedProfile = gatewayService.getMyProfile(email);

		assertTrue(retrievedProfile.getAvatarUrl().equals(generatedProfile.getAvatarUrl()));
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

	@Test
	public void deleteProfile(){
		String email = UUID.randomUUID() + "@mail.com";
		keycloakService.signup(email, DEFAULT_PASSWORD);
		UserProfile generatedProfile = dataManager.generateUserProfile(email);

		gatewayService.putProfile(email, generatedProfile);

		UserProfile retrievedProfile = gatewayService.getMyProfile(email);

		assertTrue(retrievedProfile.getAvatarUrl().equals(generatedProfile.getAvatarUrl()));

		log.debug("-----------continue");

		gatewayService.deleteMyProfile(email);

		Throwable thrown = catchThrowable(() -> gatewayService.getMyProfile(email));

		assertThat(thrown)
				.isInstanceOf(FeignException.NotFound.class);

	}

	@Test
	public void patchProfile(){
		String email = UUID.randomUUID() + "@mail.com";
		keycloakService.signup(email, DEFAULT_PASSWORD);
		UserProfile generatedProfile = dataManager.generateUserProfile(email);

		gatewayService.putProfile(email, generatedProfile);

		var newFieldValue = new HashMap<String, String>();
		String newValue = UUID.randomUUID().toString();
		newFieldValue.put("avatarUrl", newValue);

		UserProfile retrievedProfile = gatewayService.patchProfile(email, newFieldValue);

		assertTrue(retrievedProfile.getAvatarUrl().equals(newValue));
	}


}
