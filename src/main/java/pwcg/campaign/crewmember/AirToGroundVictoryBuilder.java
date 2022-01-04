package pwcg.campaign.crewmember;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankType;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirToGroundVictoryBuilder
{
    private CrewMember victorCrewMember;
    private IVehicle victimVehicle;

    public AirToGroundVictoryBuilder (CrewMember victorCrewMember, IVehicle victimVehicle)
    {
        this.victimVehicle = victimVehicle;
        this.victorCrewMember = victorCrewMember;
    }
    
    public Victory generateOutOfMissionVictory(Date date) throws PWCGException
    {        
        Victory victory = null;
        try
        {
            if (victimVehicle != null)
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
	        createVictoryHeader(date, victory, victorCrewMember.getSide().getOppositeSide());
	
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
        Company squadron = victorCrewMember.determineSquadron();
        if (squadron != null)
        {
            TankType victorTankType = squadron.determineBestPlane(date);
    
            if (victorTankType != null)
            {
                VictoryEntity victor = new VictoryEntity();
                victor.setAirOrGround(Victory.AIRCRAFT);
                victor.setType(victorTankType.getDisplayName());
                victor.setName(victorTankType.getDisplayName());
                victor.setSquadronName(squadron.determineDisplayName(date));
                victor.setCrewMemberName(victorCrewMember.getRank() + " " + victorCrewMember.getName());
                victor.setCrewMemberSerialNumber(victorCrewMember.getSerialNumber());
                victor.setCrewMemberStatus(CrewMemberStatus.STATUS_ACTIVE);
                return victor;
            }
        }
        
        return null;
    }

    private VictoryEntity createVictim(Date date) throws PWCGException
    {
        VictoryEntity victim = new VictoryEntity();            
        victim.setAirOrGround(Victory.VEHICLE);
        victim.setType(victimVehicle.getVehicleType());
        victim.setName(victimVehicle.getVehicleName());
        victim.setSquadronName("");
        victim.setCrewMemberName("");
        victim.setCrewMemberSerialNumber(SerialNumber.NO_SERIAL_NUMBER);
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
