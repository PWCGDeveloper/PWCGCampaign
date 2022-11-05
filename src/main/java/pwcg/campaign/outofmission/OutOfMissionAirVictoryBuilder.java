package pwcg.campaign.outofmission;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryEntity;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;

public class OutOfMissionAirVictoryBuilder
{
    private Campaign campaign;
    private Squadron victimSquadron;
    private IVictimGenerator victimGenerator;
    private SquadronMember victorPilot;
    private SquadronMember victimPilot;
    private EquippedPlane victimPlane;

    public OutOfMissionAirVictoryBuilder (Campaign campaign, Squadron victimSquadron, IVictimGenerator victimGenerator, SquadronMember victorPilot)
    {
        this.campaign = campaign;
        this.victimSquadron = victimSquadron;
        this.victimGenerator = victimGenerator;
        this.victorPilot = victorPilot;
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
        
        Squadron squadron = victorPilot.determineSquadron();

        PlaneType victorPlaneType = squadron.determineBestPlane(campaign.getDate());

        victor.setAirOrGround(Victory.AIRCRAFT);
        victor.setType(victorPlaneType.getDisplayName());
        victor.setName(victorPlaneType.getDisplayName());
        victor.setSquadronName(squadron.determineDisplayName(date));
        victor.setPilotName(victorPilot.getRank() + " " + victorPilot.getName());
        victor.setPilotSerialNumber(victorPilot.getSerialNumber());
        victor.setPilotStatus(SquadronMemberStatus.STATUS_ACTIVE);
        
        return victor;
    }

    private VictoryEntity createVictim(Date date) throws PWCGException
    {
        victimPilot = victimGenerator.generateVictimAiCrew();
        victimPlane = victimGenerator.generateVictimPlane();
        if (victimPilot != null && victimPlane != null)
        {
            VictoryEntity victim = new VictoryEntity();            
            victim.setAirOrGround(Victory.AIRCRAFT);
            victim.setType(victimPlane.getType());
            victim.setName(victimPlane.getDisplayName());
            victim.setSquadronName(victimSquadron.determineDisplayName(date));
            victim.setPilotSerialNumber(victimPilot.getSerialNumber());
            victim.setPilotStatus(SquadronMemberStatus.STATUS_KIA);
            return victim;
        }
        return null;
    }

    private String getEventLocation(Side enemySide, Date date) throws PWCGException
    {
        String eventLocationDescription = "";
        
        Coordinate squadronPosition = victorPilot.determineSquadron().determineCurrentPosition(date);
        if (squadronPosition != null)
        {
            FrontLinesForMap frontLines = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(date);
            FrontLinePoint eventPosition = frontLines.findCloseFrontPositionForSide(squadronPosition, 100000, enemySide);
    
            eventLocationDescription =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getGroupManager().getTownFinder().findClosestTown(eventPosition.getPosition()).getName();
            if (eventLocationDescription == null || eventLocationDescription.isEmpty())
            {
                eventLocationDescription = "";
            }
        }
    
        return eventLocationDescription;
    }

    public SquadronMember getVictimPilot()
    {
        return victimPilot;
    }

    public EquippedPlane getVictimPlane()
    {
        return victimPlane;
    }
}
