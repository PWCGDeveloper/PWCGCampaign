package pwcg.campaign.personnel;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadmember.VictoryEntity;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.building.PwcgStructure;

public class StructureVictoryBuilder
{
    private Campaign campaign;
    private SquadronMember victorPilot;
    private PwcgStructure victimStructure;

    public StructureVictoryBuilder (Campaign campaign, SquadronMember victorPilot, PwcgStructure victimStructure)
    {
        this.campaign = campaign;
        this.victimStructure = victimStructure;
        this.victorPilot = victorPilot;
    }
    
    public Victory generateOutOfMissionVictory(Date date) throws PWCGException
    {        
        Victory victory = null;
        try
        {
            if (victimStructure != null)
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

    private Victory createVictory(Date date) throws PWCGException
    {
        VictoryEntity victim = createVictim(date);
        VictoryEntity victor = createVictor(date);
        
        Victory victory = null;
        if (victim != null && victor != null)
        {
	        victory = new Victory();
	        createVictoryHeader(date, victory, victorPilot.getSide().getOppositeSide());
	
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

        PlaneType victorPlaneType = squadron.determineBestPlane(date);
        if (victorPlaneType == null)
        {
            return null;
        }

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
        VictoryEntity victim = new VictoryEntity();            
        victim.setAirOrGround(Victory.VEHICLE);
        victim.setType(victimStructure.getDescription());
        victim.setName(victimStructure.getDescription());
        victim.setSquadronName("");
        victim.setPilotName("");
        victim.setPilotSerialNumber(SerialNumber.NO_SERIAL_NUMBER);
        victim.setPilotStatus(SquadronMemberStatus.STATUS_KIA);
        return victim;
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
}
