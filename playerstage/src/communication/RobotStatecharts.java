package communication;

import java.io.IOException;

import org.yakindu.sct.runtime.java.gostop.GoStopCycleBasedStatemachine;
import org.yakindu.sct.runtime.java.gostop.GoStopCycleBasedStatemachine.State;

import javaclient3.PlayerClient;
import javaclient3.PlayerException;
import javaclient3.Position2DInterface;
import javaclient3.RangerInterface;
import javaclient3.structures.PlayerConstants;
import communication.*;

public class RobotStatecharts {

	private PlayerClient robot;
	private Position2DInterface posi;
	private RangerInterface rngi;
	private double speed;
	private double yawSpeed;
	private double THRESHOLD = 0.6;
	GoStopCycleBasedStatemachine sc;
	// Central token counts the number of request for the resources.
	private Counter numberOfRequest;

	private RobotStatecharts(String host, int port, Counter c) {
		try {
			// Connect to the Player server and request access to Position and
			// Sonar
			sc = new GoStopCycleBasedStatemachine();
			robot = new PlayerClient(host, port);
			posi = robot.requestInterfacePosition2D(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			rngi = robot.requestInterfaceRanger(0,
					PlayerConstants.PLAYER_OPEN_MODE);
			robot.runThreaded(-1, -1);
			numberOfRequest = c;
		} catch (PlayerException e) {
			System.err
					.println("SpaceWandererExample: > Error connecting to Player: ");
			System.err.println("    [ " + e.toString() + " ]");
			System.exit(1);
		}
	}

	public void run() {
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
		// sc.getInterfaceCar().setVarNumberOfRequest(numberOfRequest.value());
	}

	private void getValueFromStatechart() {
		// TODO Auto-generated method stub
		speed = sc.getInterfaceCar().getVarForwardSpeed();
		yawSpeed = sc.getInterfaceCar().getVarTurnSpeed();
		posi.setSpeed(speed, yawSpeed);
		if (sc.getInterfaceCar().isRaisedOwnRequest()) {
			numberOfRequest.increment();
		}
		if (sc.getInterfaceCar().isRaisedCrossSucc()) {
			numberOfRequest.decrement();
		}
	}

	private void printSensorData() {
		while (!rngi.isDataReady())
			;
		double[] IRValues = rngi.getData().getRanges();
		System.out.println("sensor data:");
		for (int i = 0; i < rngi.getData().getRanges_count(); i++) {
			System.out.println(IRValues[i]);
		}
	}

	private void printSC() {

		if (sc.isStateActive(State.GoForward)) {
			System.out.println("Current State: " + State.GoForward);
		} else if (sc.isStateActive(State.Stop)) {
			System.out.println("Current State: " + State.Stop);
		} else if (sc.isStateActive(State.TurnLeft)) {
			System.out.println("Current State: " + State.TurnLeft);
		} else if (sc.isStateActive(State.TurnRight)) {
			System.out.println("Current State: " + State.TurnRight);
		} else {
			System.out.println("Sth wired");
		}

		if (sc.isStateActive(State.ReadSensorData)) {
			System.out.println("Current State: " + State.ReadSensorData);
		} else if (sc.isStateActive(State.HeadingMiddle)) {
			System.out.println("Current State: " + State.HeadingMiddle);
		} else if (sc.isStateActive(State.HeadingLeft)) {
			System.out.println("Current State: " + State.HeadingLeft);
		} else if (sc.isStateActive(State.HeadingRight)) {
			System.out.println("Current State: " + State.HeadingRight);
		} else if (sc.isStateActive(State.ReadFailed)) {
			System.out.println("Current State: " + State.ReadFailed);
		} else if (sc.isStateActive(State.DangerDistance)) {
			System.out.println("Current State: " + State.DangerDistance);
		} else {
			System.out.println("Sth wired");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Start Player/Stage simulation environment
		startSimulation();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Initialize the intersection token for vehicle communication
		Counter c = new Counter();

		RobotStatecharts robot1 = new RobotStatecharts("localhost", 6666, c);
		robot1.run();
		RobotStatecharts robot2 = new RobotStatecharts("localhost", 6667, c);
		robot2.run();
		RobotStatecharts robot3 = new RobotStatecharts("localhost", 6668, c);
		robot3.run();
		RobotStatecharts robot4 = new RobotStatecharts("localhost", 6669, c);
		robot4.run();
		// RobotStatecharts robot11 = new RobotStatecharts("localhost", 6676);
		// robot11.run();
		// RobotStatecharts robot12 = new RobotStatecharts("localhost", 6677);
		// robot12.run();
		// RobotStatecharts robot13 = new RobotStatecharts("localhost", 6678);
		// robot13.run();
		// RobotStatecharts robot14 = new RobotStatecharts("localhost", 6679);
		// robot14.run();
	}

	public static void startSimulation() {
		try {
			final Process p = Runtime.getRuntime().exec(
					"/usr/local/bin/player config/simple.cfg");
			// Runtime.getRuntime().addShutdownHook(new Thread() {
			// public void run() {
			// p.destroy();
			// }
			// });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
