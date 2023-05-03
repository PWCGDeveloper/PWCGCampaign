package pwcg.campaign.shipping;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.TargetDistance;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionHumanParticipants;

public class ShipEncounterZoneManager
{
    public static ShipEncounterZone getShipEncounterZone (Campaign campaign, MissionHumanParticipants participatingPlayers) throws PWCGException
    {
        ShipEncounterZone shipEncounterZone = null;
        
        Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(participatingPlayers.getAllParticipatingPlayers().get(0).getSquadronId());
        Coordinate playerSquadronPosition = squadron.determineCurrentAirfieldAnyMap(campaign.getDate()).getPosition();
        ShippingLaneManager shippingLaneManager = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getShippingLaneManager();
        if (shippingLaneManager != null)
        {
            shipEncounterZone =  shippingLaneManager.getNearbyEncounterZone(campaign, playerSquadronPosition);        
            if (shipEncounterZone != null)
            {
                Coordinate encounterPoint = getInRangeEncounterPosition(campaign, shipEncounterZone, playerSquadronPosition);
                shipEncounterZone.setEncounterPoint(encounterPoint);;
            }
        }
        
        return shipEncounterZone;
    }
    
    
    private static Coordinate getInRangeEncounterPosition(Campaign campaign, ShipEncounterZone shipEncounterZone, Coordinate playerSquadronPosition) throws PWCGException
    {
        CoordinateBox coordinateBox = CoordinateBox.coordinateBoxFromCorners(shipEncounterZone.getSwCorner(), shipEncounterZone.getNeCorner());
        Coordinate encounterPoint = coordinateBox.chooseCoordinateWithinBox();

        double distanceFromPlayer = MathUtils.calcDist(encounterPoint, playerSquadronPosition);
        while (distanceFromPlayer > TargetDistance.findMaxTargetDistanceForConfiguration(campaign))
        {
            double angle = MathUtils.calcAngle(encounterPoint, playerSquadronPosition);
            Coordinate adjustedEncounterPoint = MathUtils.calcNextCoord(campaign.getCampaignMap(), encounterPoint.copy(), angle, 3000.0);
            
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
