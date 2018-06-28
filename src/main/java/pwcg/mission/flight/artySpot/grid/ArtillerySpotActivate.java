package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotActivate 
{
    protected McuTimer activateTimer = new McuTimer();
	protected McuActivate activate = new McuActivate();
	
	ArtillerySpotActivate()
	{
	}

	/**
	 * @param writer
	 */
	protected void create(Coordinate gridPosition, String type, String position) 
	{
        activate.setName (type + "_" + position + "_Activate");
        activate.setPosition(gridPosition);
        
        // Allow enough seconds to allow time for deactivate process to complete
        activateTimer.setName (type + "_" + position + "_ActivateTimer");
        activateTimer.setTimer(1);
		activateTimer.setPosition(gridPosition);
		activateTimer.setTarget(activate.getIndex());
	}

	/**
	 * @param writer
	 * @throws PWCGIOException 
	 */
	public void write(BufferedWriter writer) throws PWCGIOException 
	{
		activateTimer.write(writer);
        activate.write(writer);
	}

    /**
     * @param index
     */
    public void addActivateTarget(int index)
    {
        activate.setTarget (index);
    }

    /**
     * @param index
     */
    public void addActivateTimerTarget(int index)
    {
        activateTimer.setTarget (index);
    }

    /**
     * @return the activateTimer
     */
    public McuTimer getActivateTimer()
    {
        return this.activateTimer;
    }
	
}
