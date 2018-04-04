package br.com.dataanalyser.jaderbittencourt.service;

import br.com.dataanalyser.jaderbittencourt.model.Client;
import br.com.dataanalyser.jaderbittencourt.model.Item;
import br.com.dataanalyser.jaderbittencourt.model.Sale;
import br.com.dataanalyser.jaderbittencourt.model.Salesman;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

@RunWith(JUnit4.class)
public class ReportComputerTest {

    private ReportComputer reportComputer;

    @Before
    public void setUp() {
        reportComputer = new ReportComputer();
    }

    @Test
    public void shouldGenerateFileNameAsExpected() {
        Assert.assertTrue(reportComputer.generateFilename().contains(".done.dat"));
    }

    @Test
    public void shouldComputeTotalClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("0123456789", "lucas", "rural"));
        clients.add(new Client("987654321", "pedro", "ti"));

        int result = reportComputer.computeTotalClients(clients);
        Assert.assertEquals(clients.size(), result);
    }

    @Test
    public void shouldComputeTotalSellers() {
        List<Salesman> sellers = new ArrayList<>();
        sellers.add(new Salesman("12345678900", "joao", 1.0));
        sellers.add(new Salesman("12345678901", "marcos", 2.0));

        int result = reportComputer.computeTotalSellers(sellers);
        Assert.assertEquals(sellers.size(), result);
    }

    @Test
    public void shouldCalculateTheMostExpensiveSale() {
        List<Item> item1 = new ArrayList<>();
        item1.add(new Item(1, 2.0, 5.0));
        item1.add(new Item(2, 3.0, 10.0));

        List<Item> item2 = new ArrayList<>();
        item2.add(new Item(1, 2.0, 5.0));
        item2.add(new Item(2, 3.0, 10.0));

        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale(1, item1, "joao"));
        sales.add(new Sale(2, item2, "marcos"));

        Integer result = reportComputer.calculateMostExpensiveSale(sales);
        Integer expectedResult = 1;
        Assert.assertEquals(expectedResult, result);

    }

    @Test
    public void shouldFindTheWorstSalesman() {
        List<Item> item1 = new ArrayList<>();
        item1.add(new Item(1, 2.0, 5.0));
        item1.add(new Item(2, 3.0, 10.0));

        List<Salesman> sellers = new ArrayList<>();
        sellers.add(new Salesman("12345678900", "joao", 1.0));
        sellers.add(new Salesman("12345678901", "marcos", 2.0));

        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale(1, item1, "joao"));

        String result = reportComputer.computeWorstSalesman(sales, sellers);

        Assert.assertEquals("[marcos]", result);
    }

    @Test
    public void shouldBuildSalesAndSalesmanList() {
        List<Item> item1 = new ArrayList<>();
        item1.add(new Item(1, 2.0, 5.0));
        item1.add(new Item(2, 3.0, 10.0));

        List<Sale> sales = new ArrayList<>();
        sales.add(new Sale(1, item1, "joao"));

        Map<String,Double> result = reportComputer.buildSalesBySalesman(sales);
        Double expectedResult = 40.0;
        Assert.assertTrue(result.containsKey("joao"));
        Assert.assertEquals(result.get("joao"), expectedResult);
    }


    @Test
    public void shouldCalculateSalesItemsCorrectly() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1, 2.0, 5.0));
        items.add(new Item(2, 3.0, 10.0));
        Double result = reportComputer.sumSaleItems(items);
        Double expectedResult = 40.0;
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void shouldReturnZeroIfItemsListIsEmptyOrNull() {
        Double expectedResult = 0.0;

        Double result = reportComputer.sumSaleItems(null);
        Assert.assertEquals(expectedResult, result);

        result = reportComputer.sumSaleItems(new ArrayList<>());
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void shouldReturnNullIfThereIsNoSalesmanWithoutSale() {
        List<Salesman> sellers = new ArrayList<>();
        sellers.add(new Salesman("12345678900", "joao", 1.0));
        sellers.add(new Salesman("12345678901", "marcos", 2.0));

        List<String> names = Arrays.asList("marcos", "joao");
        String result = reportComputer.findSalesmanWithoutSale(names, sellers);
        Assert.assertEquals(null, result);
    }

    @Test
    public void shouldReturnTheSalesmanWithoutSale() {
        List<Salesman> sellers = new ArrayList<>();
        sellers.add(new Salesman("12345678900", "joao", 1.0));
        sellers.add(new Salesman("12345678901", "marcos", 2.0));

        List<String> names = Arrays.asList("marcos");
        String result = reportComputer.findSalesmanWithoutSale(names, sellers);
        Assert.assertEquals("[joao]", result);
    }

    @Test
    public void shouldFindWorstSalesman() {
        Map<String, Double> salesBySalesman = new HashMap<>();
        salesBySalesman.put("joao", 1.0);
        salesBySalesman.put("marcos", 2.0);
        String result = reportComputer.findWorstSalesman(salesBySalesman);
        Assert.assertEquals("joao", result);

    }

}