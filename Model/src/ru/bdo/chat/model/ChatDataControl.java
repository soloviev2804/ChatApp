package ru.bdo.chat.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import static java.lang.String.format;

/**
 * Chat bean data control
 *
 * @author solovyev.vladimir
 * @since 10.09.2015
 */
public class ChatDataControl {
    private static final Log log = LogFactory.getLog(ChatDataControl.class);

    private static final String CENSORED_WORD = "*censored*";

    private static Pattern censorPattern;

    private String userName;
    private String messageText;

    static {
        init();
    }

    private static void init() {
        Properties properties = new Properties();
        try (InputStream is = ChatDataControl.class.getResourceAsStream("chat.properties")) {
            properties.load(is);
            String prohibitedWord = properties.getProperty("prohibitedWord");
            censorPattern = Pattern.compile(prohibitedWord, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        } catch (IOException e) {
            log.error("Can't read chat properties", e);
        }
    }

    public void save() {
        if (StringUtils.isEmpty(messageText)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.error(format("save message: author %s text %s", userName, messageText));
        }
        String text = censorshipCheck(messageText);
        Message msg = new Message(userName, text);
        MessageStorage.getInstance().save(msg);
        messageText = null;
    }

    private String censorshipCheck(String input) {
        return censorPattern.matcher(input).replaceAll(CENSORED_WORD);
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
