package fr.treeptik.cloudunit.core.dto;

import static org.junit.Assert.*;

import fr.treeptik.cloudunit.core.dto.LogUnit;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by nicolas on 30/09/15.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void parameterSourceNull() {
        LogUnit line1 = new LogUnit(null, "Hello, I am a log line or a stackstrace!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterMessageNull() {
        LogUnit line1 = new LogUnit("catalina.out", null);
    }

    @Test
    public void differentsLinesForMessage() {
        LogUnit line1 = new LogUnit("catalina.out", "Hello, I am a log stackstrace!");
        LogUnit line2 = new LogUnit("catalina.out", "Goodbye, I am an another message!");
        assertFalse(line1.equals(line2));

        LogUnit line3 = new LogUnit("catalina.out", "Hello, I am a log stackstrace!");
        LogUnit line4 = new LogUnit("localhost_20151203", "Hello, I am a log stackstrace!");
        assertFalse(line3.equals(line4));
    }

    @Test
    public void differentsLinesForDates() {
        LogUnit line1 = new LogUnit("catalina.out", "Hello, I am a log stackstrace!");
        LogUnit line2 = new LogUnit("localhost.txt", "Hello, I am a log stackstrace!");
        assertFalse(line1.equals(line2));
    }

}