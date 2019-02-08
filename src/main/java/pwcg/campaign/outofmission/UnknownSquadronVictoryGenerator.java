package pwcg.campaign.outofmission;

import java.util.Date;

import pwcg.aar.outofmission.phase1.elapsedtime.OutOfMissionPlaneFinder;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryEntity;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;

public class UnknownSquadronVictoryGenerator
{
    private SquadronMember victorPilot;

    public UnknownSquadronVictoryGenerator (SquadronMember victorPilot)
    {
        this.victorPilot = victorPilot;
    }
    
    public Victory generateOutOfMissionVictory(Date date) throws PWCGException
    {        
        Victory victory = null;
        try
        {
            victory = createVictory(date);
        }
        catch (PWCGException e)
        {
            Logger.logException(e);
            victory = null;
        }
        
        return victory;
    }

    private Victory createVictory(Date date) throws PWCGException
    {
        VictoryEntity victim = createVictim(date);
        VictoryEntity victor = createVictor(date);
        
        Victory victory = null;
        if (victim != null && victor != null)
        {
	        victory = new Victory();
	        Squadron victorPilotSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(victorPilot.getSquadronId());
            createVictoryHeader(date, victory, victorPilotSquadron.determineSquadronCountry(date).getSide().getOppositeSide());
	
	        victory.setVictim(victim);
	        victory.setVictor(victor);
	        victory.setDate(date);
        }
        
        return victory;
    }

    private void createVictoryHeader(Date date, Victory victory, Side enemySide) throws PWCGException
    {
        victory.setDate(date);
        victory.setCrashedInSight(false);
   
        String location = getEventLocation(enemySide, date);
        victory.setLocation(location);
    }

    private VictoryEntity createVictor(Date date) throws PWCGException
    {
        VictoryEntity victor = new VictoryEntity();
        
        Squadron squadron = victorPilot.determineSquadron();
        
        OutOfMissionPlaneFinder outOfMissionPlaneFinder = new OutOfMissionPlaneFinder();
        PlaneType planeType = outOfMissionPlaneFinder.findPlaneType(
                squadron,
                squadron.determineSquadronPrimaryRole(date),
                date);

        String victorType = planeType.getType();

        victor.setAirOrGround(Victory.AIR_VICTORY);
        victor.setType(victorType);
        victor.setSquadronName(squadron.determineDisplayName(date));
        victor.setPilotName(victorPilot.getRank() + " " + victorPilot.getName());
        victor.setPilotStatus(SquadronMemberStatus.STATUS_ACTIVE);
        
        return victor;
    }

    private VictoryEntity createVictim(Date date) throws PWCGException
    {
        VictoryEntity victim = new VictoryEntity();            
        victim.setAirOrGround(Victory.AIR_VICTORY);
        victim.setType("");
        victim.setSquadronName("");
        victim.setPilotStatus(SquadronMemberStatus.STATUS_KIA);
        return victim;
    }

    private String getEventLocation(Side enemySide, Date date) throws PWCGException
    {
        String eventLocationDescription = "";
        
        Coordinate squadronPosition = victorPilot.determineSquadron().determineCurrentPosition(date);
        if (squadronPosition != null)
        {
            FrontLinesForMap frontLines = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(date);
            FrontLinePoint eventPosition = frontLines.findCloseFrontPositionForSide(squadronPosition, 100000, enemySide);
    
            eventLocationDescription =  PWCGContextManager.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(eventPosition.getPosition()).getName();
            if (eventLocationDescription == null || eventLocationDescription.isEmpty())
            {
                eventLocationDescription = "";
            }
        }
    
        return eventLocationDescription;
    }
}
