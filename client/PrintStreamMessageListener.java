package client;

import common.MessageListener;
import common.MessageSource;

import java.io.PrintStream;

public class PrintStreamMessageListener implements MessageListener {
    private PrintStream out;

    /**
     * default construtor
     * @param out where it wants to be sent out to
     */
    public PrintStreamMessageListener(PrintStream out){
        this.out = out;
    }

    /**
     * if a message is recieved then we flush it
     * @param message The message received by the subject
     * @param source The source from which this message originated (if needed).
     */
    public void messageReceived(String message, MessageSource source){
        out.append(message + "\n");
        out.flush();
    }

    /**
     * closes the source
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source){
        source.removeMessageListener(this);
        out.close();
    }
}
