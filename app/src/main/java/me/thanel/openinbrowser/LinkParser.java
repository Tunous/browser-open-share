package me.thanel.openinbrowser;

import android.net.Uri;

import org.apache.commons.validator.routines.UrlValidator;

class LinkParser {
    private LinkParser() {
    }

    static Uri parseText(String text) {
        String linkToValidate = textWithHttpScheme(text);

        if (new UrlValidator().isValid(linkToValidate)) {
            return Uri.parse(linkToValidate);
        }
        return buildSearchUri(text);
    }

    private static Uri buildSearchUri(String searchTerm) {
        return new Uri.Builder()
                .scheme("https")
                .authority("google.com")
                .path("search")
                .appendQueryParameter("q", searchTerm)
                .build();
    }

    private static String textWithHttpScheme(String text) {
        if (!text.startsWith("http://") && !text.startsWith("https://")) {
            return "http://" + text;
        }
        return text;
    }
}
