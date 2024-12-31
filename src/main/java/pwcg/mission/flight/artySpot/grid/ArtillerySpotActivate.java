package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotActivate 
{
    private McuTimer activateTimer = new McuTimer();
	private McuActivate activate = new McuActivate();
	
	public void create(Coordinate gridPosition, String type, String position) 
	{
        activate.setName (type + "_" + position + "_Activate");
        activate.setPosition(gridPosition);
        
        // Allow enough seconds to allow time for deactivate process to complete
        activateTimer.setName (type + "_" + position + "_ActivateTimer");
        activateTimer.setTimerTarget(1);
		activateTimer.setPosition(gridPosition);
		activateTimer.setTimerTarget(activate.getIndex());
	}

	public void write(BufferedWriter writer) throws PWCGException 
	{
		activateTimer.write(writer);
        activate.write(writer);
	}

	public void addActivateTarget(int index)
    {
        activate.setActivateTarget (index);
    }
    
    public void addActivateTimerTarget(int index)
    {
        activateTimer.setTimerTarget (index);
    }

    public McuTimer getActivateTimer()
    {
        return this.activateTimer;
    }
	
}
