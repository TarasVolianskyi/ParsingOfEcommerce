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
            System.out.println(product.getDescription());
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
        //  Elements elements = documentMain.select("div.product-image a");//not necessary
        Element numberOfPages = documentMain.select(".gt9").last();
        //System.out.println("count = " + elements.size());
        //System.out.println("number of pages = " + numberOfPages.text());
        int numbOfAllPages = Integer.parseInt(numberOfPages.text());
        for (int i = 1; i <= 3/* numbOfAllPages*/; i++) {
           String urlPageFirstPart;
            if(i>1){
                urlPageFirstPart = String.format("https://www.aboutyou.de/suche?term=shirt&category=20201&page=%d", i);
            }else {
                urlPageFirstPart = String.format("https://www.aboutyou.de/suche?term=shirt&category=20202");
            }

            System.out.println("============ page - " + i + "  =====================");
            Document documentForEveryPage = (Document) Jsoup.connect(urlPageFirstPart).get();
            Elements elementsForEveryPage = documentForEveryPage.select("div.product-image a");
            for (int j = 0; j < elementsForEveryPage.size(); j++) {
                String secPartOfLink = elementsForEveryPage.get(j).attr("href");
                String mainUrlForOneProduct = "https://www.aboutyou.de" + secPartOfLink;
                result.add(mainUrlForOneProduct);
            }
        }
        //TODO add searcher for the first page
        return result;
    }

    private Product getProductInfoFromURL(String item) throws IOException {
        //Document document = (Document) Jsoup.connect("https://www.aboutyou.de/p/review/shirt-mit-foto-print-3674301").get();
        Document document = (Document) Jsoup.connect(item).get();
        Element elementName = document.selectFirst(WebsiteKeys.keyForElmName);
        Element elementInitialPrice = document.selectFirst(WebsiteKeys.keyForElmInitialPrice);
        Element elementPriceIfDisc = document.selectFirst(WebsiteKeys.keyForElmPriceIfDisc);
        Element elementPrice = document.selectFirst(WebsiteKeys.keyForElmPrice);
        Element elementDescription = document.selectFirst("span.badge_1hhulki-o_O-extra_3bg74t");
        Element elementLinkForBrand = document.selectFirst("div.wrapper_e296pg a");
        String secPartOfLinkForBrand = elementLinkForBrand.attr("href");
        Document docForBrand = Jsoup.connect(item + secPartOfLinkForBrand).get();

        Element elementBrand = docForBrand.selectFirst(WebsiteKeys.keyForElmBrand);

        //  String secPartOfLinkForColor = elementColor.attr("href");
//add more info from description
        ArrayList<String> arrayListDescrInf = new ArrayList<>();
        Elements elemNamesOfDescr = document.select("div.container_iv4rb4 p");
        for (int i = 0; i < elemNamesOfDescr.size(); i++) {
            String nameOfDescr = elemNamesOfDescr.get(i).attr("span.subline_19eqe01");
            System.out.println("descr s= " + nameOfDescr);
            arrayListDescrInf.add(nameOfDescr);
        }

        ArrayList<String> arrayListColors = new ArrayList<>();
        Elements elemColors = document.select("div.thumbsWrapper_1rutc76 p");
        for (int i = 0; i < elemColors.size(); i++) {
            String nameOfColors = elemColors.get(i).attr("href");
            System.out.println("color s -" + nameOfColors);
            arrayListDescrInf.add(nameOfColors);
        }

        ArrayList<String> arrayListSizes = new ArrayList<>();
        Elements elemNamesOfSizes = document.select("div.container_iv4rb4 p");
        for (int i = 0; i < elemNamesOfSizes.size(); i++) {
            String nameOfSize = elemNamesOfSizes.get(i).attr("p.subline_19eqe01");
            //System.out.println("size s - " + nameOfSize);
            arrayListDescrInf.add(nameOfSize);
        }


        String name = elementName.text();
        String brand = elementBrand.text();
        String size;
         String color = arrayListColors.toString();
        String price;
        String initialPrice;
        String description = arrayListDescrInf.toString();

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
         result.setColor(color);
        result.setPrice(price);
        result.setInitialPrice(initialPrice);
        // result.setColor(color);
        result.setDescription(description);
        return result;
    }
}
