package pwcg.campaign.api;

import pwcg.campaign.group.airfield.hotspot.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;

public interface IAirfieldObjectSelector
{
    IVehicle createAirfieldObject(HotSpot hotSpot, IAirfield airfield) throws PWCGException;

}