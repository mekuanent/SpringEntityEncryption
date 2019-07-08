package com.mekuanent.encryption.handler;

/**
 * Interface for generic Encryption Handlers
 *
 * @author Mekuanent Kassaye
 */
public interface IEncryptionHandler {

    /**
     * handles com.mekuanent.encryption of the provided text
     * @param raw raw input string
     * @return if successful encrypted base64 string otherwise {@literal null}
     */
    String encrypt(String raw);

    /**
     * handles decryption of the provided text
     * @param cipher encrypted base64 string
     * @return if successful decrypted string otherwise {@literal null}
     */
    String decrypt(String cipher);

}
