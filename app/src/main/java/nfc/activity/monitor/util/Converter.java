package nfc.activity.monitor.util;

/**
 * @author victor
 */
public class Converter {

    public static String byteToHex(byte[] bytes) {
        final StringBuilder sBuilder = new StringBuilder();

        for(byte b : bytes) {
            sBuilder.append(String.format("%02x", b));
        }

        return sBuilder.toString().toUpperCase();
    }

    public static String byteToDecimal(byte[] bytes) {
        return Long.parseLong(byteToHex(bytes), 16)+"";
    }
}
