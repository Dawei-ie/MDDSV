package communication;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import communication.*;

/**
 * @author ydw The class simulate the communication device within a car.
 */
public class Communication {

	List<MsgSender> messageSenders = new ArrayList<MsgSender>();
	List<MsgReceiver> messageReceivers = new ArrayList<MsgReceiver>();
	MsgSender msgSender;
	MsgReceiver msgReceiver;

	private void addSenders() {
		messageSenders.add(new MsgSender("car1"));
		messageSenders.add(new MsgSender("car2"));
	}

	private void addReceivers() {
		// messageReceivers.add(new MsgReceiver(messageSenders, "car1"));
		// messageReceivers.add(new MsgReceiver(messageSenders, "car2"));
	}

	public Communication(String id) {
		addSenders();
		addReceivers();
		msgSender = new MsgSender(id);
//		msgReceiver = new MsgReceiver(messageSenders, id);

	}

	public void run() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				for (MsgSender sender : messageSenders) {
					sender.addObserver(msgReceiver);
				}
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msgSender.broadcastMsg();
				}
			}
		};
		thread.start();
	}

	/**
	 * @param args
	 *            For test use.
	 */
	public static void main(String[] args) {

		Communication c = new Communication("Car_1");
		c.run();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Communication c1 = new Communication("Car_2");
		c1.run();
	}
}
