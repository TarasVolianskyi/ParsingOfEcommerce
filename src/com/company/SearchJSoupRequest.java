package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import java.io.IOException;
import java.util.ArrayList;

public class SearchJSoupRequest {

    private final String SOURCE_URL = "https://www.aboutyou.de/suche?term=%s&category=20201";
    private String keyWord;

    public SearchJSoupRequest(String keyWord) {
        this.keyWord = keyWord;
    }

    public void execute() {
        Document executeDoc = getDocumentAfterSearch();
        if (executeDoc == null) {
            System.out.println("Something go wrong");
            return;
        }
        ArrayList<String> items = getAllProductsFromDoc(executeDoc);//links
        ArrayList<Product> products = new ArrayList<>();
        for (String item : items) {
            Product product = getProductInfoFromURL(item);
            products.add(product);
        }
        SearchResult searchResult = new SearchResult();
        searchResult.setContent(products);
        System.out.println(searchResult.createXMLResult());

    }
    private Document getDocumentAfterSearch() {
        String title = "";
        Document doc = null;
        try {
            doc = (Document) Jsoup.connect(String.format(SOURCE_URL, keyWord)).get();
            title = doc.title();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Jsoup Can read HTML page from URL, title : " + title);

        return doc;
    }
    private ArrayList<String> getAllProductsFromDoc(Document executeDoc) {
        ArrayList<String> result = new ArrayList<>();
        result.add("https://www.aboutyou.de/p/calvin-klein-jeans/t-shirt-mit-logo-3673277");
        result.add("https://www.aboutyou.de/p/calvin-klein-jeans/t-shirt-mit-logo-3653182");
        result.add("https://www.aboutyou.de/p/h-i-s-jeans/t-shirt-3366743");
        //TODO add all items from reserch
        return result;
    }
    private Product getProductInfoFromURL(String item) {
        Product result = new Product();
        result.setTitle("my product");
        /*result.setColor"my product");
        result.setTitle("my product");
        result.setTitle("my product");*/
        //TODO get all info about product using JSOUP
        return result;
    }

}
