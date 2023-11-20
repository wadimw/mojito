package com.box.l10n.mojito.cli.structuredoutput;

import org.json.simple.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.OutputCapture;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StructuredWriterTest {

    /**
     * logger
     */
    static Logger logger = LoggerFactory.getLogger(StructuredWriterTest.class);

    @Rule
    public OutputCapture outputCapture = new OutputCapture();

    @Test
    public void outputStdout() {
        StructuredWriter structuredWriter = new StructuredWriter(StructuredWriter.OutputType.STDOUT);

        JSONObject message = new JSONObject();
        message.put("foo", "bar");
        message.put("baz", 1);

        structuredWriter.write(message);

        assertTrue(outputCapture.toString().contains(message.toJSONString()));
    }

    @Test
    public void disableOutput() {
        StructuredWriter structuredWriter = new StructuredWriter(StructuredWriter.OutputType.NONE);

        JSONObject message = new JSONObject();
        message.put("foo", "bar");
        message.put("baz", 1);

        structuredWriter.write(message);

        assertFalse(outputCapture.toString().contains(message.toJSONString()));
    }
}