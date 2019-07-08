package com.mekuanent.encryption.registry;

import com.mekuanent.encryption.listener.InsertEventListener;
import com.mekuanent.encryption.listener.LoadEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Binds EventListeners
 *
 * @author Mekuanent Kassaye
 */
@Component
public class HibernateSpringIntegratorRegistry {

    @Autowired(required = false)
    private List<LoadEventListener> hibernateLoadEventListeners;

    @Autowired(required = false)
    private List<InsertEventListener> hibernateInsertEventListeners;

    /**
     * fetches all {@link LoadEventListener LoadEventListeners}
     * @return list of {@link LoadEventListener LoadEventListeners}
     */
    public List<LoadEventListener> getHibernateLoadEventListeners() {
        if (hibernateLoadEventListeners == null) {
            return Collections.emptyList();
        }
        return hibernateLoadEventListeners;
    }

    /**
     * fetches all {@link InsertEventListener InsertEventListeners}
     * @return list of {@link InsertEventListener InsertEventListeners}
     */
    public List<InsertEventListener> getHibernateInsertEventListeners() {
        if (hibernateInsertEventListeners == null) {
            return Collections.emptyList();
        }
        return hibernateInsertEventListeners;
    }
}