package nfc.activity.monitor;

import android.util.Log;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import nfc.activity.monitor.util.Converter;

/*
 * The MIT License
 *
 * Copyright 2016 Victor Santiago.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
