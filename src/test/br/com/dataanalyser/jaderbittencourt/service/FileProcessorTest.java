package br.com.dataanalyser.jaderbittencourt.service;

import br.com.dataanalyser.jaderbittencourt.model.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class FileProcessorTest {

    private FileProcessor fileProcessor;

    @Before
    public void setUp() {
        fileProcessor = new FileProcessor(System.getProperty("user.home") + File.separator + "data" + File.separator + "in" + File.separator);
    }

    @Test
    public void shouldProcessEachLineTypeCorrectly() {
        int initialClientSize = fileProcessor.getClients().size();
        fileProcessor.processLine("002ç2345675434544345çJose da SilvaçRural");
        int finalClientSize = fileProcessor.getClients().size();

        int initialSalesmanSize = fileProcessor.getSellers().size();
        fileProcessor.processLine("001ç1234567891234çDiegoç50000");
        int finalSalesmanSize = fileProcessor.getSellers().size();

        int initialSalesSize = fileProcessor.getSales().size();
        fileProcessor.processLine("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego");
        int finalSalesSize = fileProcessor.getSales().size();

        Assert.assertTrue(initialClientSize < finalClientSize);
        Assert.assertTrue(initialSalesmanSize < finalSalesmanSize);
        Assert.assertTrue(initialSalesSize < finalSalesSize);
    }

    @Test
    public void shouldBuildClient() {
        int initialSize = fileProcessor.getClients().size();
        fileProcessor.buildClient(Arrays.asList("002ç2345675434544345çJose da SilvaçRural".split("ç")));
        int finalSize = fileProcessor.getClients().size();
        Assert.assertTrue(finalSize > initialSize);
    }

    @Test
    public void shouldBuildSalesman() {
        int initialSize = fileProcessor.getSellers().size();
        fileProcessor.buildSalesman(Arrays.asList("001ç1234567891234çDiegoç50000".split("ç")));
        int finalSize = fileProcessor.getSellers().size();
        Assert.assertTrue(finalSize > initialSize);
    }

    @Test
    public void shouldBuildSale() {
        int initialSize = fileProcessor.getSales().size();
        fileProcessor.buildSale(Arrays.asList("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego".split("ç")));
        int finalSize = fileProcessor.getSales().size();
        Assert.assertTrue(finalSize > initialSize);
    }

    @Test
    public void shouldBuildItem() {
        Item i = fileProcessor.buildItem("1-10-100");
        Assert.assertNotNull(i);
    }

    @Test
    public void shouldSplitItemsIntoArray() {
        List<String> result = fileProcessor.splitItemsIntoArray("[1-10-100,2-30-2.50,3-40-3.10]");
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void shouldBuildItemsList() {
        List<Item> result = fileProcessor.buildItemsList("[1-10-100,2-30-2.50,3-40-3.10]");
        Assert.assertNotNull(result);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void shouldGetFileExtension() {
        Assert.assertEquals("dat", fileProcessor.getFileExtension("oi.dat"));
    }

    @Test
    public void shouldAllowReportToBeProcessed() {
        fileProcessor.buildClient(Arrays.asList("002ç2345675434544345çJose da SilvaçRural".split("ç")));
        fileProcessor.buildSalesman(Arrays.asList("001ç1234567891234çDiegoç50000".split("ç")));
        fileProcessor.buildSale(Arrays.asList("003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego".split("ç")));

        Assert.assertTrue(fileProcessor.shouldComputeReportFile());
    }

    @Test
    public void shouldBlockReportOfBeingProcessed() {
        Assert.assertFalse(fileProcessor.shouldComputeReportFile());
    }


}
