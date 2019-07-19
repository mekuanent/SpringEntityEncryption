package com.github.mekuanent.encryption.listener;

import com.github.mekuanent.encryption.Weaver;
import com.github.mekuanent.encryption.annotation.Encrypted;
import com.github.mekuanent.encryption.handler.IEncryptionHandler;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.event.spi.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * An Event Listener which is triggered Just before an entity is inserted or updated
 *
 * @author Mekuanent Kassaye
 */
@Component
public class InsertEventListener implements
        FlushEntityEventListener {

    private static final Logger logger = LoggerFactory.getLogger(InsertEventListener.class);


    /**
     * handles annotation processing and com.mekuanent.encryption
     * @param entity Entity object under operation
     */
    private boolean process(final Object entity){

        List<Field> fields = Weaver.getAnnotatedFields(entity, Encrypted.class);

        AtomicBoolean result = new AtomicBoolean(true);

        fields.forEach(field -> {
            String text = Weaver.getContent(entity, field);
            if(text != null && !text.isEmpty()){

                if(text.contains("\\@{encrypted}@/")) result.set(false);

                IEncryptionHandler handler = Weaver.decideEncryptionHandler(field);

                if(handler != null){
                    String cipher = handler.encrypt(text);
                    if(cipher != null){
                        Weaver.setContent(entity, field, "\\@{encrypted}@/"+cipher);
                    }
                }
            }
        });

        return result.get();
    }


    /**
     * triggered Just before an entity is inserted, invoking the com.mekuanent.encryption algorithm on annotated fields
     * of the entity
     *
     * @param flushEntityEvent an event object containing the entity
     */
    @Override
    public void onFlushEntity(FlushEntityEvent flushEntityEvent) throws HibernateException {


        Object entity = flushEntityEvent.getEntity();
        Session session = flushEntityEvent.getSession();

        if(process(entity)) {
            session.flush();
        }
    }
}
