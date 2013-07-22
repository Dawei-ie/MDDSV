package communication;

import java.util.ArrayList;

import org.yakindu.sct.runtime.java.gostop.GoStopCycleBasedStatemachine;

import javaclient3.PlayerClient;
import javaclient3.PlayerException;
import javaclient3.Position2DInterface;
import javaclient3.RangerInterface;
import javaclient3.structures.PlayerConstants;

public class TalkingRobot extends PlayerClient {

	/**
	 * To get data from simulation:
	 *		// Connect to the Player server and request access to Position and Sonar
	 *		robot = new PlayerClient(host, port);
	 *		posi = robot.requestInterfacePosition2D(0, PlayerConstants.PLAYER_OPEN_MODE);
	 *		rngi = robot.requestInterfaceRanger(0, PlayerConstants.PLAYER_OPEN_MODE);
	 *		robot.runThreaded(-1, -1);
	 *
	 * 		//Sensor data from left, middle and right range sensors, return distance value
	 * 		while (!rngi.isDataReady()); // check if simulator is ready to provide data
	 * 		double[] rangerValues = rngi.getData().getRanges();
	 * 		double left = rangerValues[0];
	 * 		double middle = rangerValues[1];
	 * 		double right = rangerValues[2];
	 * 
	 * 		// GPS data, position of robots
	 * 		while (!posi.isDataReady());
	 * 		double x = posi.getX();
	 * 		double y = posi.getY();
	 * 		// control a robot's speed, don't care about speed in this example
	 * 		posi.setSpeed(speed, yawSpeed);
	 * 
	 * 		// if received a message, return true
	 * 		msgReceiver.isDataAvailable();
	 * 		// get the first message's sender id
	 * 		msgReceiver.peekReceivedMsg().getSender();
	 * 		// if own sent message is received by others, return true
	 * 		msgSender.isAcknowledged();
	 */
	
	// interfaces to interact with simulator
	// robot interface
	private PlayerClient robot;
	// position data interface
	private Position2DInterface posi;
	// sensor data interface
	private RangerInterface rngi;
	// communication interface
	private MsgSender msgSender;
	private MsgReceiver msgReceiver;

	private double speed;
	private double yawSpeed;
	private double THRESHOLD = 0.6;
	private double INTERRADIUS = 1.5;
	GoStopCycleBasedStatemachine sc;

	private String name;

	public MsgSender getMsgSender() {
		return msgSender;
	}

	public void setMsgSender(MsgSender msgSender) {
		this.msgSender = msgSender;
	}

	public MsgReceiver getMsgReceiver() {
		return msgReceiver;
	}

	public void setMsgReceiver(MsgReceiver msgReceiver) {
		this.msgReceiver = msgReceiver;
	}

	public TalkingRobot(String serverName, int portNumber) {
		super(serverName, portNumber);
		// TODO Auto-generated constructor stub
		name = serverName + ":" + String.valueOf(portNumber);
		msgSender = new MsgSender(name);
		msgReceiver = new MsgReceiver(name);
		RobotStatecharts(serverName, portNumber);
	}

	public void startSC() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				sc.enter();
				// sc.setTimerService(new TimerService());
				while (!isInterrupted()) {
					// printSensorData();
					setValueForStatechart();
					sc.runCycle();
					getValueFromStatechart();
					// if (numberOfRequest.value() > 0) {
					// System.out.println(numberOfRequest.value());
					// System.out.println(sc.getInterfaceCar()
					// .getVarDistanceToIntersection());
					// }
					// printSC();
				}
			}
		};
		thread.start();
	}

	public void sendMsg() {
		msgSender.broadcastMsg();
	}

	public void releaseResources() {
		msgSender.releaseResourceMsg();
	}

	public void addRelatedVehicles(ArrayList<TalkingRobot> robots) {
		addReceivers(robots);
		addSenders(robots);
	}

	private void addSenders(ArrayList<TalkingRobot> robots) {
		// TODO Auto-generated method stub
		for (TalkingRobot robot : robots) {
			robot.getMsgSender().addObserver(msgReceiver);
		}
	}

	private void addReceivers(ArrayList<TalkingRobot> robots) {
		// TODO Auto-generated method stub
		for (TalkingRobot robot : robots) {
			msgSender.addObserver(robot.getMsgReceiver());
		}
	}

	private void setValueForStatechart() {
		while (!rngi.isDataReady())
			;
		double[] rangerValues = rngi.getData().getRanges();
		double left = rangerValues[0];
		double middle = rangerValues[1];
		double right = rangerValues[2];
		while (!posi.isDataReady())
			;
		double x = posi.getX();
		double y = posi.getY();
		double d = Math.sqrt(x * x + y * y);
		sc.getInterfaceCar().setVarDistanceToIntersection(d);
		sc.getInterfaceCar().setVarSensorData1(left);
		sc.getInterfaceCar().setVarSensorData2(middle);
		sc.getInterfaceCar().setVarSensorData3(right);
		sc.getInterfaceCar().setVarTHRESHOLD(THRESHOLD);
		sc.getInterfaceCar().setVarINTERRADIUS(INTERRADIUS);
		sc.getInterfaceCar().setVarNoOtherReq(!msgReceiver.isDataAvailable());
		if (msgReceiver.isDataAvailable()) sc.getInterfaceCar().setVarIsOwnRequest(this.msgSender.equals(msgReceiver.peekReceivedMsg().getSender()));
		sc.getInterfaceCar().setVarIsAck(msgSender.isAcknowledged());
	}

	private void getValueFromStatechart() {
		// TODO Auto-generated method stub
		speed = sc.getInterfaceCar().getVarForwardSpeed();
		yawSpeed = sc.getInterfaceCar().getVarTurnSpeed();
		posi.setSpeed(speed, yawSpeed);
		if (sc.getInterfaceCar().isRaisedOwnRequest()) {
			this.sendMsg();
		}
		if (sc.getInterfaceCar().isRaisedCrossSucc()) {
			this.releaseResources();
		}
	}

	private void RobotStatecharts(String host, int port) {
		try {
			// State chart logic
			sc = new GoStopCycleBasedStatemachine();
			// Connect to the Player server and request access to Position and
			// Sonar
			robot = new PlayerClient(host, port);
			posi = robot.requestInterfacePosition2D(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			rngi = robot.requestInterfaceRanger(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			robot.runThreaded(-1, -1);
		} catch (PlayerException e) {
			System.err.println(" Error connecting to Player: ");
			System.err.println(" [ " + e.toString() + " ]");
			System.exit(1);
		}
	}
}
