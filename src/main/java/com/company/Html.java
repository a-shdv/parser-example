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
            // career.habr.com
//            Document doc = Jsoup.connect("https://career.habr.com/vacancies/1000135941").get();
//            Elements newsHeadlines = doc.select("html body.vacancies_show_page div.page-container div.page-container__main div.page-width.page-width--responsive div.content-wrapper div.content-wrapper__main.content-wrapper__main--left div");

            // careerist.ru
            Document doc = Jsoup.connect("https://careerist.ru/vakansii/java-razrabotchik-junior-50380356.html").get();
            Elements newsHeadlines = doc.select("html body.carrierist div#subbody div#container.container-home div.container-body section.container.m-t-2 div.row div.col-md-12.col-lg-8.col-lg-push-4.col-xl-9.col-xl-push-3 div.row div#mainPageCenter.col-lg-12.col-xl-8 div#vacancy-show.page_builder.vacancy-show div.targetDesBG div.b-b-1 p");
            for (Element headline : newsHeadlines) {
                System.out.printf("%s", headline.text());
            }

            System.out.println();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
