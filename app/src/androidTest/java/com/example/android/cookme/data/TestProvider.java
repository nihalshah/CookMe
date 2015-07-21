package com.example.android.cookme.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.test.AndroidTestCase;

/**
 * Created by eduardovaca on 21/07/15.
 */
public class TestProvider extends AndroidTestCase {

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                RecipeProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: RecipeProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + RecipeContract.CONTENT_AUTHORITY,
                    providerInfo.authority, RecipeContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: RecipeProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }
}
