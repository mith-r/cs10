

public class Producer extends Thread {
    private MessageBox box;
    private int numberToSend;
    private String name;

    public Producer(String name, MessageBox box, int numberToSend){
        this.box = box; this.numberToSend = numberToSend;
        this.name = name;
    }

    public void run(){
        try{
            for (int i = 0; i < numberToSend; i ++){
                sleep((int)(Math.random()*5000));
                box.put("message #" + i);
                System.out.println(name + " put message #" + i + " into the message box");
            }
            box.put("EOF"); //EOF means end of file
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
