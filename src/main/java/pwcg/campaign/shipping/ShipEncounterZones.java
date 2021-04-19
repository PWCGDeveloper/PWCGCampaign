package pwcg.campaign.shipping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import pwcg.campaign.skirmish.SkirmishDistance;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;

public class ShipEncounterZones
{
    private List<ShipEncounterZone> shipEncounterZones = new ArrayList<>();

    public List<ShipEncounterZone> getShipEncounterZones()
    {
        return shipEncounterZones;
    }

    public ShipEncounterZone getNearbyShipEncounterZone(Date date, Coordinate targetGeneralLocation) throws PWCGException
    {
        List<ShipEncounterZone> nearbyRouteDefinitions = new ArrayList<>();
        for (ShipEncounterZone shipEncounterZone : shipEncounterZones)
        {
            if (!DateUtils.isDateInRange(date, shipEncounterZone.getStartDate(), shipEncounterZone.getEndDate()))
            {
                continue;
            }
            
            CoordinateBox coordinateBox = CoordinateBox.coordinateBoxFromCorners(shipEncounterZone.getSwCorner(), shipEncounterZone.getNeCorner());
            double distance = MathUtils.calcDist(coordinateBox.getCenter(), targetGeneralLocation);
            if (distance < SkirmishDistance.findMaxSkirmishDistance())
            {
                nearbyRouteDefinitions.add(shipEncounterZone);
            }
        }
        
        if (!nearbyRouteDefinitions.isEmpty())
        {
            Collections.shuffle(nearbyRouteDefinitions);
            return nearbyRouteDefinitions.get(0);
        }
        return null;
    }

    public ShipEncounterZone getShipEncounterZoneByName(String skirmishName) throws PWCGException
    {
        for (ShipEncounterZone shipEncounterZone : shipEncounterZones)
        {
            if (shipEncounterZone.getName().equals(skirmishName))
            {
                return shipEncounterZone;
            }
        }
        
        throw new PWCGException("No ship encounter zone for name " + skirmishName);
    }


    public ShipEncounterZone getShipEncounterByName(String skirmishName) throws PWCGException
    {
        for (ShipEncounterZone shipEncounterZone : shipEncounterZones)
        {
            if (shipEncounterZone.getName().equals(skirmishName))
            {
                return shipEncounterZone;
            }
        }
        
        throw new PWCGException("No Ship Encounter Zone for name " + skirmishName);
    }

}
