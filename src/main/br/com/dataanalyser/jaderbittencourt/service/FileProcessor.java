package br.com.dataanalyser.jaderbittencourt.service;

import br.com.dataanalyser.jaderbittencourt.model.Client;
import br.com.dataanalyser.jaderbittencourt.model.Item;
import br.com.dataanalyser.jaderbittencourt.model.Sale;
import br.com.dataanalyser.jaderbittencourt.model.Salesman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FileProcessor {

    private String path;

    private List<File> files = new ArrayList<>();

    private List<Client> clients = new ArrayList<>();
    private List<Salesman> sellers = new ArrayList<>();
    private List<Sale> sales = new ArrayList<>();

    private final String ACCEPTED_EXTENSION = "dat";

    private ReportComputer reportComputer;

    public FileProcessor(String path) {
        this.path = path;
        this.reportComputer = new ReportComputer();
    }

    protected void processFiles() {
        for (File f: files) {
            BufferedReader abc = null;
            try {
                abc = new BufferedReader(new FileReader(f));
                String line;
                while((line = abc.readLine()) != null) {
                    processLine(line);
                }
                abc.close();
                f.delete();
            } catch (IOException e) {
                // TODO tratar isso
                e.printStackTrace();
            }
        }
        files = new ArrayList<>();

        if (shouldComputeReportFile()) {
            reportComputer.computeReportFile(clients, sellers, sales);
            clearLists();
        }
    }

    /**
     * Get all .dat files from homepath/data/in directory and populate the files list
     */
    protected void buildFilesList() {
        String directory = path;

        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        if (ACCEPTED_EXTENSION.equals(getFileExtension(file.getFileName().toString()))) {
                            files.add(new File(file.toString()));
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * return the file extension, splitting the file name by "." and returning the last position
     * @param filePath
     * @return
     */
    protected String getFileExtension(String filePath) {
        List<String> result = Arrays.asList(filePath.split("\\."));
        return result.get(result.size() -1);

    }

    /**
     * Split the line by "ç" and call the respective builder, based on the line id
     * @param line
     */
    protected void processLine(String line) {
        List<String> values = Arrays.asList(line.split("ç"));

        if (values.isEmpty() || values.size() != 4) {
            return;
        }

        switch (values.get(0)) {
            case "001":
                buildSalesman(values);
                break;
            case "002":
                buildClient(values);
                break;
            case "003":
                buildSale(values);
                break;
            default:
                System.out.println("***** INVALID LINE *****");
                System.out.println(values.toString());
                System.out.println("************************");
                break;
        }

    }

    /**
     * build the salesman object
     * @param values
     */
    protected void buildSalesman(List<String> values) {
        Salesman salesman = new Salesman();
        salesman.setCPF(values.get(1));
        salesman.setName(values.get(2));
        salesman.setSalary(Double.parseDouble(values.get(3)));
        sellers.add(salesman);
    }

    /**
     * Build the client object
     * @param values
     */
    protected void buildClient(List<String> values) {
        Client client = new Client();
        client.setCNPJ(values.get(1));
        client.setName(values.get(2));
        client.setBusinessArea(values.get(3));
        clients.add(client);
    }

    /**
     * Build sale object
     * @param values
     */
    protected void buildSale(List<String> values) {
        Sale sale = new Sale();
        sale.setSaleId(Integer.parseInt(values.get(1)));
        sale.setSaleItems(buildItemsList(values.get(2)));
        sale.setSalesmanName(values.get(3));
        sales.add(sale);
    }

    /**
     * Build and return the sales items list
     * @param s
     * @return
     */
    protected List<Item> buildItemsList(String s) {
        List<Item> items = new ArrayList<>();

        for(String l: splitItemsIntoArray(s)) {
            Item item = buildItem(l);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    /**
     * Replace the "[" and "]" from string and split it by ","
     * @param s
     * @return
     */
    protected List<String> splitItemsIntoArray(String s) {
        return Arrays.asList(s.replace("[", "").replace("]","").split(","));
    }

    /**
     * Split the line by "-" and build sale item object
     * @param line
     * @return
     */
    protected Item buildItem(String line) {
        List<String> values = Arrays.asList(line.split("-"));

        if (values.isEmpty() || values.size() != 3) {
            return null;
        }

        Item item = new Item();
        item.setId(Integer.parseInt(values.get(0)));
        item.setQuantity(Double.parseDouble(values.get(1)));
        item.setPrice(Double.parseDouble(values.get(2)));
        return item;
    }

    protected void clearLists() {
        files = new ArrayList<>();
        clients = new ArrayList<>();
        sellers = new ArrayList<>();
        sales = new ArrayList<>();
    }

    protected boolean shouldComputeReportFile() {
        return clients.size() > 0 && sellers.size() > 0 && sales.size() > 0;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Salesman> getSellers() {
        return sellers;
    }

    public List<Sale> getSales() {
        return sales;
    }
}
