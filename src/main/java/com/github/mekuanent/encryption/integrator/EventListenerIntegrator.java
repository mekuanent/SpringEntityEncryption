package com.github.mekuanent.encryption.integrator;

import com.github.mekuanent.encryption.listener.InsertEventListener;
import com.github.mekuanent.encryption.listener.LoadEventListener;
import com.github.mekuanent.encryption.registry.HibernateSpringIntegratorRegistry;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.*;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


/**
 * A Service to register EventListener Components
 *
 * @author Mekuanent Kassaye
 */
@Service
public class EventListenerIntegrator {

    private static final Logger log = LoggerFactory.getLogger(EventListenerIntegrator.class);

    @Autowired
    private HibernateEntityManagerFactory entityManagerFactory;

    @Autowired
    private HibernateSpringIntegratorRegistry hibernateSpringIntegratorRegistry;


    /**
     * Obtains EventListener beans and registers them to the listener registry
     */
    @PostConstruct
    public void registerListeners() {
        log.debug("Registering Spring managed HibernateEventListeners");

        EventListenerRegistry listenerRegistry = ((SessionFactoryImpl) entityManagerFactory
                .getSessionFactory()).getServiceRegistry().getService(
                EventListenerRegistry.class);
        List<com.github.mekuanent.encryption.listener.LoadEventListener> loadEventListeners = hibernateSpringIntegratorRegistry
                .getHibernateLoadEventListeners();

        List<InsertEventListener> insertEventListeners = hibernateSpringIntegratorRegistry
                .getHibernateInsertEventListeners();

        for (LoadEventListener hel : loadEventListeners) {
            log.debug("Registering: {}", hel.getClass());

            PostLoadEventListener.class.isAssignableFrom(hel.getClass());
            listenerRegistry.appendListeners(EventType.POST_LOAD,
                    (PostLoadEventListener) hel);
        }

        for (InsertEventListener hel : insertEventListeners) {
            log.debug("Registering: {}", hel.getClass());

            PreInsertEventListener.class.isAssignableFrom(hel.getClass());

            listenerRegistry.appendListeners(EventType.FLUSH_ENTITY,
                    InsertEventListener.class);
        }
    }
}
