package nfc.activity.monitor;

import android.util.Log;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nfc.activity.monitor.util.Converter;

/**
 * @author victor
 */
public class Monitor implements IXposedHookLoadPackage {

    private final String NFC_PACKAGE = "com.android.nfc";
    private final String NFC_TAG_SERVICE = NFC_PACKAGE + ".NfcService.TagService";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam)
            throws Throwable {
        if(!loadPackageParam.packageName.equals(NFC_PACKAGE)) return;

        //For more information on the "transceive" method, please consult:
        //https://android.googlesource.com/platform/packages/apps/Nfc/+/android-4.2.1_r1.2/src/com/android/nfc/NfcService.java
        findAndHookMethod(NFC_TAG_SERVICE, loadPackageParam.classLoader, "transceive",
                int.class, byte[].class, boolean.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                //TransceiveResult object - More info:
                //https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/nfc/TransceiveResult.java
                Object result = param.getResult();

                //Command used to access (e.g.: access keys) a sector or sector read on RFID tag.
                byte[] command = (byte[]) param.args[1];

                //Reflection to get RAW data from method...
                byte[] data = (byte[]) result.getClass().getMethod("getResponseOrThrow").invoke(result);

                //Now time to log what we got in both the Xposed log and ADB:
                log(command, data);
            }
        });

    }

    private void log(byte[] command, byte[] data) {
        //Xposed log...
        XposedBridge.log("---------------------------------------------------");
        XposedBridge.log("Command / Sector: (HEX) " + Converter.byteToHex(command));
        XposedBridge.log("Data: (HEX) " + Converter.byteToHex(data));
        XposedBridge.log("---------------------------------------------------");

        //ADB log - Filter using "NFCMonitor" on Android Studio...
        Log.i("NFCMonitor", "---------------------------------------------------");
        Log.i("NFCMonitor", "Command / Sector: (HEX) " + Converter.byteToHex(command));
        Log.i("NFCMonitor", "Data: (HEX) " + Converter.byteToHex(data));
        Log.i("NFCMonitor", "---------------------------------------------------");
    }
}
