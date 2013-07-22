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

package ie.tcd.dsg.extension;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import de.uniluebeck.itm.tcpip.Storage;
import it.polito.appeal.traci.ChangeStateQuery;
import it.polito.appeal.traci.protocol.Constants;

public class ResumeFromStopQuery extends ChangeStateQuery {

	private String _vehicleID;

	public ResumeFromStopQuery(DataInputStream dis, DataOutputStream dos) {
		super(dis, dos, Constants.CMD_SET_VEHICLE_VARIABLE);
		// TODO Auto-generated constructor stub
	}

	public void setData(String vID) {
		_vehicleID = vID;
	}

	@Override
	protected void writeRequestTo(Storage content) {
		// TODO Auto-generated method stub
		content.writeUnsignedByte(Constants.CMD_RESUME);
		content.writeStringASCII(_vehicleID);

		content.writeUnsignedByte(Constants.TYPE_COMPOUND);
		content.writeInt(0);
	}

}
