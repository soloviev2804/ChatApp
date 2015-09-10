package ru.bdo.chat.view;

import oracle.adf.view.rich.activedata.BaseActiveDataModel;
import oracle.adf.view.rich.event.ActiveDataEntry;
import oracle.adf.view.rich.event.ActiveDataUpdateEvent;
import oracle.adf.view.rich.model.ActiveCollectionModelDecorator;
import oracle.adf.view.rich.model.ActiveDataModel;
import oracle.adfinternal.view.faces.activedata.ActiveDataEventUtil;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.SortableModel;
import ru.bdo.chat.model.Message;
import ru.bdo.chat.model.MessageListener;
import ru.bdo.chat.model.MessageStorage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Chat table model for ADS
 * @author solovyev.vladimir
 * @since 11.09.2015
 */
public class ChatCollectionModel extends ActiveCollectionModelDecorator {

    private final ChatActiveDataModel activeModel = new ChatActiveDataModel();
    private final CollectionModel model = new SortableModel(MessageStorage.getInstance().getMessages());

    public ActiveDataModel getActiveDataModel() {
        return activeModel;
    }

    public CollectionModel getCollectionModel() {
        return model;
    }

    private class ChatActiveDataModel extends BaseActiveDataModel {

        private final AtomicInteger changeCount = new AtomicInteger(0);
        private final AdsMessageListener adsMessageListener = new AdsMessageListener();

        public int getCurrentChangeCount() {
            return changeCount.get();
        }

        protected void startActiveData(Collection<Object> rowKeys, int startChangeCount) {
            MessageStorage.getInstance().addListener(adsMessageListener);
        }

        protected void stopActiveData(Collection<Object> rowKeys) {
            MessageStorage.getInstance().removeListener(adsMessageListener);
        }

        private class AdsMessageListener implements MessageListener {
            public void messageAdded(List<Message> newMessages) {
                for (Message m : newMessages) {
                    ActiveDataUpdateEvent event = ActiveDataEventUtil.buildActiveDataUpdateEvent(
                            ActiveDataEntry.ChangeType.INSERT_BEFORE, changeCount.incrementAndGet(),
                            new Object[]{0},
                            null, new String[]{"id"},
                            new Object[]{m.getId()});
                    ChatActiveDataModel.this.fireActiveDataUpdate(event);
                }
            }
        }

    }

}