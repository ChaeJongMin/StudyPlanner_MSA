package com.studyplaner.authservice;

import com.studyplaner.authservice.config.MyProperties;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;

@SpringBootTest
class AuthserviceApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(AuthserviceApplicationTests.class);
	@Autowired
	private MyProperties myProperties;


	@Test
	void contextLoads() {

		log.info("가져온 값 : "+myProperties.getSecret());
		log.info(String.valueOf(myProperties.getTokenValidityInSeconds()));
	}

}
