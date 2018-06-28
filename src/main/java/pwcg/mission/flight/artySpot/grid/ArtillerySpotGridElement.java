package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.mcu.McuAttackArea;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotGridElement 
{
	private McuTimer elementRowtriggerTimer = new McuTimer();
    private McuTimer elementColumnTriggerTimer = new McuTimer();
	private McuAttackArea attackArea = new McuAttackArea();
	private McuSubtitle fireSubtitle = new McuSubtitle();

	public ArtillerySpotGridElement ()
	{
	}
	
	public void createGridElement(String name, ArtillerySpotArtilleryGroup friendlyArtillery, ArtillerySpotDeactivate deactivate) 
	{
        
        // Subtitles        
        fireSubtitle.setName(name + "_Subtitle");
        fireSubtitle.setText("Fire on Grid " + name);

        // Row and column triggers for this element
        elementRowtriggerTimer.setName(name + "_RowTimer");
        elementColumnTriggerTimer.setName(name + "_ColumnTimer");
		attackArea.setName(name + "_AttackArea");
		
		// row timer
		elementRowtriggerTimer.setTimer(0);

        // column timer - causes deactivate of all timers and trigger of firing timer
        elementColumnTriggerTimer.setTimer(0);
		
        elementRowtriggerTimer.setTarget(elementColumnTriggerTimer.getIndex());
        elementColumnTriggerTimer.setTarget(attackArea.getIndex());
        elementColumnTriggerTimer.setTarget(fireSubtitle.getIndex());
        elementColumnTriggerTimer.setTarget(deactivate.getMasterDeactivateTimer().getIndex());

		// Link the attack area to the main gun
        attackArea.setObject(friendlyArtillery.getLeadIndex());
		
		attackArea.setAttackAir(0);
		attackArea.setAttackGround(1);
		attackArea.setAttackGTargets(0);
		attackArea.setAttackArea(ArtillerySpotGrid.GRID_SIZE_METERS / 2);  // Radius not diameter
		attackArea.setPriority(WaypointPriority.PRIORITY_HIGH);
		attackArea.setTime(600);
		
        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(fireSubtitle.getLcText(), fireSubtitle.getText());
	}
	
	/**
	 * @param writer
	 */
	public void setMcuPosition(Coordinate gridPosition) 
	{
		elementRowtriggerTimer.setPosition(gridPosition);
		elementColumnTriggerTimer.setPosition(gridPosition);
		fireSubtitle.setPosition(gridPosition);
        attackArea.setPosition(gridPosition);
	}

	/**
	 * @param writer
	 * @throws PWCGIOException 
	 */
	public void write(BufferedWriter writer) throws PWCGIOException 
	{
		elementRowtriggerTimer.write(writer);
		elementColumnTriggerTimer.write(writer);
		fireSubtitle.write(writer);
        attackArea.write(writer);
	}

	
	public McuTimer getElementRowTriggerTimer() 
	{
		return elementRowtriggerTimer;
	}
	
	public McuTimer getElementColumnTriggerTimer() 
	{
		return elementColumnTriggerTimer;
	}
	
	public Coordinate getPosition()
	{
	    return attackArea.getPosition().copy();
	}
}
