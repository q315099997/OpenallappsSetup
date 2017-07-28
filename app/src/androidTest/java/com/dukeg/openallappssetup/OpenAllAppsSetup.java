package com.dukeg.openallappssetup;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@SdkSuppress(minSdkVersion = 19)
@RunWith(AndroidJUnit4.class)
public class OpenAllAppsSetup {
    private UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    private static final String TAG = "Open all apps setup";

    //Open an app by its package name
    public void launch(String packageName, int timeout) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // Wait for launcher
        final String launcherPackage = resolveInfo.activityInfo.packageName;

        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), timeout);

        // Launch the blueprint app
        Context context = InstrumentationRegistry.getContext();
        final Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // Clear out any previous instances
        context.startActivity(LaunchIntent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), timeout);
    }
    @Before
    public void setup(){
        mDevice.pressHome();
    }

    @Test
    public void run(){
        Context context = InstrumentationRegistry.getContext();
        PackageManager mPackageManger = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = mPackageManger.queryIntentActivities(intent, 0);
        final ArrayList<String> packageNameList = new ArrayList<>();
        for (Object o : ((List) resolveInfoList)) {
            ResolveInfo resolveInfo = (ResolveInfo) o;
            if (!packageNameList.contains(resolveInfo.activityInfo.packageName)) {
                packageNameList.add(resolveInfo.activityInfo.packageName);
            }
        }
        packageNameList.remove("com.dukeg.openallappssetup");
        String numOfApps = "Total " + packageNameList.size() + " apps";
        Log.i(TAG, numOfApps);

        for (int i = 0; i < packageNameList.size(); i++) {
            String launchPackageLog = "No "+ i + " app " + packageNameList.get(i) + " launched";
            Log.i(TAG,launchPackageLog);
            launch(packageNameList.get(i), 3000);
            if (mDevice.wait(Until.hasObject((By.text("继续"))), 3000)){
                mDevice.wait(Until.findObject(By.text("继续")), 3000).click();
                Log.i(TAG, "Clicked 继续");
            }
            if (mDevice.wait(Until.hasObject((By.text("同意"))), 3000)){
                mDevice.wait(Until.findObject(By.text("同意")), 3000).click();
                Log.i(TAG, "Clicked 同意");
            }
            if (mDevice.wait(Until.hasObject((By.text("进入"))), 3000)){
                mDevice.wait(Until.findObject(By.text("进入")), 3000).click();
                Log.i(TAG, "Clicked 进入.");
            }
            if (mDevice.wait(Until.hasObject((By.text("试用"))), 3000)){
                mDevice.wait(Until.findObject(By.text("试用")), 3000).click();
                Log.i(TAG, "Clicked 试用");
            }
            if (mDevice.wait(Until.hasObject((By.text("跳过"))), 3000)){
                mDevice.wait(Until.findObject(By.text("跳过")), 3000).click();
                Log.i(TAG, "Clicked 跳过");
            }
            if (mDevice.wait(Until.hasObject((By.text("忽略"))), 3000)){
                mDevice.wait(Until.findObject(By.text("忽略")), 3000).click();
                Log.i(TAG, "Clicked 忽略");
            }
            mDevice.pressBack();
            if (mDevice.wait(Until.hasObject((By.text("退出"))), 3000)){
                mDevice.wait(Until.findObject(By.text("退出")), 3000).click();
                Log.i(TAG, "Clicked 退出");
            }
            mDevice.pressBack();
            mDevice.pressBack();
            Log.i(TAG, "Exit app");
        }
        Log.i(TAG, "All jobs done");
    }
}
