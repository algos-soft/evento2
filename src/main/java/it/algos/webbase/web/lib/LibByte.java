package it.algos.webbase.web.lib;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

/**
 * Created by gac on 31 lug 2016.
 * .
 */
public class LibByte {


    public static int byteArrayToInt(byte[] b) {
        int num = 0;
        if ((b != null) && (b.length > 0)) {
            num = b[3] & 0xFF |
                    (b[2] & 0xFF) << 8 |
                    (b[1] & 0xFF) << 16 |
                    (b[0] & 0xFF) << 24;
        }
        return num;
    }// end of static method

    public static byte[] intToByteArray(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }// end of static method

    public static byte[] bigDecimalToByteArray(BigDecimal bd) {
        double value = bd.doubleValue();
        byte[] bytes = new byte[8];
        ByteBuffer.wrap(bytes).putDouble(value);
        return bytes;
    }// end of static method

    public static BigDecimal byteArrayToBigDecimal(byte[] bytes) {
        double dbl = ByteBuffer.wrap(bytes).getDouble();
        return new BigDecimal(dbl);
    }// end of static method

}// end of abstract static class
