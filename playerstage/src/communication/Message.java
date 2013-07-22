package communication;

public class Message {

	// Global counter belong to Message class.
	private static Counter id;

	// The owner of the message.
	private MsgSender sender;

	// Used for release resources
	private boolean releaseFlag;

	Message() {
		if (id == null) {
			id = new Counter();
		}
		id.increment();
	}

	public int getId() {
		return id.value();
	}

	public boolean isReleaseFlag() {
		return releaseFlag;
	}

	public void setReleaseFlag(boolean releaseFlag) {
		this.releaseFlag = releaseFlag;
	}

	public MsgSender getSender() {
		return sender;
	}

	public void setSender(MsgSender sender) {
		this.sender = sender;
	}
}
