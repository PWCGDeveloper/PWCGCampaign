package pwcg.aar.outofmission.phase4.ElapsedTIme;

import java.util.Date;
import java.util.List;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;

public class SquadronMoveHandler
{
    private Campaign campaign = null;

    public SquadronMoveHandler (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public SquadronMoveEvent squadronMoves(Date newDate, Company squadron) throws PWCGException 
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
            squadronMoveEvent = new SquadronMoveEvent(lastAirfield, newAirfield, squadron.getCompanyId(), needsFerry, newDate, isNewsworthy);
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
        Airfield airfieldNow = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldNameNow);
        Airfield airfieldNext = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfield(airfieldNameNext);
        if (airfieldNow != null && airfieldNext != null)
        {
            distance = MathUtils.calcDist(airfieldNow.getPosition(), airfieldNext.getPosition());
        }
        return distance;
    }
}
