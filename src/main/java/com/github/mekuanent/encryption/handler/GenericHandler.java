package com.github.mekuanent.encryption.handler;

import org.springframework.stereotype.Component;


/**
 * A generic and default {@link IEncryptionHandler EncryptionHandler} which ignores com.mekuanent.encryption
 *
 * @author Mekuanent Kassaye
 */
@Component
public class GenericHandler implements IEncryptionHandler {

    /**
     * handles com.mekuanent.encryption of the provided text
     * @param raw raw input text
     * @return always returns {@literal null}
     */
    @Override
    public String encrypt(String raw) {
        return null;
    }

    /**
     * handles decryption
     * @param cipher encrypted base64 text
     * @return always returns {@literal null}
     */
    @Override
    public String decrypt(String cipher) {
        return null;
    }
}
