package com.agenatech.test.vetathome;


import com.agenatech.test.vetathome.payload.response.UserProfile;
import com.agenatech.test.vetathome.service.DataManager;
import com.agenatech.test.vetathome.service.GatewayService;
import com.agenatech.test.vetathome.service.KeycloakService;
import com.agenatech.test.vetathome.utils.UriUtils;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
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

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class VetAtHomeApplicationNegativeTests {
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


	@Test
	public void getOtherProfile(){
		UserProfile myProfile = gatewayService.getDefaultProfile();
		log.debug("------- profile {}", myProfile);

		String email = UUID.randomUUID() + "@mail.com";
		keycloakService.signup(email, DEFAULT_PASSWORD);

		Throwable thrown = catchThrowable(() -> gatewayService.getProfileById(email, DEFAULT_USER_ID) );

		assertThat(thrown)
				.isInstanceOf(FeignException.NotFound.class);
	}

	@Test
	public void putIncorrectProfileId(){
		String email = UUID.randomUUID() + "@mail.com";
		keycloakService.signup(email, DEFAULT_PASSWORD);
		UserProfile generatedProfile = dataManager.generateUserProfile(email);

		Throwable thrown = catchThrowable(() -> gatewayService.putProfileById(email, UUID.randomUUID().toString(), generatedProfile));

		assertThat(thrown)
				.isInstanceOf(FeignException.NotFound.class);
	}

	@Test
	public void patchOtherProfile(){
		String email = UUID.randomUUID() + "@mail.com";
		keycloakService.signup(email, DEFAULT_PASSWORD);
		UserProfile generatedProfile = dataManager.generateUserProfile(email);

		gatewayService.putProfile(email, generatedProfile);

		var newFieldValue = new HashMap<String, String>();
		String newValue = UUID.randomUUID().toString();
		newFieldValue.put("avatarUrl", newValue);

		Throwable thrown = catchThrowable(() ->  gatewayService.patchProfileById(email, DEFAULT_USER_ID, newFieldValue));
		assertThat(thrown)
				.isInstanceOf(FeignException.NotFound.class);
	}

}
