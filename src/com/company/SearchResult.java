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
        String result = "";

        for (Product product : content) {
            result += String.format("<item>%s</item>", product.getTitle());
        }
        return result;
    }

}
