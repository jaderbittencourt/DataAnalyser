package br.com.dataanalyser.jaderbittencourt.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DataAnalyserTest {

    private DataAnalyser dataAnalyser;

    @Before
    public void setUp() {
        dataAnalyser = new DataAnalyser();
    }

    @Test
    public void shouldNotProcessIfSystemIsAlreadyProcessing() {
        dataAnalyser.startProcessing();
        boolean systemCurrentState = dataAnalyser.isIdle();
        dataAnalyser.analyseDataFiles();

        Assert.assertEquals(systemCurrentState, dataAnalyser.isIdle());
    }

    @Test
    public void shouldSetSystemAsProcessing() {
        dataAnalyser.stopProcessing();
        boolean systemCurrentState = dataAnalyser.isIdle();
        dataAnalyser.startProcessing();
        boolean systemNewState = dataAnalyser.isIdle();
        Assert.assertNotEquals(systemCurrentState, systemNewState);
    }

    @Test
    public void shouldSetSystemAsIdle() {
        dataAnalyser.startProcessing();
        boolean systemCurrentState = dataAnalyser.isIdle();
        dataAnalyser.stopProcessing();
        boolean systemNewState = dataAnalyser.isIdle();
        Assert.assertNotEquals(systemCurrentState, systemNewState);

    }
}
