package com.github.mekuanent.encryption.handler;

/**
 * A Utility class to set a global {@link IEncryptionHandler EncryptionHandler}
 *
 * @author Mekuanent Kassaye
 */
final public class EncryptionHandler {

    public static IEncryptionHandler handler;

    /**
     * Sets the global com.mekuanent.encryption handler
     * @apiNote This can be overriden by specifying a local com.mekuanent.encryption handler to the field
     * @param handler must not be {@literal null}.
     */
    public static void set(IEncryptionHandler handler){
        EncryptionHandler.handler = handler;
    }

    /**
     * private constructor preventing instantiation
     */
    private EncryptionHandler() {
    }
}
