package pwcg.aar.outofmission.phase4.ElapsedTIme;

import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronMoveHandler
{
    private Campaign campaign = null;

    public SquadronMoveHandler (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public SquadronMoveEvent squadronMoves(Date newDate, Squadron squadron) throws PWCGException 
    {
        SquadronMoveEvent squadronMoveEvent = null;
        
        String airfieldNameNow = squadron.determineCurrentAirfieldName(campaign.getDate());
        String airfieldNameNext = squadron.determineCurrentAirfieldName(newDate);
        
        if (!airfieldNameNext.equalsIgnoreCase(airfieldNameNow))
        {
            String lastAirfield = squadron.determineCurrentAirfieldAnyMap(campaign.getDate()).getName();
            String newAirfield = squadron.determineCurrentAirfieldAnyMap(newDate).getName();
            boolean needsFerry = needsFerryMission(airfieldNameNow, airfieldNameNext);
            boolean isNewsworthy = true;
            squadronMoveEvent = new SquadronMoveEvent(lastAirfield, newAirfield, squadron.getSquadronId(), needsFerry, newDate, isNewsworthy);
        }
        
        return squadronMoveEvent;
    }

    private boolean needsFerryMission(String airfieldNameNow, String airfieldNameNext) throws PWCGException 
    {
        boolean needsFerry = false;
        
        boolean airfieldsOnSameMap = areAirfieldsOnTheSameMap(airfieldNameNow, airfieldNameNext);
        if (airfieldsOnSameMap)
        {
            needsFerry = true;
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
}
