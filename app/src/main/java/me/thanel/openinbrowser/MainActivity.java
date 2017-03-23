package me.thanel.openinbrowser;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(Intent.EXTRA_TEXT)) {
            Toast.makeText(this, R.string.error_find_link, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Uri uri = Uri.parse(intent.getStringExtra(Intent.EXTRA_TEXT));
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        ResolveInfo defaultBrowserInfo = getDefaultBrowserInfo();

        if (defaultBrowserInfo == null) {
            openNormally(sendIntent);
            finish();
            return;
        }

        sendIntent.setPackage(defaultBrowserInfo.activityInfo.packageName);
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendIntent);
        } else {
            openNormally(sendIntent);
        }

        finish();
    }

    private void openNormally(Intent sendIntent) {
        sendIntent.setPackage(null);
        Toast.makeText(this, R.string.warning_no_default_browser, Toast.LENGTH_SHORT).show();
        startActivity(sendIntent);
    }

    private ResolveInfo getDefaultBrowserInfo() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"));
        return getPackageManager().resolveActivity(browserIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
    }
}
