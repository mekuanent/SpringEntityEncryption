package com.mekuanent.encryption.listener;

import com.mekuanent.encryption.Weaver;
import com.mekuanent.encryption.annotation.Encrypted;
import com.mekuanent.encryption.handler.EncryptionHandler;
import com.mekuanent.encryption.handler.IEncryptionHandler;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


/**
 * An Event Listener which is triggered Just before an entity is inserted or updated
 *
 * @author Mekuanent Kassaye
 */
@Component
public class InsertEventListener implements PreInsertEventListener, PreUpdateEventListener,
        FlushEntityEventListener, PreCollectionUpdateEventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoadEventListener.class);
    public static final LoadEventListener INSTANCE =
            new LoadEventListener();

    /**
     * triggered Just before an entity is inserted, invoking the com.mekuanent.encryption algorithm on annotated fields
     * of the entity
     *
     * @param preInsertEvent an event object containing the entity
     */
    @Override
    public boolean onPreInsert(PreInsertEvent preInsertEvent) {

        return process(preInsertEvent.getEntity());

    }

    /**
     * handles annotation processing and com.mekuanent.encryption
     * @param entity Entity object under operation
     */
    private boolean process(final Object entity){

        List<Field> fields = Weaver.getAnnotatedFields(entity, Encrypted.class);
        fields.forEach(field -> {
            String text = Weaver.getContent(entity, field);
            if(text != null && !text.isEmpty()){

                IEncryptionHandler handler = Weaver.decideEncryptionHandler(field);

                if(handler != null){
                    String cipher = handler.encrypt(text);
                    if(cipher != null){
                        Weaver.setContent(entity, field, cipher);
                    }
                }
            }
        });

        return false;
    }

    /**
     * triggered Just before an entity is updated, invoking the com.mekuanent.encryption algorithm on annotated fields
     * of the entity
     *
     * @param preUpdateEvent an event object containing the entity
     */
    @Override
    public boolean onPreUpdate(PreUpdateEvent preUpdateEvent) {
        return process(preUpdateEvent.getEntity());
    }

    @Override
    public void onFlushEntity(FlushEntityEvent flushEntityEvent) throws HibernateException {
        process(flushEntityEvent.getEntity());
        flushEntityEvent.setDirtyProperties(new int[]{0, 1, 2});
        int[] dirty = flushEntityEvent.getDirtyProperties();
        flushEntityEvent.setDirtyCheckPossible(true);
        flushEntityEvent.setHasDirtyCollection(true);
    }

    @Override
    public void onPreUpdateCollection(PreCollectionUpdateEvent preCollectionUpdateEvent) {
        System.out.println("sandsad");
    }
}
