package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;
import java.io.IOException;

import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.McuCheckZone;
import pwcg.mission.mcu.McuTimer;
import pwcg.mission.mcu.McuWaypoint;

public class BalloonWinch
{
    protected int index = IndexGenerator.getInstance().getNextIndex();  
    private MissionBeginUnit missionBeginUnit = null;
    private McuTimer winchCheckZoneTimer = null;
	private McuCheckZone winchCheckZone = null;
	private McuWaypoint winchDownWP = null;
	private McuTimer winchDownTimer = null;

    private BalloonUnit balloonUnit = null;    
    private GroundUnitInformation pwcgGroundUnitInformation;

    public BalloonWinch (BalloonUnit balloonUnit, GroundUnitInformation pwcgGroundUnitInformation) 
	{
	    this.balloonUnit = balloonUnit;
        this.pwcgGroundUnitInformation  = pwcgGroundUnitInformation;
	}

	public void createWinchUnit() throws PWCGException  
	{
		createWinch();
		createGroundTargetAssociations();
	}

	private void createWinch() throws PWCGException 
	{
        Coalition enemyCoalition = CoalitionFactory.getEnemyCoalition(pwcgGroundUnitInformation.getCountry());
	    
        missionBeginUnit = new MissionBeginUnit(pwcgGroundUnitInformation.getPosition().copy());
        
		winchCheckZone = new McuCheckZone();
		winchCheckZone.setZone(1000);
		winchCheckZone.triggerCheckZoneByCoalition(enemyCoalition);

		winchCheckZone.setName("Winch Check Zone");
		winchCheckZone.setDesc("Winch Check Zone");
		winchCheckZone.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		
		// Make the winch down CZ Timer
		winchCheckZoneTimer = new McuTimer();
		winchCheckZoneTimer.setName("Winch Check Zone Timer");
		winchCheckZoneTimer.setDesc("Winch Check Zone Timer");
		winchCheckZoneTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		
		
		// Make the winch down Timer
		winchDownTimer = new McuTimer();
		winchDownTimer.setName("Winch down timer");
		winchDownTimer.setDesc("Winch down timer");
		winchDownTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		winchDownTimer.setTimer(60);
		
		// Make the winch down WP
		Coordinate winchDownPos = pwcgGroundUnitInformation.getPosition().copy();
		winchDownPos.setYPos(0.0);
		
		winchDownWP = WaypointFactory.createLandingApproachWaypointType();		
		winchDownWP.setTriggerArea(10);
		winchDownWP.setDesc("Winch Down WP");
		winchDownWP.setName("Winch Down WP");
		winchDownWP.setPriority(WaypointPriority.PRIORITY_HIGH);	
		winchDownWP.setSpeed(10);		
		winchDownWP.setPosition(winchDownPos);
        winchDownWP.setObject(balloonUnit.getVehicle().getEntity().getIndex());
	}

	private void createGroundTargetAssociations()
	{
		missionBeginUnit.linkToMissionBegin(winchCheckZoneTimer.getIndex());
        winchCheckZoneTimer.setTarget(winchCheckZone.getIndex());
        winchCheckZone.setTarget(winchDownTimer.getIndex());
        winchDownTimer.setTarget(winchDownWP.getIndex());
	}

    public void write(BufferedWriter writer) throws PWCGException 
    {      
        try
        {
            writer.write("Group");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writer.write("  Name = Balloon Winch\";");
            writer.newLine();
            writer.write("  Index = " + index + ";");
            writer.newLine();
            writer.write("  Desc = Balloon Winch\";");
            writer.newLine();

            missionBeginUnit.write(writer);
            winchCheckZoneTimer.write(writer);
            winchCheckZone.write(writer);
            winchDownTimer.write(writer);
            winchDownWP.write(writer);
            
            writer.write("}");
            writer.newLine();
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
    }
}	

