package com.box.l10n.mojito.cli.structuredoutput;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for {@link StructuredWriter}.
 *
 * @author jaurambault
 */
@Configuration
public class StructuredWriterConfig {
    @Primary
    @Bean
    public StructuredWriter structuredWriter(StructuredWriterConfigurationProperties structuredWriterConfigurationProperties) {
        return new StructuredWriter(structuredWriterConfigurationProperties.outputType);
    }
}
