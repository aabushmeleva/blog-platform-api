package com.github.aabushmeleva.blog.integration;

import com.github.aabushmeleva.blog.config.DataSourceConfiguration;
import com.github.aabushmeleva.blog.config.WebConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.transaction.annotation.Transactional;

@SpringJUnitWebConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class
})
@TestPropertySource(properties = {
        "db.driver-class-name=org.h2.Driver",
        "db.url=jdbc:h2:mem:blog_integration_test;DB_CLOSE_DELAY=-1",
        "db.username=sa",
        "db.password=",
        "images.dir=${java.io.tmpdir}/my-blog-back-app-tests/images"
})
@Transactional
public abstract class AbstractIntegrationTest {
}
