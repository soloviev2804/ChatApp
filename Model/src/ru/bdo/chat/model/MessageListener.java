package ru.bdo.chat.model;

import java.util.EventListener;
import java.util.List;

/**
 * @author solovyev.vladimir
 * @since 11.09.2015
 */
public interface MessageListener extends EventListener {

    void messageAdded(List<Message> newMessages);
}

