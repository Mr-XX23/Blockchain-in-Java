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
    public static int difficulty = 3;
    public static Wallet walletA;
    public static Wallet walletB;
    public static float minimumTransaction = 0.1f;
    public static Transaction genesisTransaction;

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
        Wallet coinbase = new Wallet();

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction
        genesisTransaction.transactionId = "0"; //manually set the transaction id
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //it's important to store our first transaction in the UTXOs list.

        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        //testing
        Block block1 = new Block(genesis.hash);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
        addBlock(block1);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block2 = new Block(block1.hash);
        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
        addBlock(block2);
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        Block block3 = new Block(block2.hash);
        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        isChainValid();
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
        System.out.println("Blockchain is valid");
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}

