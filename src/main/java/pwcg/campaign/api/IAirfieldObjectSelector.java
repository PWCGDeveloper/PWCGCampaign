package pwcg.campaign.api;

import pwcg.campaign.group.airfield.HotSpot;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.vehicle.IVehicle;

public interface IAirfieldObjectSelector
{

    /**
     * @param hotSpot
     * @throws PWCGException 
     * @
     */
    IVehicle createWaterTower(HotSpot hotSpot, IAirfield airfield) throws PWCGException;

    /**
     * @param hotSpot
     * @throws PWCGException 
     * @
     */
    IVehicle createAirfieldObject(HotSpot hotSpot, IAirfield airfield) throws PWCGException;

}