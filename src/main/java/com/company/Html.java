package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Html {
    private static final Logger log = LoggerFactory.getLogger(Html.class);

    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://career.habr.com/vacancies/1000135941").get();
//            Document doc = Jsoup.connect("https://en.wikipedia.org/").get();
            Elements newsHeadlines = doc.select("html body.vacancies_show_page div.page-container div.page-container__main div.page-width.page-width--responsive div.content-wrapper div.content-wrapper__main.content-wrapper__main--left div");
            for (Element headline : newsHeadlines) {
                System.out.printf("%s", headline.text());
            }

            System.out.println();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
