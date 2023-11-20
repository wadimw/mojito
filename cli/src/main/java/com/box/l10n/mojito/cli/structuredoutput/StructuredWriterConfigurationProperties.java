package com.box.l10n.mojito.cli.structuredoutput;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("l10n.structuredWriter")
public class StructuredWriterConfigurationProperties {
    StructuredWriter.OutputType outputType = StructuredWriter.OutputType.NONE;

    public StructuredWriter.OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(StructuredWriter.OutputType outputType) {
        this.outputType = outputType;
    }
}
