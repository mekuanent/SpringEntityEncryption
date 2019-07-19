package com.github.mekuanent.encryption.listener;

import com.github.mekuanent.encryption.Weaver;
import com.github.mekuanent.encryption.annotation.Encrypted;
import com.github.mekuanent.encryption.handler.IEncryptionHandler;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * An Event Listener which is triggered Just after an entity is loaded
 *
 * @author Mekuanent Kassaye
 */
@Component
public class LoadEventListener implements PostLoadEventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoadEventListener.class);
    public static final LoadEventListener INSTANCE =
            new LoadEventListener();


    /**
     * triggered after an entity is loaded, invoking the decryption algorithm on annotated fields
     * of the entity
     *
     * @param postLoadEvent an event object containing the entity
     */
    @Override
    public void onPostLoad(PostLoadEvent postLoadEvent) {
        final Object entity = postLoadEvent.getEntity();


        List<Field> fields = Weaver.getAnnotatedFields(entity, Encrypted.class);
        fields.forEach(field -> {
            String cipher = Weaver.getContent(entity, field);
            if(cipher != null && !cipher.isEmpty()){

                if(cipher.length() > 15){
                    cipher = cipher.substring(15);
                    IEncryptionHandler handler = Weaver.decideEncryptionHandler(field);

                    if(handler != null){
                        String decrypted = handler.decrypt(cipher);
                        if(decrypted != null){
                            Weaver.setContent(entity, field, decrypted);
                        }
                    }
                }else if(cipher.length() == 15){
                    Weaver.setContent(entity, field, "");
                }
            }

        });
    }

}
