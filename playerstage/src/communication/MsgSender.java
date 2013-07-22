package communication;

import java.util.Observable;

public class MsgSender extends Observable {

	private Message msg;
	private String senderName;
	// private boolean acknowledged;
	private int ackCount = 0;

	public boolean isAcknowledged() {
		if (ackCount == 4) {
			System.out.println("[Message " + msg.getId() + " acknowledged]");
			ackCount = 0;
			return true;
		} else {
			return false;
		}
	}

	synchronized public void setAcknowledged(boolean acknowledged) {
		if (acknowledged)
			ackCount++;
	}

	public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public String getName() {
		return senderName;
	}

	public void setSender(String sender) {
		this.senderName = sender;
	}

	public MsgSender(String sender) {
		this.senderName = sender;
	}

	public void broadcastMsg() {
		generateMsg();
		setChanged();
		notifyObservers();
		// System.out.println("BroadcastMsg:" + msg.getId() + "_from_"+
		// msg.getSender());
	}

	public void releaseResourceMsg() {
		generateReleaseResourceMsg();
		setChanged();
		notifyObservers();
	}

	public Message getMessage() {
		return this.msg;
	}

	private void generateReleaseResourceMsg() {
		msg.setReleaseFlag(true);
	}

	private void generateMsg() {
		msg = new Message();
		msg.setSender(this);
	}
}
