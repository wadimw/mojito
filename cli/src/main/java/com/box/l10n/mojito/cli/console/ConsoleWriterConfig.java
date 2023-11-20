package com.box.l10n.mojito.cli.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for {@link ConsoleWriter}.
 *
 * @author jaurambault
 */
@Configuration
public class ConsoleWriterConfig {

    @Autowired
    ConsoleWriterConfigurationProperties consoleWriterConfigurationProperties;

    @Primary
    @Bean
    public ConsoleWriter consoleWriter() {
        return new ConsoleWriter(consoleWriterConfigurationProperties.getConsoleOutputType(),
                consoleWriterConfigurationProperties.getLoggerOutputType());
    }

    @Bean(name = "ansiCodeEnabledFalse")
    public ConsoleWriter consoleWriterNoAnsi() {
        return new ConsoleWriter(getOutputTypeNoAnsi(consoleWriterConfigurationProperties.getConsoleOutputType()),
                getOutputTypeNoAnsi(consoleWriterConfigurationProperties.getLoggerOutputType()));
    }

    private ConsoleWriter.OutputType getOutputTypeNoAnsi(ConsoleWriter.OutputType outputType) {
        if (outputType == ConsoleWriter.OutputType.ANSI) {
            return ConsoleWriter.OutputType.PLAIN;
        }
        return outputType;
    }
}
