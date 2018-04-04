package br.com.dataanalyser.jaderbittencourt.model;

import lombok.Data;

@Data
public class Item {

    private int id;
    private Double quantity;
    private Double price;

    public Item() {
    }

    public Item(int id, Double quantity, Double price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }
}
