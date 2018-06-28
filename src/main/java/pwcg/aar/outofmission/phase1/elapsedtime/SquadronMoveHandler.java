package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class SquadronMoveHandler
{
    private Campaign campaign = null;

    public SquadronMoveHandler (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public SquadronMoveEvent squadronMoves(Date newDate) throws PWCGException 
    {
        SquadronMoveEvent squadronMoveEvent = null;
        
        Squadron squad = campaign.determineSquadron();
        String airfieldNameNow = squad.determineCurrentAirfieldName(campaign.getDate());
        String airfieldNameNext = squad.determineCurrentAirfieldName(newDate);
        
        if (!airfieldNameNext.equalsIgnoreCase(airfieldNameNow))
        {
            
            squadronMoveEvent = new SquadronMoveEvent();
            squadronMoveEvent.setLastAirfield(squad.determineCurrentAirfieldAnyMap(campaign.getDate()));
            squadronMoveEvent.setNewAirfield(squad.determineCurrentAirfieldAnyMap(newDate));
            squadronMoveEvent.setDate(campaign.getDate());
            squadronMoveEvent.setSquadron(squad);
            
            boolean needsFerry = needsFerryMission(airfieldNameNow, airfieldNameNext);
            squadronMoveEvent.setNeedsFerryMission(needsFerry);
        }
        
        return squadronMoveEvent;
    }

    private boolean needsFerryMission(String airfieldNameNow, String airfieldNameNext) throws PWCGException 
    {
        boolean needsFerry = false;
        
        boolean airfieldsOnSameMap = areAirfieldsOnTheSameMap(airfieldNameNow, airfieldNameNext);
        if (airfieldsOnSameMap)
        {
            double distance = calculateDistanceBetweenAirfields(airfieldNameNow, airfieldNameNext);
            if (distance < 50000.0)
            {
                needsFerry = true;
            }
        }
        
        return needsFerry;
    }

    private boolean areAirfieldsOnTheSameMap(String airfieldNameNow, String airfieldNameNext) throws PWCGException
    {
        List<FrontMapIdentifier> airfieldNowMaps = AirfieldManager.getMapIdForAirfield(airfieldNameNow);
        List<FrontMapIdentifier> airfieldNextMaps = AirfieldManager.getMapIdForAirfield(airfieldNameNext);
       
        boolean airfieldsOnSameMap = false;
        for (FrontMapIdentifier airfieldNowMap : airfieldNowMaps)
        {
            for (FrontMapIdentifier airfieldNextMap : airfieldNextMaps)
            {
                if (airfieldNowMap == airfieldNextMap)
                {
                    airfieldsOnSameMap = true;
                }
            }
        }
        return airfieldsOnSameMap;
    }

    private double calculateDistanceBetweenAirfields(String airfieldNameNow, String airfieldNameNext)
    {
        double distance = 1000000.0;
        IAirfield airfieldNow = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldNameNow);
        IAirfield airfieldNext = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldNameNext);
        if (airfieldNow != null && airfieldNext != null)
        {
            distance = MathUtils.calcDist(airfieldNow.getPosition(), airfieldNext.getPosition());
        }
        return distance;
    }
}
