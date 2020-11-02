package it.algos.webbase.web.lib;


import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Library of cryptographic functions.
 */
public class LibCrypto {

    private static final DESStringEncrypter encrypter = new DESStringEncrypter();

    /**
     * Standard string encryption.
     * Encrypts in DES and encodes Base64.
     * Uses a default cipher with a standard key.
     */
    public static String encrypt(String in) {
        String out = null;
        if(in!=null){
            out = encrypter.encrypt(in);
        }
        return out;
    }


    /**
     * Standard string decryption
     */
    public static String decrypt(String in) {
        String out = null;
        if(in!=null){
            out=encrypter.decrypt(in);
        }
        return out;
    }


    /**
     * Encrypter/decrypter.
     * Encrypts/decrypts in des and encodes/decodes in Base64
     */
    private static class DESStringEncrypter {

        private static Cipher ecipher;
        private static Cipher dcipher;

        private static SecretKey key;

        public DESStringEncrypter() {
            try {

                //            // generate secret key using DES algorithm
                //            key = KeyGenerator.getInstance("DES").generateKey();

                // use a fixed arbitrary key
                byte[] keyBytes = new byte[]{(byte) 59, (byte) -122, (byte) 112, (byte) 112, (byte) -2, (byte) -83, (byte) -83, (byte) 21};
                key = new SecretKeySpec(keyBytes, "DES");

                ecipher = Cipher.getInstance("DES");
                dcipher = Cipher.getInstance("DES");

                // initialize the ciphers with the given key
                ecipher.init(Cipher.ENCRYPT_MODE, key);
                dcipher.init(Cipher.DECRYPT_MODE, key);


            } catch (NoSuchAlgorithmException e) {
                System.out.println("No Such Algorithm:" + e.getMessage());
                return;
            } catch (NoSuchPaddingException e) {
                System.out.println("No Such Padding:" + e.getMessage());
                return;
            } catch (InvalidKeyException e) {
                System.out.println("Invalid Key:" + e.getMessage());
                return;
            }

        }


        public static String encrypt(String str) {

            try {

                // encode the string into a sequence of bytes using the named charset
                // storing the result into a new byte array.
                byte[] utf8 = str.getBytes("UTF8");
                byte[] enc = ecipher.doFinal(utf8);

                // encode to base64
                enc = BASE64EncoderStream.encode(enc);

                return new String(enc);

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;

        }

        public static String decrypt(String str) {

            try {

                // decode with base64 to get bytes
                byte[] dec = BASE64DecoderStream.decode(str.getBytes());
                byte[] utf8 = dcipher.doFinal(dec);

                // create new string based on the specified charset
                return new String(utf8, "UTF8");

            } catch (Exception e) {

                e.printStackTrace();

            }

            return null;

        }

    }
}
