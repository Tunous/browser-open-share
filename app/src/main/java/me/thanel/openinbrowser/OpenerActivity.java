package me.thanel.openinbrowser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.Toast;

import org.apache.commons.validator.routines.UrlValidator;

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

        String extraText = intent.getStringExtra(Intent.EXTRA_TEXT);
        Uri linkToOpen = LinkParser.parseText(extraText);

        String defaultBrowserPackageName = BrowserUtils.getDefaultBrowserPackageName(this);
        BrowserUtils.openWith(this, linkToOpen, defaultBrowserPackageName);

        finish();
    }
}
