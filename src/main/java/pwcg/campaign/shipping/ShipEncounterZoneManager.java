package pwcg.campaign.shipping;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.SkirmishDistance;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionHumanParticipants;

public class ShipEncounterZoneManager
{
    public static ShipEncounterZone getShipEncounterZone (Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        Company squadron =  PWCGContext.getInstance().getCompanyManager().getCompany(participatingPlayers.getAllParticipatingPlayers().get(0).getCompanyId());
        Coordinate playerSquadronPosition = squadron.determineCurrentAirfieldAnyMap(campaign.getDate()).getPosition();
        ShipEncounterZone shipEncounterZone = PWCGContext.getInstance().getCurrentMap().getShippingLaneManager().getNearbyEncounterZone(campaign.getDate(), playerSquadronPosition);
        
        if (shipEncounterZone != null)
        {
            Coordinate encounterPoint = getInRangeEncounterPosition(shipEncounterZone, playerSquadronPosition);
            shipEncounterZone.setEncounterPoint(encounterPoint);;
        }
        
        return shipEncounterZone;
    }
    
    
    private static Coordinate getInRangeEncounterPosition(ShipEncounterZone shipEncounterZone, Coordinate playerSquadronPosition) throws PWCGException
    {
        CoordinateBox coordinateBox = CoordinateBox.coordinateBoxFromCorners(shipEncounterZone.getSwCorner(), shipEncounterZone.getNeCorner());
        Coordinate encounterPoint = coordinateBox.chooseCoordinateWithinBox();

        double distanceFromPlayer = MathUtils.calcDist(encounterPoint, playerSquadronPosition);
        while (distanceFromPlayer > SkirmishDistance.findMaxSkirmishDistance())
        {
            double angle = MathUtils.calcAngle(encounterPoint, playerSquadronPosition);
            Coordinate adjustedEncounterPoint = MathUtils.calcNextCoord(encounterPoint.copy(), angle, 3000.0);
            
            if (!coordinateBox.isInBox(adjustedEncounterPoint))
            {
                return encounterPoint;
            }
            else
            {
                encounterPoint = adjustedEncounterPoint;
            }
            
            distanceFromPlayer = MathUtils.calcDist(encounterPoint, playerSquadronPosition);
        }
        return encounterPoint;
    }

}
