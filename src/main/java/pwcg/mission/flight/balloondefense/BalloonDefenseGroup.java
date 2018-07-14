package pwcg.mission.flight.balloondefense;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
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

	public BalloonDefenseGroup (Campaign campaign) 
	{
		super(TacticalTarget.TARGET_BALLOON);
		
		this.campaign = campaign;
	}

	public void initialize ( MissionBeginUnitCheckZone missionBeginUnit, Coordinate balloonPosition, ICountry country) 
	{
		String countryName = country.getNationality();
		String name = countryName + " Balloon";

        super.initialize (missionBeginUnit, name, balloonPosition, balloonPosition, country);

		this.country = country;		
	}

	@Override
	public void createUnitMission() throws PWCGException  
	{
		createBalloon(country);		
        createSpawnTimer();
		createWinch();
		createGroundTargetAssociations();

        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, country, position.copy());
        aaaMg = groundUnitFactory.createAAAMGBattery(4, 4);
        aaaMg.setAiLevel(AiSkillLevel.COMMON);
        
        aaaArty = groundUnitFactory.createAAAArtilleryBattery(4, 4);
        aaaArty.setAiLevel(AiSkillLevel.COMMON);
	}

	protected void createWinch() 
	{
        // Enemy invokes winch down
        Coalition enemyCoalition = Coalition.getEnemyCoalition(country);
	    
		winchCheckZone = new McuCheckZone(enemyCoalition);
		winchCheckZone.setZone(1000);

		winchCheckZone.setName("Winch Check Zone for " + name);
		winchCheckZone.setDesc("Winch Check Zone for " + name);
		winchCheckZone.setPosition(position.copy());
		
		// Make the winch down CZ Timer
		winchCheckZoneTimer = new McuTimer();
		winchCheckZoneTimer.setName("Winch Check Zone Timer for " + name);
		winchCheckZoneTimer.setDesc("Winch Check Zone Timer for " + name);
		winchCheckZoneTimer.setPosition(position.copy());
		
		
		// Make the winch down Timer
		winchDownTimer = new McuTimer();
		winchDownTimer.setName("Winch Check Zone Timer for " + name);
		winchDownTimer.setDesc("Winch Check Zone Timer for " + name);
		winchDownTimer.setPosition(position.copy());
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
        spawnTimer.setPosition(position.copy());
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
		
		position.setYPos(Balloon.BALLOON_ALTITUDE);
		
		Orientation orient = new Orientation();
		orient.setyOri(RandomNumberGenerator.getRandom(360));

		balloon.setPosition(position.copy());
		balloon.setOrientation(orient.copy());
		balloon.populateEntity(position, orient);
		
		spawner.setName("Balloon Spawn");      
		spawner.setDesc("Balloon Spawn");      
		spawner.setPosition(position.copy());
		spawner.setOrientation(orient.copy());
		spawner.setObject(balloon.getEntity().getIndex());

		balloonIcon = new McuIcon(balloon);
	}

	/* (non-Javadoc)
	 * @see rof.campaign.mission.Unit#linkTimers()
	 */
	@Override
	protected void createGroundTargetAssociations()
	{
		missionBeginUnit.linkToMissionBegin(spawnTimer.getIndex());
        
        // Connect winch down CZ Timer -> winch down CZ -> winch downtimer -> winch down WP
        spawnTimer.setTarget(spawner.getIndex());
        spawnTimer.setTarget(winchCheckZoneTimer.getIndex());
        winchCheckZoneTimer.setTarget(winchCheckZone.getIndex());
        winchCheckZone.setTarget(winchDownTimer.getIndex());
        winchDownTimer.setTarget(winchDownWP.getIndex());
	}

	/**
	 * Write the mission to a file
	 * 
	 * @param writer
	 * @throws PWCGException 
	 * @
	 */
	@Override
	public void write(BufferedWriter writer) throws PWCGException 
	{
		balloon.write(writer);
		balloonIcon.write(writer);

        missionBeginUnit.write(writer);

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

	/**
	 * @return
	 */
	public Balloon getBalloon() 
	{
		return balloon;
	}
	
	/**
	 * TODO IMPROVEMENT maybe put some trucks around the balloon
	 */
	@Override
	public List<IVehicle> getVehicles() 
	{
		return new ArrayList<IVehicle>();
	}

    
    /**
     * @param index
     */
    public void setBalloonCheckZoneForPlayer(int index)
    {
        MissionBeginUnitCheckZone mbu = (MissionBeginUnitCheckZone) missionBeginUnit;
        mbu.getCheckZone().setCZObject(index);
    }
}	

