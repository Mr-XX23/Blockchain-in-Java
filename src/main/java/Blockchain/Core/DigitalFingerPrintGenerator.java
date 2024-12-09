package Blockchain.Core;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class DigitalFingerPrintGenerator {
    public static String applySHA256;

    public static String applySHA256(String dataForGeneratingHash) {
        try {
            //Get Instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Applies SHA-256 to our data
            byte[] hash = digest.digest(dataForGeneratingHash.getBytes(StandardCharsets.UTF_8));
            // Initialize to store hash as a hexadecimal
            StringBuilder generatedHash = new StringBuilder();
            // Create a hash
            for (byte h : hash){
                String hex = Integer.toHexString(0xff & h);
                  // System.out.println(hex);
                if( hex.length() == 1 ) generatedHash.append('0');
                generatedHash.append(hex);
            }
            // return hashedString
            return generatedHash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //Applies ECDSA Signature and returns the result ( as bytes ).
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output = new byte[0];
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            byte[] realSig = dsa.sign();
            output = realSig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    //Verifies a String signature
    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            return ecdsaVerify.verify(signature);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }
}
