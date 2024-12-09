package Blockchain.Core;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

import Blockchain.CryptoBalami.Transaction;
import Blockchain.CryptoBalami.TransactionOutput;
import Blockchain.CryptoBalami.Wallet;
import com.google.gson.GsonBuilder;

public class BalamiChainMain {

    public static ArrayList<Block> blockchain = new ArrayList<>();
    //list of all unspent transactions.
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
    public static int difficulty = 5;
    public static Wallet walletA;
    public static Wallet walletB;
    public static float minimumTransaction;

    public static void main(String[] args) {

//        blockchain.add(new Block("Hi, I am first block", "0"));
//        System.out.println("Trying to Mine Block 1 ..... ");
//        blockchain.get(0).mineBlock(difficulty);
//
//        blockchain.add(new Block("Hi, I am second block", blockchain.get(blockchain.size()-1).hash));
//        System.out.println("Trying to Mine Block 2 ..... ");
//        blockchain.get(1).mineBlock(difficulty);
//
//        blockchain.add(new Block("Hi, I am third block", blockchain.get(blockchain.size()-1).hash));
//        System.out.println("Trying to Mine Block 3 ..... ");
//        blockchain.get(2).mineBlock(difficulty);
//
//        System.out.println("\nBlockchain is Valid: " + isChainValid());
//
//        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
//        System.out.println("\nThe block chain: ");
//        System.out.println(blockchainJson);

        //Setup Bouncey castle as a Security Provider
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        //Create the new wallets
        walletA = new Wallet();
        walletB = new Wallet();

        //Test public and private keys
        System.out.println("Private and public keys:");
        System.out.println(DigitalFingerPrintGenerator.getStringFromKey(walletA.privateKey));
        System.out.println(DigitalFingerPrintGenerator.getStringFromKey(walletA.publicKey));

        //Create a test transaction from WalletA to walletB
        Transaction transaction = new Transaction(walletA.publicKey, walletB.publicKey, 5, null);
        transaction.generateSignature(walletA.privateKey);

        //Verify the signature works and verify it from the public key
        System.out.println("Is signature verified");
        System.out.println(transaction.verifiySignature());
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        // Check the blockchain through loop
        for (int i = 1; i < blockchain.size(); i++ ) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);

            // compare pre block and calculated blocks
            if(!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }

            //compare pre hash and registered pre hash
            if (!previousBlock.hash.equals(currentBlock.previousBlockHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }

            // Checked if hash is solved
            if (!currentBlock.hash.substring(0,difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't mined");
                return false;
            }
        }
        return true;
    }

}

