package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TaskGetOneProduct implements Callable<Product>{

    private String item;

    public TaskGetOneProduct(String item) {
        this.item = item;
    }

    @Override
        public Product call() throws Exception {
        //Document document = (Document) Jsoup.connect("https://www.aboutyou.de/p/review/shirt-mit-foto-print-3674301").get();
        Document document = (Document) Jsoup.connect(item).get();
        Element elementName = document.selectFirst(WebsiteKeys.keyForElmName);
        Element elementInitialPrice = document.selectFirst(WebsiteKeys.keyForElmInitialPrice);
        Element elementPriceIfDisc = document.selectFirst(WebsiteKeys.keyForElmPriceIfDisc);
        Element elementPrice = document.selectFirst(WebsiteKeys.keyForElmPrice);
        Element elementDescription = document.selectFirst(".outerWrapper_gdz8cm");
        Element elementLinkForBrand = document.selectFirst("div.wrapper_e296pg a");
        String secPartOfLinkForBrand = elementLinkForBrand.attr("href");
        Document docForBrand = Jsoup.connect(item + secPartOfLinkForBrand).get();

        Element elementBrand = docForBrand.selectFirst(WebsiteKeys.keyForElmBrand);
        //  String secPartOfLinkForColor = elementColor.attr("href");   .extrasWrapper_78iygn > p:nth-child(1)
        //add more info from description
        ArrayList<String> arrayListDescrInf = new ArrayList<>();
        Elements elemNamesOfDescr = document.select("div.container_iv4rb4 p");
        for (int i = 0; i < elemNamesOfDescr.size(); i++) {
            String nameOfDescr = elemNamesOfDescr.get(i).attr("span.subline_19eqe01");
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
        String description = elementDescription.html();

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
