package com.box.l10n.mojito.cli.structuredoutput;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Writes structured messages to given output.
 */
public class StructuredWriter {
    /**
     * logger
     */
    static Logger logger = LoggerFactory.getLogger(StructuredWriter.class);

    OutputType outputType;

    public StructuredWriter(OutputType outputType) {
        this.outputType = outputType;
    }


    /**
     * Write a message to output
     */
    public <T extends JSONObject> void write(T message) {
        String serializedMessage = message.toJSONString();
        logger.trace(serializedMessage);

        if (outputType == OutputType.STDOUT) {
            System.out.println(serializedMessage);
        }
    }

    public enum OutputType {
        NONE, STDOUT
    }
}
