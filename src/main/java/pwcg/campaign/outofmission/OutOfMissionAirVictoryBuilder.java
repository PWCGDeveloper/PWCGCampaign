package pwcg.campaign.outofmission;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.crewmember.VictoryEntity;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

public class OutOfMissionAirVictoryBuilder
{
    private Campaign campaign;
    private Company victimSquadron;
    private IVictimGenerator victimGenerator;
    private CrewMember victorCrewMember;
    private CrewMember victimCrewMember;
    private EquippedTank victimPlane;

    public OutOfMissionAirVictoryBuilder (Campaign campaign, Company victimSquadron, IVictimGenerator victimGenerator, CrewMember victorCrewMember)
    {
        this.campaign = campaign;
        this.victimSquadron = victimSquadron;
        this.victimGenerator = victimGenerator;
        this.victorCrewMember = victorCrewMember;
    }
    
    public Victory generateOutOfMissionVictory(Date date) throws PWCGException
    {        
        Victory victory = null;
        try
        {
            if (victimSquadron != null)
            {
                victory = createVictory(date);
            }
        }
        catch (PWCGException e)
        {
            PWCGLogger.logException(e);
            victory = null;
        }
        
        return victory;
    }

    private Victory createVictory(Date date)
                    throws PWCGException
    {
        VictoryEntity victim = createVictim(date);
        VictoryEntity victor = createVictor(date);
        
        Victory victory = null;
        if (victim != null && victor != null)
        {
	        victory = new Victory();
	        createVictoryHeader(date, victory, victimSquadron.determineSquadronCountry(date).getSide());
	
	        victory.setVictim(victim);
	        victory.setVictor(victor);
	        victory.setDate(date);
        }
        
        return victory;
    }

    private void createVictoryHeader(Date date, Victory victory, Side enemySide) throws PWCGException
    {
        victory.setDate(date);
        victory.setCrashedInSight(true);
   
        String location = getEventLocation(enemySide, date);
        victory.setLocation(location);
    }

    private VictoryEntity createVictor(Date date) throws PWCGException
    {
        VictoryEntity victor = new VictoryEntity();
        
        Company squadron = victorCrewMember.determineSquadron();

        TankType victorTankType = squadron.determineBestPlane(campaign.getDate());

        victor.setAirOrGround(Victory.AIRCRAFT);
        victor.setType(victorTankType.getDisplayName());
        victor.setName(victorTankType.getDisplayName());
        victor.setSquadronName(squadron.determineDisplayName(date));
        victor.setCrewMemberName(victorCrewMember.getRank() + " " + victorCrewMember.getName());
        victor.setCrewMemberSerialNumber(victorCrewMember.getSerialNumber());
        victor.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
        
        return victor;
    }

    private VictoryEntity createVictim(Date date) throws PWCGException
    {
        victimCrewMember = victimGenerator.generateVictimAiCrew();
        victimPlane = victimGenerator.generateVictimPlane();
        if (victimCrewMember != null && victimPlane != null)
        {
            VictoryEntity victim = new VictoryEntity();            
            victim.setAirOrGround(Victory.AIRCRAFT);
            victim.setType(victimPlane.getType());
            victim.setName(victimPlane.getDisplayName());
            victim.setSquadronName(victimSquadron.determineDisplayName(date));
            victim.setCrewMemberSerialNumber(victimCrewMember.getSerialNumber());
            victim.setCrewMemberStatus(CrewMemberStatus.STATUS_KIA);
            return victim;
        }
        return null;
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

    public CrewMember getVictimCrewMember()
    {
        return victimCrewMember;
    }

    public EquippedTank getVictimPlane()
    {
        return victimPlane;
    }
}
