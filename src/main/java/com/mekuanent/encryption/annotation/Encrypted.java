package com.mekuanent.encryption.annotation;

import com.mekuanent.encryption.handler.GenericHandler;
import com.mekuanent.encryption.handler.IEncryptionHandler;

import java.lang.annotation.*;


/**
 * Indicates a field that should be encrypted when it is stored and decrypted when it is accessed
 * It accepts an optional {@link IEncryptionHandler com.mekuanent.encryption handler} if the field to be encrypted needs
 * local com.mekuanent.encryption configuration
 * @author Mekuanent Kassaye
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypted {

    /**
     * Apply a specific/local com.mekuanent.encryption handler to the field
     * @return the com.mekuanent.encryption handler specific to the field
     */
    Class<? extends IEncryptionHandler> handler() default GenericHandler.class;

}
