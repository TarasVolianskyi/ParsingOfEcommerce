package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TaskGetProductsFromPage implements Callable<ArrayList<String>> {

    private ArrayList<String> result = new ArrayList<>();
    private String urlPageFirstPart;

    public TaskGetProductsFromPage(String urlPageFirstPart) {
        this.urlPageFirstPart = urlPageFirstPart;
    }

    @Override
    public ArrayList<String> call() throws Exception {

      Document documentForEveryPage = (Document) Jsoup.connect(urlPageFirstPart).get();
        Elements elementsForEveryPage = documentForEveryPage.select(WebsiteKeys.KEY_DOC_FOR_EVERY_PAGE);
        for (int j = 0; j < elementsForEveryPage.size(); j++) {
            String secPartOfLink = elementsForEveryPage.get(j).attr("href");
            String mainUrlForOneProduct = WebsiteKeys.SITE_URL + secPartOfLink;
            result.add(mainUrlForOneProduct);
        }
        //result.add(urlPageFirstPart);
        return result;
    }
}
