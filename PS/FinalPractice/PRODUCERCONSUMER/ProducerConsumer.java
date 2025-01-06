/**
 * Solutions to problem 2 of cs10 final practice exam
 * Producer/consumer example
 * Producer sends messages to consumer by way of MessageBox semaphore with synchronized send/receive methods.
 *
 * @author warrenshepard, Spring 2023
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Spring 2014 to separate out helper classes
 * @author Tim Pierson, Dartmouth CS 10, Spring 2019, updated to pass Producer number of messages to send
 */
public class ProducerConsumer {
	public static final int numMessages = 20;	// how many messages to send from producer to consumer
	private Producer producer;
	private Consumer consumer1;
	private Consumer consumer2;

	public ProducerConsumer() {
		MessageBox box = new MessageBox();	
		producer = new Producer("producer 1", box,numMessages);
		consumer1 = new Consumer("consumer 1", box, 200);
		consumer2 = new Consumer("consumer 2", box, 350);
	}

	/**
	 * Just starts the producer and consumer running
	 */
	public void communicate() {
		producer.start();	
		consumer1.start();
		consumer2.start();
	}

	public static void main(String[] args) {
		new ProducerConsumer().communicate();
	}
}