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

import it.polito.appeal.traci.Edge;
import it.polito.appeal.traci.SumoTraciConnection;
import it.polito.appeal.traci.Vehicle;

import java.util.Collection;

/**
 * This code runs an instance of SUMO, queries the map bounds and does ten
 * simulation steps. For each step, it prints which and how many vehicles are
 * active.
 * <p>
 * The specified configuration file is relative to the TraCI4J package's base
 * directory.
 * 
 * @author Enrico Gueli &lt;enrico.gueli@polito.it&gt;
 * 
 */
public class GetVehicleInfo {

	/** main method */
	public static void main(String[] args) {
		SumoTraciConnection conn = new SumoTraciConnection(
				"traci_tls/data/cross.sumocfg", // config file
				12345 // random seed
		);
		try {
			conn.runServer();

			// the first two steps of this simulation have no vehicles.
			for (int i = 0; i < 1000; i++) {
				Collection<Vehicle> vehicles = conn.getVehicleRepository()
						.getAll().values();
				Vehicle aVehicle = null;
				if (vehicles.size() > 0) {
					// while (vehicles.iterator().hasNext()) {}

					aVehicle = vehicles.iterator().next();

					if (aVehicle.getID().equals("0")) {
						System.out.println("Vehicle " + aVehicle
								+ " will traverse these edges: "
								+ aVehicle.queryReadCurrentRoute().get());
						System.out.println("Vehicle " + aVehicle
								+ " current position: "
								+ aVehicle.queryReadPosition().get());
						aVehicle.queryReadSpeed().get();

					}

				}
				// System.out.println("======="+conn.getTrafficLightRepository().getByID("0").queryReadState().get());
				conn.nextSimStep();
			}

			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
