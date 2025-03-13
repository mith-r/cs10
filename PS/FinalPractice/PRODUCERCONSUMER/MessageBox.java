import java.util.ArrayList;
import java.util.List;

public class MessageBox {
    private List<String> messages;

    public MessageBox(){
        messages = new ArrayList<>();
    }

    public synchronized void put (String msg) throws InterruptedException {
        while (messages.size() >= 15){
            wait();
        }

        messages.add(msg);
        notifyAll();
    }

    public synchronized String take() throws InterruptedException {
        while (messages.size() <= 5){
            wait();
        }

        String m = messages.get(messages.size()-1);
        messages.remove(messages.size()-1);
        notifyAll();
        return m;

    }
}
