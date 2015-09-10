package ru.bdo.chat.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author solovyev.vladimir
 * @since 10.09.2015
 */
public class MessageStorage {
    private static final Log log = LogFactory.getLog(MessageStorage.class);

    private static final int SIZE = 100;

    private List<Message> storage = new CopyOnWriteArrayList<Message>();
    private List<MessageListener> listeners = new CopyOnWriteArrayList<MessageListener>();

    private static class MessageStorageHolder {
        private static MessageStorage messageStorageInstance = new MessageStorage();
    }

    public static MessageStorage getInstance() {
        return MessageStorageHolder.messageStorageInstance;
    }

    public List<Message> getMessages() {
        return storage;
    }

    public void save(Message msg) {
        boolean added = storage.add(msg);
        while (added && storage.size() > SIZE) {
            storage.remove(0);
        }
        fireEvent(storage);
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void removeListener(MessageListener listener) {
        listeners.remove(listener);
    }

    private void fireEvent(List<Message> newMessages) {
        for (MessageListener listener : listeners) {
            listener.messageAdded(newMessages);
        }
    }
}
