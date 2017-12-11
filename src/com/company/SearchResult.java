package com.company;

import java.util.ArrayList;

public class SearchResult {
    private ArrayList<Product> content;

    public ArrayList<Product> getContent() {
        return content;
    }

    public void setContent(ArrayList<Product> content) {
        this.content = content;
    }

    public String createXMLResult() {

        String xmlView = "<?xml version=" + "'1.0'" + " encoding=" + "'UTF-8'" + "?>" +
                "<offers>";

        String result = "";

        for (Product product : content) {
            result += String.format("<title>%s</title>", product.getTitle());
            result += String.format("<name>%s</name>", product.getName());
            result += String.format("<brand>%s</brand>", product.getBrand());
            result += String.format("<color>%s</color>", product.getColor());
            result += String.format("<price>%s</price>", product.getPrice());
            result += String.format("<initial_price>%s</initial_price>", product.getInitialPrice());
            result += String.format("<description>%s</description>", product.getDescription());
        }
        return result;
    }

}
