package pwcg.mission;

import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.LocationAwayFromFrontFinder;
import pwcg.core.location.PWCGLocation;

public class MissionCenterBuilderStrategicIntercept implements IMissionCenterBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;
    
    public MissionCenterBuilderStrategicIntercept(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }

    @Override
    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {   
        List<PWCGLocation> townLocations = getTowns(missionBoxRadius);
        List<PWCGLocation> townLocationsAwayFromFont = getTownsAwayFromFront(townLocations);
        return pickTargetTown(townLocationsAwayFromFont);        
    }

    private List<PWCGLocation> getTowns(int missionBoxRadius) throws PWCGException
    {
        Company squadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();        
        Coordinate squadronLocation = squadron.determineCurrentPosition(campaign.getDate());
        return PWCGContext.getInstance().getCurrentMap().getGroupManager().findTownsForSideWithinRadius(squadron.determineSide(), campaign.getDate(), squadronLocation, missionBoxRadius * 2);
    }
    
    private List<PWCGLocation> getTownsAwayFromFront(List<PWCGLocation> townLocations) throws PWCGException
    {
        Company squadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();        

        List<PWCGLocation> townLocationsAwayFromFont = LocationAwayFromFrontFinder.getLocationsAwayFromFront(
                townLocations, squadron.determineSide(), campaign.getDate());
        return townLocationsAwayFromFont;
    }

    private Coordinate pickTargetTown(List<PWCGLocation> townLocationsAwayFromFont) throws PWCGException
    {
        if (!townLocationsAwayFromFont.isEmpty())
        {
            Collections.shuffle(townLocationsAwayFromFont);
            return townLocationsAwayFromFont.get(0).getPosition();
        }
        else
        {
            Company squadron = participatingPlayers.getAllParticipatingPlayers().get(0).determineSquadron();        
            Coordinate squadronLocation = squadron.determineCurrentPosition(campaign.getDate());
            return squadronLocation;
        }
    }
}
