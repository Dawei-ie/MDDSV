/*   
    Copyright (C) 2013 ApPeAL Group, Politecnico di Torino

    This file is part of TraCI4J.

    TraCI4J is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    TraCI4J is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with TraCI4J.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.polito.appeal.traci.examples;

import static org.junit.Assert.assertTrue;
import ie.tcd.dsg.extension.StopVehicleQuery;
import it.polito.appeal.traci.AddVehicleQuery;
import it.polito.appeal.traci.ChangeRouteQuery;
import it.polito.appeal.traci.ChangeTargetQuery;
import it.polito.appeal.traci.Edge;
import it.polito.appeal.traci.Repository;
import it.polito.appeal.traci.Route;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.TLState;
import it.polito.appeal.traci.ValueReadQuery;
import it.polito.appeal.traci.Vehicle;
import it.polito.appeal.traci.VehicleType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;

import de.uniluebeck.itm.tcpip.Storage;

/**
 * This code picks a vehicle from the active ones and queries its current route.
 * 
 * @author Enrico Gueli &lt;enrico.gueli@polito.it&gt;
 * 
 */
public class OpenStepsClose {

	private static SumoTraciConnection conn = null;
	private static Vehicle firstVehicle = null;

	/**
	 * Returns the first vehicle entered in the simulation. Since all vehicles
	 * depart from the same road, and SUMO lets at most one vehicle to depart
	 * from a given road at each step, the vehicle returned from this function
	 * will always be the same.
	 * 
	 * @throws IOException
	 */
	public static void getFirstVehicle() throws IOException {
		Repository<Vehicle> repo = conn.getVehicleRepository();
		if (repo.getAll().isEmpty()) {
			firstVehicle = null;
		} else {
			firstVehicle = repo.getAll().values().iterator().next();

		}
	}

	public static void testAddVehicle(String vname) throws IOException {

		// assertTrue(conn.getVehicleRepository().getIDs().size() > 0);
		final String id = vname;
		Collection<Route> routes = conn.getRouteRepository().getAll().values();
		Random rn = new Random();
		int randomIndex = rn.nextInt(12);
		Route route = (Route) routes.toArray()[randomIndex];
		VehicleType vType = conn.getVehicleTypeRepository().getByID("SUMO_DEFAULT_TYPE");

		AddVehicleQuery avq = conn.queryAddVehicle();
		avq.setVehicleData(id, vType, route, 0, 0, 0);
		avq.run();
	}

	public static void addVehicle(String vname, String route) throws IOException {

		Route r = conn.getRouteRepository().getByID(route);
		VehicleType vType = conn.getVehicleTypeRepository().getByID("SUMO_DEFAULT_TYPE");

		AddVehicleQuery avq = conn.queryAddVehicle();
		avq.setVehicleData(vname, vType, r, 0, 0, 0);
		avq.run();
	}
	
	/** main method */
	public static void main(String[] args) {
		BasicConfigurator.configure();

		conn = new SumoTraciConnection("cross1l/cross1l.sumocfg", -1);
		try {
			conn.runServer();
			// ChangeRouteQuery ctq = null;
//			addVehicle("v1", "WE");
//			addVehicle("v2", "EW");
//			addVehicle("v3", "NS");
//			addVehicle("v4", "SN");
//			addVehicle("v5", "NS");
//			addVehicle("v6", "SN");
			for (int i = 0; i < 200; i++) {
				conn.nextSimStep();
				if (i%2 == 0) {
					testAddVehicle("v" + String.valueOf(i));
				}
				if (firstVehicle == null) {
					getFirstVehicle();
				} else {
					Repository<Vehicle> repo = conn.getVehicleRepository();
					firstVehicle.addReceivers(repo.getAll().values());
					firstVehicle.addSenders(repo.getAll().values());
//					System.out.println(" === route === "
//							+ firstVehicle.queryReadCurrentRoute().get());
//					System.out.println(" === edge === "
//							+ firstVehicle.queryReadCurrentEdge().get());
//					System.out.println(" === lane === "
//							+ firstVehicle.queryReadCurrentLane().get());
//					System.out.println(" === position === "
//							+ firstVehicle.queryReadPosition().get());
				}
				// // double x = firstVehicle.queryReadPosition().get().getX();
				// if (i == -1) {
				// StopVehicleQuery svq = conn.queryStopVehicle();
				// svq.setData("0", "2i1", 400, (short) 0, -1);
				// svq.run();
				// }
				// firstVehicle.queryChangeTarget().setValue(conn.getEdgeRepository().getByID("1i"));
				// firstVehicle.queryChangeTarget().run();
				// firstVehicle.queryReroute();
				// System.out.println(" === new route === " +
				// firstVehicle.queryReadCurrentRoute().get());
				// if (ctq == null) {
				// List<Edge> newRoute = new ArrayList<Edge>();
				// newRoute.add(conn.getEdgeRepository().getByID("52o"));
				// newRoute.add(conn.getEdgeRepository().getByID("2i"));
				// newRoute.add(conn.getEdgeRepository().getByID("4o"));
				// newRoute.add(conn.getEdgeRepository().getByID("54i"));
				// ctq = firstVehicle.queryChangeRoute();
				// ctq.setValue(newRoute);
				// ctq.run();
				// System.out.println("======= "
				// + firstVehicle.queryReadCurrentRoute().get());
				// }
				// }

				// int time = conn.getCurrentSimStep();
				// Collection<Vehicle> vehicles = conn.getVehicleRepository()
				// .getAll().values();

				// System.out.println("At time step " + time + ", there are "
				// + vehicles.size() + " vehicles: " + vehicles);
				// conn.getTrafficLightRepository().getByID("0")
				// .queryChangeLightsState().setValue(tlstate);
				// conn.getTrafficLightRepository().getByID("0")
				// .queryChangeLightsState().run();
				// System.out.println("======="+conn.getTrafficLightRepository().getByID("0").queryReadState().get());
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
