package com.box.l10n.mojito.cli.console;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("l10n.consoleWriter")
public class ConsoleWriterConfigurationProperties {

    ConsoleWriter.OutputType consoleOutputType = ConsoleWriter.OutputType.ANSI;

    ConsoleWriter.OutputType loggerOutputType = ConsoleWriter.OutputType.PLAIN;

    public ConsoleWriter.OutputType getConsoleOutputType() {
        return consoleOutputType;
    }

    public void setConsoleOutputType(ConsoleWriter.OutputType consoleOutputType) {
        this.consoleOutputType = consoleOutputType;
    }

    public ConsoleWriter.OutputType getLoggerOutputType() {
        return loggerOutputType;
    }

    public void setLoggerOutputType(ConsoleWriter.OutputType loggerOutputType) {
        this.loggerOutputType = loggerOutputType;
    }
}
