package com.github.mekuanent.encryption.exception;

import com.github.mekuanent.encryption.handler.IEncryptionHandler;

/**
 * This exception is thrown when no {@link IEncryptionHandler EncryptionHandler}
 * is found for the field which should be defined either globally or locally
 *
 * @author Mekuanent Kassaye
 */
public class NoEncryptionHandlerDefined extends Exception {

    /**
     * Constructs a NoEncryptionHandlerDefined with default
     * message.
     */
    public NoEncryptionHandlerDefined() {
        super("No Encryption Handler Defined. Please set it on EncryptionHandler or individually on the fields");
    }
}
