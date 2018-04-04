package br.com.dataanalyser.jaderbittencourt.model;

import lombok.Data;

@Data
public class Client {

    private String CNPJ;
    private String name;
    private String businessArea;

    public Client() {
    }

    public Client(String CNPJ, String name, String businessArea) {
        this.CNPJ = CNPJ;
        this.name = name;
        this.businessArea = businessArea;
    }
}
