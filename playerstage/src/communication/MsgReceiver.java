package communication;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

public class MsgReceiver implements Observer {

	private String receiver;
	private double msgLostRate = 0;
	private Queue<Message> queue;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Queue<Message> getQueue() {
		return queue;
	}

	public MsgReceiver(String receiver) {
		this.receiver = receiver;
		queue = new LinkedList<Message>();
	}

	public boolean isDataAvailable() {
		if (!queue.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public Message peekReceivedMsg() {
		return queue.peek();
	}

	private void received(Message msg) {
		// if the message have been received before, keep the newest one and
		// delete the former
		Iterator<Message> i = queue.iterator();
		while (i.hasNext()) {
			Message m = i.next();
			if (m.getSender().equals(msg.getSender())) {
				queue.remove(m);
			}
		}
		queue.add(msg);
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if (o instanceof MsgSender) {
			MsgSender ms = (MsgSender) o;
			Message msg = ms.getMessage();
			if (msg.isReleaseFlag()) {
				queue.poll();
			} else {
				// simulate message lost here
				double randomValue = Math.random();
				if (randomValue > msgLostRate) {
					received(msg);
					msg.getSender().setAcknowledged(true);
				}
			}
			System.out.println("[receiver] " + receiver + " [msg id] "
					+ msg.getId() + " [sent from] " + msg.getSender().getName()
					+ " [rsc released] " + msg.isReleaseFlag()
					+ " [msg queue size] " + queue.size());
		}
	}
}
