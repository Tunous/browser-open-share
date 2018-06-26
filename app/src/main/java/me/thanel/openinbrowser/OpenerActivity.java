package me.thanel.openinbrowser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

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

        String linkToOpen = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (!linkToOpen.startsWith("http://") && !linkToOpen.startsWith("https://")) {
            // Http scheme is required for activities to correctly handle links. If user shared
            // regular text try to convert it to a link by inserting the missing scheme.
            linkToOpen = "http://" + linkToOpen;
        }
        Uri uri = Uri.parse(linkToOpen);
        ResolveInfo defaultBrowserInfo = BrowserUtils.getDefaultBrowserInfo(this);
        if (defaultBrowserInfo != null) {
            BrowserUtils.openWith(this, uri, defaultBrowserInfo);
        } else {
            BrowserUtils.openWithChooser(this, uri);
        }

        finish();
    }
}
