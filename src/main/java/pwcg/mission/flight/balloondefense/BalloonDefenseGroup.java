package pwcg.mission.flight.balloondefense;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.Balloon;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointGoal;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuSpawn;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class BalloonDefenseGroup extends GroundUnit
{
    protected Campaign campaign;
	protected Balloon balloon = null;
	protected McuIcon balloonIcon = null;
	
	protected McuTimer winchCheckZoneTimer = null;
	protected McuCheckZone winchCheckZone = null;
	protected McuWaypoint winchDownWP = null;
	protected McuTimer winchDownTimer = null;

    protected McuTimer spawnTimer = new McuTimer();
    protected McuSpawn spawner = new McuSpawn();
    
    protected GroundAAABattery aaaMg = null;
    protected GroundAAABattery aaaArty = null;

	public BalloonDefenseGroup (Campaign campaign, GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super(pwcgGroundUnitInformation);

		this.campaign = campaign;
	}

	@Override
	public void createUnitMission() throws PWCGException  
	{
		createBalloon(pwcgGroundUnitInformation.getCountry());		
        createSpawnTimer();
		createWinch();
		createGroundTargetAssociations();

        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, pwcgGroundUnitInformation.getCountry(), pwcgGroundUnitInformation.getPosition().copy());
        aaaMg = groundUnitFactory.createAAAMGBattery(4, 4);
        aaaMg.setAiLevel(AiSkillLevel.COMMON);
        
        aaaArty = groundUnitFactory.createAAAArtilleryBattery(4, 4);
        aaaArty.setAiLevel(AiSkillLevel.COMMON);
	}

	protected void createWinch() 
	{
        // Enemy invokes winch down
        Coalition enemyCoalition = Coalition.getEnemyCoalition(pwcgGroundUnitInformation.getCountry());
	    
		winchCheckZone = new McuCheckZone();
		winchCheckZone.setZone(1000);
		winchCheckZone.triggerCheckZoneByPlaneCoalition(enemyCoalition);

		winchCheckZone.setName("Winch Check Zone for " + pwcgGroundUnitInformation.getName());
		winchCheckZone.setDesc("Winch Check Zone for " + pwcgGroundUnitInformation.getName());
		winchCheckZone.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		
		// Make the winch down CZ Timer
		winchCheckZoneTimer = new McuTimer();
		winchCheckZoneTimer.setName("Winch Check Zone Timer for " + pwcgGroundUnitInformation.getName());
		winchCheckZoneTimer.setDesc("Winch Check Zone Timer for " + pwcgGroundUnitInformation.getName());
		winchCheckZoneTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		
		
		// Make the winch down Timer
		winchDownTimer = new McuTimer();
		winchDownTimer.setName("Winch Check Zone Timer for " + pwcgGroundUnitInformation.getName());
		winchDownTimer.setDesc("Winch Check Zone Timer for " + pwcgGroundUnitInformation.getName());
		winchDownTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		winchDownTimer.setTimer(60);
		
		// Make the winch down WP
		Coordinate winchDownPos = balloon.getPosition().copy();
		winchDownPos.setYPos(0.0);
		
		winchDownWP = WaypointFactory.createLandingApproachWaypointType();		
		winchDownWP.setTriggerArea(10);
		winchDownWP.setDesc("Winch Down WP");
		winchDownWP.setName("Winch Down WP");
		winchDownWP.setPriority(WaypointPriority.PRIORITY_HIGH);	
		winchDownWP.setGoalType(WaypointGoal.GOAL_PRIMARY);
		winchDownWP.setSpeed(10);		
		winchDownWP.setPosition(winchDownPos);
		winchDownWP.setObject(balloon.getEntity().getIndex());
	}

	   /**
     * 
     */
    protected void createSpawnTimer() 
    {
        spawnTimer.setName("Spawn Timer");
        spawnTimer.setDesc("Spawn Timer");
        spawnTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
    }

	/**
	 * Create a mission for this flight
	 * @throws PWCGException 
	 * 
	 * @
	 */
	public void createBalloon(ICountry country) throws PWCGException  
	{
		balloon = new Balloon (country);
		
		Coordinate balloonPosition = pwcgGroundUnitInformation.getPosition().copy();
		balloonPosition.setYPos(Balloon.BALLOON_ALTITUDE);
		
		Orientation orient = new Orientation();
		orient.setyOri(RandomNumberGenerator.getRandom(360));

		balloon.setPosition(balloonPosition);
		balloon.setOrientation(orient.copy());
		balloon.populateEntity(balloonPosition, orient);
		
		spawner.setName("Balloon Spawn");      
		spawner.setDesc("Balloon Spawn");      
		spawner.setPosition(balloonPosition.copy());
		spawner.setOrientation(orient.copy());
		spawner.setObject(balloon.getEntity().getIndex());

		balloonIcon = new McuIcon(balloon, pwcgGroundUnitInformation.getCountry().getSide());
	}

	@Override
	protected void createGroundTargetAssociations()
	{
		pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(spawnTimer.getIndex());
        
        // Connect winch down CZ Timer -> winch down CZ -> winch downtimer -> winch down WP
        spawnTimer.setTarget(spawner.getIndex());
        spawnTimer.setTarget(winchCheckZoneTimer.getIndex());
        winchCheckZoneTimer.setTarget(winchCheckZone.getIndex());
        winchCheckZone.setTarget(winchDownTimer.getIndex());
        winchDownTimer.setTarget(winchDownWP.getIndex());
	}

	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		balloon.write(writer);
		balloonIcon.write(writer);

		pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);

	    spawnTimer.write(writer);
	    spawner.write(writer);
        winchCheckZoneTimer.write(writer);
		winchCheckZone.write(writer);
		winchDownTimer.write(writer);
		winchDownWP.write(writer);
		
        if (aaaMg != null)
        {
            aaaMg.write(writer);
        }
        
        if (aaaArty != null)
        {
            aaaArty.write(writer);
        }
	}

	public Balloon getBalloon() 
	{
		return balloon;
	}

	@Override
	public List<IVehicle> getVehicles() 
	{
		return new ArrayList<IVehicle>();
	}

    public void setBalloonCheckZoneForPlayer(List<Integer> playerPlaneIds)
    {
        MissionBeginUnitCheckZone mbu = (MissionBeginUnitCheckZone) pwcgGroundUnitInformation.getMissionBeginUnit();
        for (int playerPlaneId : playerPlaneIds)
        {
        	mbu.getSelfDeactivatingCheckZone().setCZObject(playerPlaneId);
        }
    }
}	

