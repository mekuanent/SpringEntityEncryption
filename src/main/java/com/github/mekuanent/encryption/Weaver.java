package com.github.mekuanent.encryption;

import com.github.mekuanent.encryption.annotation.Encrypted;
import com.github.mekuanent.encryption.exception.NoEncryptionHandlerDefined;
import com.github.mekuanent.encryption.handler.EncryptionHandler;
import com.github.mekuanent.encryption.handler.IEncryptionHandler;
import com.github.mekuanent.encryption.handler.GenericHandler;
import com.github.mekuanent.encryption.registry.ContextHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Utility class to handle Annotation and Reflection processing
 *
 * @author Mekuanent Kassaye
 */
public class Weaver {

    /**
     * Enumerates all annotated fields with the specified annotation type
     * @param obj the object to do reflection on
     * @param cl the annotation type
     * @return list of annotated fields
     */
    public static List<Field> getAnnotatedFields(Object obj, Class<? extends Annotation> cl){

        Field[] fields = obj.getClass().getDeclaredFields();

        return Arrays.stream(fields)
                .filter(field -> field.getAnnotation(cl) != null && field.getType() == String.class)
                .collect(Collectors.toList());

    }

    /**
     * Gets the content of both private and non-private fields for the specified object
     * @param obj the object to do reflection on
     * @param field the field which has the value
     * @return the value of the field in the specified object
     */
    public static String getContent(Object obj, Field field){
        field.setAccessible(true);
        try {
             return (String)field.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the specified content on both private and non-private fields for the specified object
     * @param obj the object to do reflection on
     * @param field the field which has the value
     * @param newText the new String value to be set on property
     * @return {@literal true} if it is successful, {@literal false} if not.
     */
    public static boolean setContent(Object obj, Field field, String newText){
        field.setAccessible(true);
        try {
            field.set(obj, newText);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Chooses the desired {@link IEncryptionHandler EncryptionHandler} for the specified field
     * @param field the annotated field
     * @return the desired {@link IEncryptionHandler EncryptionHandler} if either global or local handler is set, otherwise {@literal null}
     */
    public static IEncryptionHandler decideEncryptionHandler(Field field){
        Class<? extends IEncryptionHandler> cl = field.getDeclaredAnnotation(Encrypted.class).handler();

        IEncryptionHandler handler;

        if(cl == GenericHandler.class){

            if(EncryptionHandler.handler == null){
                try {
                    throw new NoEncryptionHandlerDefined();
                } catch (NoEncryptionHandlerDefined noEncryptionHandlerDefined) {
                    noEncryptionHandlerDefined.printStackTrace();
                }
                return null;
            }

            handler = EncryptionHandler.handler;

        }else{
            handler = ContextHelper.getBean(cl);
        }

        return handler;
    }

}
