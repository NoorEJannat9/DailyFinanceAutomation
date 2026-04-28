package pages.utils;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class EmailUtils {

    public static String getResetLink(String username, String appPassword) throws Exception {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, appPassword);

        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_ONLY);

        // Get all unread messages and find the latest one
        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        if (messages.length == 0) {
            inbox.close(false);
            store.close();
            return null;
        }

        // Get the last (most recent) unread message
        Message latestMessage = messages[messages.length - 1];
        String content = latestMessage.getContent().toString();

        inbox.close(false);
        store.close();

        return content;
    }
}