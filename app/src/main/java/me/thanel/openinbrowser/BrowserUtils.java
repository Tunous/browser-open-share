package me.thanel.openinbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

public class BrowserUtils {
    private BrowserUtils() {
        // nothing to do
    }

    /**
     * Get package name of users's default browser.
     *
     * @return Package name of user's default browser or null if user has no default browser.
     */
    public static String getDefaultBrowserPackageName(Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"));
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(browserIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo == null) {
            return null;
        }
        return resolveInfo.activityInfo.packageName;
    }

    /**
     * Open the provided {@code uri} using the activity with the specified {@code packageName}.
     */
    public static void openWith(Context context, Uri uri, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                .setPackage(packageName);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else if (packageName != null) {
            // Fallback to opening link in any app that can handle it
            openWith(context, uri, null);
        } else {
            Toast.makeText(context, R.string.error_no_browser, Toast.LENGTH_LONG).show();
        }
    }
}
