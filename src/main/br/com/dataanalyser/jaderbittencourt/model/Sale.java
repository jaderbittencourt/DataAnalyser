package br.com.dataanalyser.jaderbittencourt.model;

import lombok.Data;

import java.util.List;

@Data
public class Sale {

    private int saleId;
    private List<Item> saleItems;
    private String salesmanName;

    public Sale() {
    }

    public Sale(int saleId, List<Item> saleItems, String salesmanName) {
        this.saleId = saleId;
        this.saleItems = saleItems;
        this.salesmanName = salesmanName;
    }
}
