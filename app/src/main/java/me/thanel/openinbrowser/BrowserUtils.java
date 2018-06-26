package me.thanel.openinbrowser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrowserUtils {
    private BrowserUtils() {
        // nothing to do
    }

    /**
     * Get information about users's default browser.
     */
    public static ResolveInfo getDefaultBrowserInfo(Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"));
        return context.getPackageManager().resolveActivity(browserIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
    }

    /**
     * Open the provided {@code uri} using the activity described by specified
     * {@code resolveInfo} or with browser chooser if no handling activity was found.
     */
    public static void openWith(Context context, Uri uri, ResolveInfo resolveInfo) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                .setPackage(resolveInfo.activityInfo.packageName);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            openWithChooser(context, uri);
        }
    }

    /**
     * Display chooser intent giving user option to select a browser to be used to open the
     * provided {@code uri}.
     */
    public static void openWithChooser(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri)
                .addCategory(Intent.CATEGORY_BROWSABLE);
        Intent chooserIntent = createBrowserChooserIntent(context, intent, intent.getData());
        if (chooserIntent != null) {
            context.startActivity(chooserIntent);
        } else if (intent.resolveActivity(context.getPackageManager()) != null) {
            Toast.makeText(context, R.string.warning_no_browser, Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        } else {
            Toast.makeText(context, R.string.error_no_browser, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Create a chooser intent with list of all activities that can handle the specified
     * {@code uri}.
     */
    // Taken from: https://github.com/slapperwan/gh4a
    private static Intent createBrowserChooserIntent(Context context, Intent intent, Uri uri) {
        final PackageManager pm = context.getPackageManager();
        final List<ResolveInfo> activities = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        final ArrayList<Intent> chooserIntents = new ArrayList<>();
        final String ourPackageName = context.getPackageName();

        Collections.sort(activities, new ResolveInfo.DisplayNameComparator(pm));

        for (ResolveInfo resInfo : activities) {
            ActivityInfo info = resInfo.activityInfo;
            if (!info.enabled || !info.exported) {
                continue;
            }
            if (info.packageName.equals(ourPackageName)) {
                continue;
            }

            Intent targetIntent = new Intent(intent);
            targetIntent.setPackage(info.packageName);
            targetIntent.setDataAndType(uri, intent.getType());
            chooserIntents.add(targetIntent);
        }

        if (chooserIntents.isEmpty()) {
            return null;
        }

        final Intent lastIntent = chooserIntents.remove(chooserIntents.size() - 1);
        if (chooserIntents.isEmpty()) {
            // there was only one, no need to show the chooser
            return lastIntent;
        }

        Intent chooserIntent = Intent.createChooser(lastIntent, null);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                chooserIntents.toArray(new Intent[chooserIntents.size()]));
        return chooserIntent;
    }
}
