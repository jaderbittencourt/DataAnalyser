package br.com.dataanalyser.jaderbittencourt.service;

import br.com.dataanalyser.jaderbittencourt.model.Client;
import br.com.dataanalyser.jaderbittencourt.model.Item;
import br.com.dataanalyser.jaderbittencourt.model.Sale;
import br.com.dataanalyser.jaderbittencourt.model.Salesman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.*;

public class ReportComputer {

    private final String DATA_OUT = System.getProperty("user.home") + File.separator + "data" + File.separator + "out" + File.separator;

    public ReportComputer() {
    }

    /**
     * Generate the report file
     * @param clients
     * @param sellers
     * @param sales
     */
    protected void computeReportFile(List<Client> clients, List<Salesman> sellers, List<Sale> sales) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(DATA_OUT + generateFilename(), "UTF-8");

            writer.println("Total clients: " + computeTotalClients(clients));
            writer.println("Total sellers: " + computeTotalSellers(sellers));
            writer.println("Most expensive sale: " + calculateMostExpensiveSale(sales));
            writer.println("Worst salesman: " + computeWorstSalesman(sales, sellers));
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a filename based of timestamp
     * @return
     */
    protected String generateFilename() {
        Instant instant = Instant.now();
        return  Long.toString(instant.toEpochMilli()) + "_computed.done.dat";
    }

    /**
     * Compute the total of clients
     * @param clients
     * @return
     */
    protected Integer computeTotalClients(List<Client> clients) {
        int result = 0;
        List<String> computedClients = new ArrayList<>();
        for (Client c: clients) {
            if(!computedClients.contains(c.getCNPJ())) {
                result++;
                computedClients.add(c.getCNPJ());
            }
        }
        return result;
    }

    /**
     * Compute the total of sellers
     * @param sellers
     * @return
     */
    protected Integer computeTotalSellers(List<Salesman> sellers) {
        int result = 0;
        List<String> computedSellers = new ArrayList<>();
        for (Salesman s: sellers) {
            if(!computedSellers.contains(s.getCPF())) {
                result++;
                computedSellers.add(s.getCPF());
            }
        }
        return result;
    }

    /**
     * Calculate the most expensive sale and return its id
     * @param sales
     * @return
     */
    protected Integer calculateMostExpensiveSale(List<Sale> sales) {
        Integer saleId = null;

        Double mostExpensiveSale = 0.0;
        for (Sale s: sales) {
            for(Item i: s.getSaleItems()) {
                Double temp = i.getPrice() * i.getQuantity();
                if (temp > mostExpensiveSale) {
                    mostExpensiveSale = temp;
                    saleId = s.getSaleId();
                }
            }
        }
        return saleId;
    }

    /**
     * Compute the worst salesman, validating if there is a salesman without sale or checking the sales values if needed
     * @param sales
     * @param sellers
     * @return
     */
    protected String computeWorstSalesman(List<Sale> sales, List<Salesman> sellers) {
        Map<String, Double> salesBySalesman = buildSalesBySalesman(sales);

        List<String> sellersNames = new ArrayList<>(salesBySalesman.keySet());

        String worstSalesman = findSalesmanWithoutSale(sellersNames, sellers);
        if(!Objects.isNull(worstSalesman)) {
            return worstSalesman;
        }

        return findWorstSalesman(salesBySalesman);
    }

    /**
     * Create a list of sales by salesman
     * @param sales
     * @return
     */
    protected Map<String, Double> buildSalesBySalesman(List<Sale> sales) {
        Map<String, Double> salesBySalesman = new HashMap<>();

        for (Sale s: sales) {
            if (!salesBySalesman.containsKey(s.getSalesmanName())) {
                salesBySalesman.put(s.getSalesmanName(), sumSaleItems(s.getSaleItems()));
            } else {
                Double oldValue = salesBySalesman.get(s.getSalesmanName());
                salesBySalesman.put(s.getSalesmanName(), oldValue + sumSaleItems(s.getSaleItems()));
            }
        }
        return salesBySalesman;
    }

    /**
     * For each item in a sale, multiply the item price by item quantity to compute the total sale value
     * @param items
     * @return
     */
    protected Double sumSaleItems(List<Item> items) {
        Double result = 0.0;

        if (!Objects.isNull(items)) {
            for (Item i : items) {
                result += i.getPrice() * i.getQuantity();
            }
        }

        return result;
    }

    /**
     * Check if there's a salesman without any sale
     * @param sellersNames
     * @param sellers
     * @return
     */
    protected String findSalesmanWithoutSale(List<String> sellersNames, List<Salesman> sellers) {
        // if we have the same quantity of sellers in both lists, every salesman had at least one sale
        if (sellersNames.size() == sellers.size()) {
            return null;
        }

        // build another list with salesman names
        List<String> names = new ArrayList<>();
        for(Salesman s: sellers) {
            names.add(s.getName());
        }

        // if after remove all sellers names we have at least one, this salesman is the worst
        // because he/she had no sales at all
        if(names.removeAll(sellersNames)) {
            return names.toString();
        }

        // if any case matched we just return null
        return null;
    }

    /**
     * Check the list of salesman sells and find the lowest value
     * @param salesBySalesman
     * @return
     */
    protected String findWorstSalesman(Map<String, Double> salesBySalesman) {
        Double lowestValue = null;
        String salesmanName = null;

        for (Map.Entry<String, Double> entry: salesBySalesman.entrySet()) {
            if (lowestValue == null || entry.getValue() < lowestValue) {
                lowestValue = entry.getValue();
                salesmanName = entry.getKey();
            }
        }

        return salesmanName;
    }
}
