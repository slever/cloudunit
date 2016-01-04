package fr.treeptik.cloudunit.core.functions;

import fr.treeptik.cloudunit.core.dto.LogUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by nicolas on 04/01/2016.
 */
public class LogsFilterTest {

    private List<LogUnit> logs;

    @Before
    public void init() {
        logs = new ArrayList<>();
        LogUnit line1 = new LogUnit("catalina.out", "Hello, I am a log stackstrace!");
        LogUnit line2 = new LogUnit("catalina.out", "Goodbye, I am an another message!");
        LogUnit line3 = new LogUnit("localhost.txt", "Goodbye, I am an wrong message!");
        LogUnit line4 = new LogUnit("localhost.txt", "Goodbye, I am an empty message!");

        logs = new ArrayList() {
            {
                add(line1);
                add(line2);
                add(line3);
                add(line4);
            }
        };
    }

    @Test
    public void testFilterSource() {
        List<LogUnit> logsFiltered = LogsFilter.bySource.apply("catalina.out", logs);
        Assert.assertEquals(2, logsFiltered.size());

        logsFiltered = LogsFilter.bySource.apply("NO FILE", logs);
        Assert.assertEquals(0, logsFiltered.size());
    }

    @Test
    public void testFilterMessage() {
        List<LogUnit> logsFiltered = LogsFilter.byMessage.apply("stackstrace", logs);
        Assert.assertEquals(1, logsFiltered.size());
        
        logsFiltered = LogsFilter.byMessage.apply("GOODB", logs);
        Assert.assertEquals(3, logsFiltered.size());

        logsFiltered = LogsFilter.bySource.apply("NOTHING HERE", logs);
        Assert.assertEquals(0, logsFiltered.size());
    }
}