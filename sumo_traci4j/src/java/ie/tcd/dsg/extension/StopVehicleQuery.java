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

public class StopVehicleQuery extends ChangeStateQuery {

	private String _vehicleID;
	private String _edgeID;
	private double _positioin;
	private short _laneIdx;
	private int _duration = -1;

	public StopVehicleQuery(DataInputStream dis, DataOutputStream dos) {
		super(dis, dos, Constants.CMD_SET_VEHICLE_VARIABLE);
		// TODO Auto-generated constructor stub
	}

	public void setData(String vID, String edgeID, double positioin, short laneIdx,
			int duration) {
		_vehicleID = vID;
		_edgeID = edgeID;
		_positioin = positioin;
		_laneIdx = laneIdx;
		_duration = duration;
	}

	@Override
	protected void writeRequestTo(Storage content) {
		// TODO Auto-generated method stub
		content.writeUnsignedByte(Constants.CMD_STOP);
		content.writeStringASCII(_vehicleID);

		content.writeUnsignedByte(Constants.TYPE_COMPOUND);
		content.writeInt(4);
		content.writeUnsignedByte(Constants.TYPE_STRING);
		content.writeStringASCII(_edgeID);
		content.writeUnsignedByte(Constants.TYPE_DOUBLE);
		content.writeDouble(_positioin);
		content.writeUnsignedByte(Constants.TYPE_BYTE);
		content.writeByte(_laneIdx);
		content.writeUnsignedByte(Constants.TYPE_INTEGER);
		if (_duration == -1) {
			content.writeInt(Integer.MAX_VALUE); // ms
		} else {
			content.writeInt(_duration);
		}
		
		// content.writeUnsignedByte(Constants.TYPE_BYTE); // optional
		// content.writeByte(3); // for both parking and triggered
	}

}
