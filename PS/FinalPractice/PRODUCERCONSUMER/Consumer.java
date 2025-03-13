public class Consumer extends Thread {
    private MessageBox box;
    private int money;
    private String name;

    public Consumer(String name, MessageBox box, int money){
        this.name = name;
        this.box = box;
        this.money = money;
    }

    public void run(){
        try {
            String message;
            while (!(message = box.take()).equals("EOF") && money >= 5){
                System.out.println(message);
                money -= 5;
                System.out.println(name + " bought " + message + "; Money remaining: $" + money);
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
