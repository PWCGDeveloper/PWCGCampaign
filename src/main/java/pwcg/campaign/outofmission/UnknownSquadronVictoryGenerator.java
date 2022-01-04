package pwcg.campaign.outofmission;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryEntity;
import pwcg.campaign.tank.OutOfMissionTankFinder;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

public class UnknownSquadronVictoryGenerator
{
    private CrewMember victorCrewMember;

    public UnknownSquadronVictoryGenerator (CrewMember victorCrewMember)
    {
        this.victorCrewMember = victorCrewMember;
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
            PWCGLogger.logException(e);
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
	        Company victorCrewMemberSquadron = PWCGContext.getInstance().getCompanyManager().getCompany(victorCrewMember.getCompanyId());
            createVictoryHeader(date, victory, victorCrewMemberSquadron.determineSquadronCountry(date).getSide().getOppositeSide());
	
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
        
        Company squadron = victorCrewMember.determineSquadron();
        
        OutOfMissionTankFinder outOfMissionPlaneFinder = new OutOfMissionTankFinder();
        TankType planeType = outOfMissionPlaneFinder.findTankType(
                squadron,
                squadron.determineSquadronPrimaryRoleCategory(date),
                date);

        victor.setAirOrGround(Victory.AIRCRAFT);
        victor.setType(planeType.getType());
        victor.setName(planeType.getDisplayName());
        victor.setSquadronName(squadron.determineDisplayName(date));
        victor.setCrewMemberName(victorCrewMember.getRank() + " " + victorCrewMember.getName());
        victor.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
        
        return victor;
    }

    private VictoryEntity createVictim(Date date) throws PWCGException
    {
        VictoryEntity victim = new VictoryEntity();            
        victim.setAirOrGround(Victory.AIRCRAFT);
        victim.setType("");
        victim.setName("");
        victim.setSquadronName("");
        victim.setCrewMemberStatus(CrewMemberStatus.STATUS_KIA);
        return victim;
    }

    private String getEventLocation(Side enemySide, Date date) throws PWCGException
    {
        String eventLocationDescription = "";
        
        Coordinate squadronPosition = victorCrewMember.determineSquadron().determineCurrentPosition(date);
        if (squadronPosition != null)
        {
            FrontLinesForMap frontLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(date);
            FrontLinePoint eventPosition = frontLines.findCloseFrontPositionForSide(squadronPosition, 100000, enemySide);
    
            eventLocationDescription =  PWCGContext.getInstance().getCurrentMap().getGroupManager().getTownFinder().findClosestTown(eventPosition.getPosition()).getName();
            if (eventLocationDescription == null || eventLocationDescription.isEmpty())
            {
                eventLocationDescription = "";
            }
        }
    
        return eventLocationDescription;
    }
}
