package org.isite.security;

import org.isite.commons.cloud.utils.PropertyUtils;
import org.isite.security.web.utils.SecurityUtils;
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
        System.out.println(stringEncryptor.encrypt("bi.app"));
    }

    @Test
    void testBasicAuth() {
        System.out.println(SecurityUtils.getBasicAuth("data.admin", "data.admin"));
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
