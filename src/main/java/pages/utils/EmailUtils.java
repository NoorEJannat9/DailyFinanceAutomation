package pages.utils;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class EmailUtils {

    public static String getResetLink(String username, String password) throws Exception {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props, null);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, password);

        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_ONLY);

        // Find the latest unread email
        Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        // This is a simplified version; in a real project, you'd parse the 'content'
        // of the latest message to find the "https://..." link.
        String content = messages[messages.length - 1].getContent().toString();

        return content; // You will extract the URL from this string
    }
}