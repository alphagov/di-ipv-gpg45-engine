package uk.gov.di.gpg45engine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import uk.gov.di.gpg45engine.domain.gpg45.IdentityProfile;

import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Configuration
public class EvidenceConfig {

    @Bean
    ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    @Bean("identity-profile-list")
    public List<IdentityProfile> identityProfiles(ObjectMapper objectMapper) throws IOException {
        var identityProfileList = new LinkedList<IdentityProfile>();

        var cl = this.getClass().getClassLoader();
        var resolver = new PathMatchingResourcePatternResolver(cl);
        var resources = resolver.getResources("classpath*:profiles/**/*.json");

        for (var resource : resources){
            var file = resource.getFile();
            try (var is = new FileInputStream(file); var isr = new InputStreamReader(is); var reader = new BufferedReader(isr)) {
                var identityProfile = objectMapper.readValue(reader, IdentityProfile.class);
                identityProfileList.add(identityProfile);
            }
        }

        identityProfileList.sort(Comparator.comparingInt(identityProfile -> identityProfile.getLevelOfConfidence().getValue()));
        Collections.reverse(identityProfileList);

        return identityProfileList;
    }
}
