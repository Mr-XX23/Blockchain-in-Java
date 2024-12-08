package Blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigitalFingerPrintGenerator {
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
                System.out.println(hex);
                if( hex.length() == 1 ) generatedHash.append('0');
                generatedHash.append(hex);
            }
            // return hashedString
            return generatedHash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
