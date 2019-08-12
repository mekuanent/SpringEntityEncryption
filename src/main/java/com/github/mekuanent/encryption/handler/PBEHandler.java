package com.github.mekuanent.encryption.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;


/**
 * A default Encryption Handler algorithm, which can only be assigned as global com.mekuanent.encryption handler
 * it uses PBEWithHmacSHA512AndAES_128 algorithm
 *
 * @author Mekuanent Kassaye
 */
public class PBEHandler implements IEncryptionHandler {

    private PBEParameterSpec ivSpec;
    private SecretKeySpec secret;
    private Exception ex;

    private static final Logger log = LoggerFactory.getLogger(PBEHandler.class);

    /**
     * Constructs PBEHandler with the following parameters
     * @param password choose a strong key for a better com.mekuanent.encryption
     * @param salt Salt for Encryption
     * @param iv IV for encryption
     * @param iteration the number of times the encryption has to iterated/layered
     */
    public PBEHandler(@NotNull char[] password, @NotNull byte[] salt, @NotNull byte[] iv,
                      int iteration) {
        this(password, salt, iv, iteration, 256);
    }

    /**
     * Constructs PBEHandler with the following parameters
     * @param password choose a strong key for a better com.mekuanent.encryption
     * @param salt salt for encryption
     * @param iv IV for encryption
     * @param derivedKeyLength the length of the salt + password combination key, default is 256
     * @param iteration the number of times the encryption has to iterated/layered
     */
    public PBEHandler(@NotNull char[] password, @NotNull byte[] salt, @NotNull byte[] iv,
                      int iteration, int derivedKeyLength) {

        try{
            KeySpec keySpec = new PBEKeySpec(password, salt, iteration, derivedKeyLength * 8);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            SecretKey tmp = f.generateSecret(keySpec);
            secret = new SecretKeySpec(tmp.getEncoded(), "PBEWithHmacSHA512AndAES_128");

            ivSpec = new PBEParameterSpec(iv, 4096, new IvParameterSpec(new byte[16]));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            ex = e;
        }


    }

    /**
     * handles com.mekuanent.encryption of the provided text
     * @param raw raw input string
     * @return if successful encrypted base64 string otherwise {@literal null}, error messages will be printed if it is null
     */
    @Override
    public String encrypt(String raw) {

        if(ivSpec == null || secret == null){
            ex.printStackTrace();
            return null;
        }

        try {
            Cipher c = Cipher.getInstance("PBEWITHHMACSHA512ANDAES_128");
            c.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
            byte[] cipherText = c.doFinal(raw.getBytes());

            return Base64.getEncoder().encodeToString(cipherText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * handles decryption of the provided text
     * @param cipher encrypted base64 string
     * @return if successful decrypted string otherwise {@literal null}, error messages will be printed if it is null
     */
    @Override
    public String decrypt(String cipher) {

        try {
            Cipher c = Cipher.getInstance("PBEWithHmacSHA512AndAES_128");
            c.init(Cipher.DECRYPT_MODE, secret, ivSpec);
            byte[] rawText = c.doFinal(Base64.getDecoder().decode(cipher));
            return new String(rawText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            log.error("Decode Couldn't work because Last unit does not have enough valid bits, This usually happens when you have un encrypted data in your entity");
        }

        return null;
    }
}
