package org.isite.security;

import org.isite.commons.cloud.utils.PropertyUtils;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest(classes = SecurityApplication.class)
class SecurityApplicationTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    void testJasyptEnencrypt() {
        System.out.println(stringEncryptor.encrypt("a2JMc4oJdCR78qpYRKHVSjCri"));
    }

    @Test
    void testBCryptEncoder() {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }

    @Test
    void testProperty() {
        //t3SMNUgdvHTm0jKm8Anaj
        System.out.println(PropertyUtils.getProperty("spring.datasource.password"));
    }
}
