package com.base.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HtmlParserUtil {
    public static Document getDocument(String htmlString) {
    	return Jsoup.parse(htmlString);
    }
}
