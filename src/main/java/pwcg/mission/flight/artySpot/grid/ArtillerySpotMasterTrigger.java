package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotMasterTrigger
{
    private McuTimer masterTriggerTimerTimer = new McuTimer();

    /**
     * @param position
     * @throws PWCGException 
     */
    public void create (Coordinate gridPosition) throws PWCGException 
    {
        masterTriggerTimerTimer.setName("MasterActivateTimer");
        masterTriggerTimerTimer.setTimer(1);
        masterTriggerTimerTimer.setPosition(gridPosition.copy());
    }
    

    /**
     * Associate the master trigger with the element row trigger
     */
    public void createTargets(ArtillerySpotGrid artySpotGrid) 
    {
        // Hook the deactivate to every column timer
        ArtillerySpotGridElement[][] gridElements = artySpotGrid.getGridElements();                
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS; ++rowIndex)
            {
                ArtillerySpotGridElement gridElement = gridElements[columnIndex][rowIndex];
                
                // Trigger every row trigger timer
                // Only one will be active
                masterTriggerTimerTimer.setTarget(gridElement.getElementRowTriggerTimer().getIndex());
            }
        }
    }

    /**
     * @param writer
     * @throws PWCGIOException 
     */
    public void write(BufferedWriter writer) throws PWCGIOException 
    {
        masterTriggerTimerTimer.write(writer);
    }


    /**
     * @return the masterTriggerTimerTimer
     */
    public McuTimer getMasterTriggerTimerTimer()
    {
        return this.masterTriggerTimerTimer;
    }
}
