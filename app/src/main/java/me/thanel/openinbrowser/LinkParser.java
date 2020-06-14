package me.thanel.openinbrowser;

import android.net.Uri;

import org.apache.commons.validator.routines.UrlValidator;
import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

import java.util.EnumSet;
import java.util.Iterator;

final class LinkParser {

    private static final UrlValidator urlValidator = new UrlValidator();
    private static final LinkExtractor linkExtractor = LinkExtractor.builder()
            .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW))
            .build();

    private LinkParser() {
    }

    static Uri parseText(String text) {
        Uri extractedLink = extractLink(text);
        if (extractedLink != null) {
            return extractedLink;
        }

        String linkToValidate = textWithHttpScheme(text);
        if (urlValidator.isValid(linkToValidate)) {
            return Uri.parse(linkToValidate);
        }

        return buildSearchUri(text);
    }

    private static Uri extractLink(String sourceText) {
        Iterable<LinkSpan> extractedLinks = linkExtractor.extractLinks(sourceText);
        Iterator<LinkSpan> iterator = extractedLinks.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        LinkSpan linkSpan = iterator.next();
        String extractedLink = sourceText.substring(linkSpan.getBeginIndex(), linkSpan.getEndIndex());
        return Uri.parse(textWithHttpScheme(extractedLink));
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
