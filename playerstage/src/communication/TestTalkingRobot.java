package communication;

import java.util.ArrayList;

public class TestTalkingRobot {

	public static void main(String[] args) {
		TalkingRobot tRobot1 = new TalkingRobot("localhost", 6666);
		TalkingRobot tRobot2 = new TalkingRobot("localhost", 6667);
		TalkingRobot tRobot3 = new TalkingRobot("localhost", 6668);
		TalkingRobot tRobot4 = new TalkingRobot("localhost", 6669);
		ArrayList<TalkingRobot> robots = new ArrayList<TalkingRobot>();
		robots.add(tRobot1);
		robots.add(tRobot2);
		robots.add(tRobot3);
		robots.add(tRobot4);
		tRobot1.addRelatedVehicles(robots);
		tRobot2.addRelatedVehicles(robots);
		tRobot3.addRelatedVehicles(robots);
		tRobot4.addRelatedVehicles(robots);

		tRobot1.startSC();
		tRobot2.startSC();
		tRobot3.startSC();
		tRobot4.startSC();
	}
}