package communication;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

	private AtomicInteger c = new AtomicInteger(0);

	public synchronized void increment() {
		c.incrementAndGet();
	}

	public synchronized void decrement() {
		c.decrementAndGet();
	}

	public synchronized int value() {
		return c.get();
	}
}