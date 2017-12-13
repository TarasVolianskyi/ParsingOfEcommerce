package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<String> items = getAllProductsFromDoc(executeDoc);//pool of links for everyone product
        System.out.println(items);
        ArrayList<Product> products = new ArrayList<>();//pool of products(info about product)
        for (String item : items) {
            Product product = getProductInfoFromURL(item);
            products.add(product);

            System.out.println("===============");
            System.out.println(product.getName());
            System.out.println(product.getPrice());
            System.out.println(product.getInitialPrice());
            System.out.println(product.getBrand());
            System.out.println(product.getColor());
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

    private ArrayList<String> getAllProductsFromDoc(Document executeDoc) throws IOException {
        ArrayList<String> result = new ArrayList<>();
        //Document documentMain = (Document) Jsoup.connect("https://www.aboutyou.de/suche?term=shirt&category=20201").get();
        Document documentMain = executeDoc;
        Elements elements = documentMain.select("div.product-image a");
        Element numberOfPages = documentMain.select(".gt9").last();
        //System.out.println("count = " + elements.size());
        //System.out.println("number of pages = " + numberOfPages.text());
        int numbOfAllPages = Integer.parseInt(numberOfPages.text());
        for (int i = 2; i <= 3/* numbOfAllPages*/; i++) {
            String urlPageFirstPart = String.format("https://www.aboutyou.de/suche?term=shirt&category=20201&page=%d", i);
            System.out.println("============ page - " + i + "  =====================");
            Document documentForEveryPage = (Document) Jsoup.connect(urlPageFirstPart).get();
            Elements elementsForEveryPage = documentForEveryPage.select("div.product-image a");
            for (int j = 0; j < elementsForEveryPage.size(); j++) {
                String secPartOfLink = elementsForEveryPage.get(j).attr("href");
                String mainUrlForOneProduct = "https://www.aboutyou.de" + secPartOfLink;
                result.add(mainUrlForOneProduct);
                // System.out.println(mainUrlForOneProduct);
            }
        }
        //TODO add all items from reserch
        //TODO add searcher for the first page
        return result;
    }

    private Product getProductInfoFromURL(String item) throws IOException {
        //Document document = (Document) Jsoup.connect("https://www.aboutyou.de/p/review/shirt-mit-foto-print-3674301").get();
        Document document = (Document) Jsoup.connect(item).get();
        Element elementName = document.selectFirst(".name_1jqcvyg");
        Element elementInitialPrice = document.selectFirst("span.originalPrice_17gsomb-o_O-strikeOut_32pxry");
        Element elementPriceIfDisc = document.selectFirst("span.finalPrice_klth9m-o_O-highlight_1t1mqn4");
        Element elementPrice = document.selectFirst("span.finalPrice_klth9m");

        Element elementDescription = document.selectFirst("span.badge_1hhulki-o_O-extra_3bg74t");


        Element elementLinkForBrand = document.selectFirst("div.wrapper_e296pg a");
        String secPartOfLinkForBarand = elementLinkForBrand.attr("href");
        Document docForBrand = Jsoup.connect(item + secPartOfLinkForBarand).get();

        Element elementBrand = docForBrand.selectFirst(".js-product-item-brand");


        String name = elementName.text();
        String brand = elementBrand.text();
        String size;
        //  String color = WebsiteKeys.elen;
        String price;
        String initialPrice;
        String description;

        if (elementInitialPrice != null) {
            initialPrice = elementInitialPrice.text();
        } else {
            initialPrice = "";
        }

        if (elementInitialPrice != null) {
            price = elementPriceIfDisc.text();
        } else {
            price = elementPrice.text();
        }

        Product result = new Product();
        //result.setTitle(document.title());
        result.setName(name);
        result.setBrand(brand);
        // result.setColor(color);
        result.setPrice(price);
        result.setInitialPrice(initialPrice);
        //result.setDescription(elementDescription.text());
        //TODO get all info about product using JSOUP
        return result;
    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 26) == 'x') {
            str = str.substring(0, str.length() - 26);
        }
        return str;
    }
}
