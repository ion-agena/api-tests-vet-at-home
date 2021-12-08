package com.agenatech.test.vetathome;

import com.agenatech.test.vetathome.payload.request.UserPetLink;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

		UserProfile retrievedProfile = gatewayService.putProfile(DEFAULT_USER_ID, generatedProfile);
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

		String petLink = uriUtils.getPetLinkFromPet(retrievedPet);
				log.debug("-------href {}", petLink);

		String petId = uriUtils.getIdFromLink(petLink);

		log.debug("-------petId {}", petId);

		List<UserProfile> owners = gatewayService.searchProfilesByPetIds(Arrays.asList(petId)).get_embedded().getProfiles();

		assertTrue(isDefaultUserPresentInTheList(owners));
	}

	@Test
	public void link(){
		PetProfile generatedPetProfile = dataManager.generatePetProfile();
		PetProfile retrievedPet = gatewayService.saveAndLink(generatedPetProfile);
		log.debug("-------retrievedPet {}", retrievedPet);

		UserProfile generatedProfile = dataManager.generateUserProfile();
		log.debug("-------generated profile {}", generatedProfile);

		UserProfile retrievedProfile = gatewayService.putProfile(generatedProfile.getAvatarUrl(), generatedProfile);
		log.debug("-------retrieved profile {}", retrievedProfile);

		UserPetLink newLink = UserPetLink.builder()
				.userLink(uriUtils.getSelfLinkFromUser(retrievedProfile))
				.petLink(uriUtils.getPetLinkFromPet(retrievedPet))
				.build();


		gatewayService.link(newLink);

		String petId = uriUtils.getIdFromLink(uriUtils.getPetLinkFromPet(retrievedPet));
		log.debug("-------petId {}", petId);

		List<UserProfile> owners = gatewayService.searchProfilesByPetIds(Arrays.asList(petId)).get_embedded().getProfiles();

		assertTrue(isDefaultUserPresentInTheList(owners));
	}

	private boolean isDefaultUserPresentInTheList(List<UserProfile> owners){
		return owners.stream().map(owner -> uriUtils.getSelfLinkFromUser(owner) )
				.map(link -> uriUtils.getIdFromLink(link)).collect(Collectors.toList())
				.contains(DEFAULT_USER_ID);
	}



}
