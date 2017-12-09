package com.company;

public class Main {

    public static void main(String[] args) {
        SearchJSoupRequest request = new SearchJSoupRequest(InputPoint.getKeyWord());
        request.execute();
    }
}
