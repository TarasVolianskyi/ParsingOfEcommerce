package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.xml.ws.Service;
import java.io.IOException;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SearchJSoupRequest {

    private final String SOURCE_URL = "https://www.aboutyou.de/suche?term=%s&category=20201";
    private String keyWord;

    public SearchJSoupRequest(String keyWord) {
        this.keyWord = keyWord;
    }

    public void execute() throws IOException {
        Document executeDoc = getDocumentAfterSearch();
        if (executeDoc == null) {
            System.out.println("Something go wrong");
            return;
        }
        ArrayList<String> items = getAllProductsFromDoc();//pool of links for everyone product
        System.out.println(items);
        ArrayList<Product> products = new ArrayList<>();//pool of products(info about product)
        ExecutorService service = Executors.newFixedThreadPool(10);//concorency

        for (String item : items) {
            TaskGetOneProduct taskGetOneProduct = new TaskGetOneProduct(item);
            Future<Product> futureProduct = service.submit(taskGetOneProduct);
            Product product = null;
            try {
                product = futureProduct.get();
                System.out.println("===============\n" + product.toString());
                products.add(futureProduct.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        SearchResult searchResult = new SearchResult();
        searchResult.setContent(products);
        //System.out.println(searchResult);
        System.out.println("----" + searchResult.createXMLResult());
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

    private ArrayList<String> getAllProductsFromDoc() throws IOException {
        ArrayList<String> result = new ArrayList<>();
        Document documentMain = (Document) Jsoup.connect(String.format(WebsiteKeys.SITE_URL + WebsiteKeys.SEARCH_TERM_TEMPLATE, keyWord)).get();
        Element numberOfPages = documentMain.select(WebsiteKeys.KEY_FOR_PAGINATION).last();
        ExecutorService service = Executors.newFixedThreadPool(5);//concorency
        int numbOfAllPages = Integer.parseInt(numberOfPages.text());
        for (int i = 1; i <=  numbOfAllPages; i++) {
            String urlPageFirstPart;
            if (i > 1) {
                urlPageFirstPart = String.format(WebsiteKeys.SITE_URL + WebsiteKeys.SEARCH_TERM_WITH_PAGINATION, keyWord, i);
            } else {
                urlPageFirstPart = String.format(WebsiteKeys.SITE_URL + WebsiteKeys.SEARCH_TERM_TEMPLATE, keyWord);
            }
            System.out.println("============ page - " + i + "  =====================");
            TaskGetProductsFromPage taskGetProductsFromPage = new TaskGetProductsFromPage(urlPageFirstPart);

            Future<ArrayList<String>> resultArray = service.submit(taskGetProductsFromPage);
            try {
                result.addAll(resultArray.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


}
