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
            result += String.format("<title>%s</title>\n" +
                    "<name>%s</name>\n" +
                    "<brand>%s</brand>\n" +
                    "<color>%s</color>\n" +
                    "<price>%s</price>\n" +
                    "<initial_price>%s</initial_price>\n" +
                    "<description>%s</description>",
                    product.getTitle(), product.getName(), product.getBrand(),
                    product.getColor(), product.getPrice(), product.getInitialPrice(),
                    product.getDescription());



        }
        return result;
    }

}
