package pwcg.mission.flight.balloondefense;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.Mission;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuCover;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerBalloonDefenseFlight extends Flight
{
	protected McuTimer coverTimer = null;
	protected McuCover cover = null;

	protected McuTimer deactivateAttackTimer = null;
	protected McuDeactivate deactivateAttackEntity = null;

	protected McuTimer deactivateCoverTimer = null;
	protected McuDeactivate deactivateCoverEntity = null;

	protected McuTimer attackAreaTimer = null;
	protected McuAttackArea attackArea = null;

	protected BalloonDefenseGroup balloonUnit = null;

    public PlayerBalloonDefenseFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, BalloonDefenseGroup balloonUnit)
    {
        super (flightInformation, missionBeginUnit);
        this.balloonUnit = balloonUnit;
   }

	@Override
	public void createUnitMission() throws PWCGException  
	{
        super.createUnitMission();
        
		// These are the balloon defense specific things
		createAttackArea();
		createCover();
		createDeactivate();
		createActivation();
	}

	@Override
	public List<McuWaypoint> createWaypoints(Mission mission, Coordinate startPosition) throws PWCGException 
	{
		BalloonDefenseWaypoints waypointGenerator = new BalloonDefenseWaypoints(
					startPosition, 
		       		getTargetCoords(), 
		       		this,
		       		mission);

        List<McuWaypoint> waypointList = waypointGenerator.createWaypoints();
        
        return waypointList;
	}

	protected void createAttackArea() throws PWCGException 
	{
		Coordinate areaCoords = balloonUnit.getBalloon().getPosition().copy();
		areaCoords.setYPos(areaCoords.getYPos() + 500);
		
		attackArea  = new McuAttackArea();
		attackArea.setAttackGround(0);
		attackArea.setAttackGTargets(0);
		attackArea.setAttackAir(1);
		attackArea.setName("Balloon Defense Attack Area for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		attackArea.setDesc("Balloon Defense Attack Area for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		attackArea.setAttackArea(10000);		
		attackArea.setOrientation(new Orientation());		
		attackArea.setPosition(areaCoords);	
		attackArea.setObject(planes.get(0).getLinkTrId());
		
		attackAreaTimer  = new McuTimer();
		attackAreaTimer.setName(getSquadron().determineDisplayName(getCampaign().getDate()) + ": Balloon Defense Attack Area Timer");		
		attackAreaTimer.setDesc("Balloon Defense Attack Area Timer for " + getSquadron().determineDisplayName(getCampaign().getDate()));		
		attackAreaTimer.setPosition(areaCoords);
		attackAreaTimer.setTarget(attackArea.getIndex());
	}	

	protected void createCover() throws PWCGException 
	{
		// Cover the escorted flight
		cover  = new McuCover();
		cover.setName("Balloon Defense Cover for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		cover.setDesc("Balloon Defense Cover for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		cover.setPosition(balloonUnit.getBalloon().getPosition().copy());
		cover.setObject(planes.get(0).getEntity().getIndex());
		cover.setTarget(balloonUnit.getBalloon().getEntity().getIndex());

		// The cover timer.
		// Activate the cover command
		// Activate the escorted squadron
		coverTimer  = new McuTimer();
		coverTimer.setName("Balloon Defense Cover Timer for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		coverTimer.setDesc("Balloon Defense Cover Timer for " + getSquadron().determineDisplayName(getCampaign().getDate()));
		coverTimer.setPosition(balloonUnit.getBalloon().getPosition().copy());
		coverTimer.setTarget(cover.getIndex());
	}	

	protected void createDeactivate() 
	{
		// Deactivate the attack entity
		deactivateAttackEntity = new McuDeactivate();
		deactivateAttackEntity.setName("Balloon Cover Deactivate Attack");
		deactivateAttackEntity.setDesc("Balloon Cover Deactivate Attack");
		deactivateAttackEntity.setOrientation(new Orientation());
		deactivateAttackEntity.setPosition(balloonUnit.getBalloon().getPosition().copy());				
		deactivateAttackEntity.setTarget(attackArea.getIndex());
		
		deactivateAttackTimer  = new McuTimer();
		deactivateAttackTimer.setName("Balloon Cover Deactivate Attack Timer");
		deactivateAttackTimer.setDesc("Balloon Cover Deactivate Attack Timer");
		deactivateAttackTimer.setOrientation(new Orientation());
		deactivateAttackTimer.setPosition(balloonUnit.getBalloon().getPosition().copy());				
		deactivateAttackTimer.setTimer(1200);				
		deactivateAttackTimer.setTarget(deactivateAttackEntity.getIndex());
		
		// Deactivate the cover entity
		deactivateCoverEntity = new McuDeactivate();
		deactivateCoverEntity.setName("Balloon Cover Deactivate Cover");
		deactivateCoverEntity.setDesc("Balloon Cover Deactivate Cover");
		deactivateCoverEntity.setOrientation(new Orientation());
		deactivateCoverEntity.setPosition(balloonUnit.getBalloon().getPosition().copy());				
		deactivateCoverEntity.setTarget(cover.getIndex());
		
		deactivateCoverTimer  = new McuTimer();
		deactivateCoverTimer.setName("Balloon Cover Deactivate Cover Timer");
		deactivateCoverTimer.setDesc("Balloon Cover Deactivate Cover Timer");
		deactivateCoverTimer.setOrientation(new Orientation());
		deactivateCoverTimer.setPosition(balloonUnit.getBalloon().getPosition().copy());				
		deactivateCoverTimer.setTimer(2);				
		deactivateCoverTimer.setTarget(deactivateCoverEntity.getIndex());
	}

	public Coordinate getCoordinatesToIntersectWithPlayer() throws PWCGException 
	{
 		return getTargetCoords();
	}

	@Override
    protected void createFlightSpecificTargetAssociations()
	{
        linkWPToPlane(getLeadPlane(), waypointPackage.getWaypointsForLeadPlane());

        McuWaypoint prevWP = null;
		for (McuWaypoint nextWP : waypointPackage.getWaypointsForLeadPlane())
		{
		    if (prevWP != null)
		    {			
    			if (prevWP.getName().equals(WaypointType.RECON_WAYPOINT.getName()))
    			{
    				prevWP.setTarget(coverTimer.getIndex());
    				coverTimer.setTarget(attackAreaTimer.getIndex());
    				attackAreaTimer.setTarget(deactivateAttackTimer.getIndex());
    				deactivateAttackTimer.setTarget(deactivateCoverTimer.getIndex());
    				deactivateCoverTimer.setTarget(nextWP.getIndex());
    			}
    			else
    			{
    				prevWP.setTarget(nextWP.getIndex());
    			}
		    }
		    
			prevWP = nextWP;
		}
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		super.write(writer);
		
		coverTimer.write(writer);
		cover.write(writer);
		
		attackAreaTimer.write(writer);
		attackArea.write(writer);
		
		deactivateAttackTimer.write(writer);
		deactivateAttackEntity.write(writer);
		
		deactivateCoverTimer.write(writer);
		deactivateCoverEntity.write(writer);
	}

	public String getMissionObjective() throws PWCGException 
	{
        String objective = "Defend our balloon" + formMissionObjectiveLocation(getTargetCoords().copy()) + ".";       

        return objective;
	}

}	

