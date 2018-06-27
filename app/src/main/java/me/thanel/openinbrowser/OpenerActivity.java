package me.thanel.openinbrowser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;

import java.util.EnumSet;

public class OpenerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(Intent.EXTRA_TEXT)) {
            // All intents with SEND action should have EXTRA_TEXT that holds the shared text.
            Toast.makeText(this, R.string.error_no_link, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        LinkExtractor linkExtractor = LinkExtractor.builder()
                .linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW))
                .build();
        String extraText = intent.getStringExtra(Intent.EXTRA_TEXT);
        Iterable<LinkSpan> extractedLinks = linkExtractor.extractLinks(extraText);
        if (!extractedLinks.iterator().hasNext()) {
            Toast.makeText(this, R.string.error_no_link, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        LinkSpan linkSpan = extractedLinks.iterator().next();
        String linkToOpen = extraText.substring(linkSpan.getBeginIndex(), linkSpan.getEndIndex());

        if (!linkToOpen.startsWith("http://") && !linkToOpen.startsWith("https://")) {
            // Http scheme is required for activities to correctly handle links so we append it here
            // if we found link without one.
            linkToOpen = "http://" + linkToOpen;
        }
        String defaultBrowserPackageName = BrowserUtils.getDefaultBrowserPackageName(this);
        BrowserUtils.openWith(this, Uri.parse(linkToOpen), defaultBrowserPackageName);

        finish();
    }
}
