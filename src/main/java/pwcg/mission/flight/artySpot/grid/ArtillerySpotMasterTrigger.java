package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuTimer;


public class ArtillerySpotMasterTrigger
{
    private McuTimer masterTriggerTimerTimer = new McuTimer();

    public void create (Coordinate gridPosition) throws PWCGException 
    {
        masterTriggerTimerTimer.setName("ASG Master Activate Timer");
        masterTriggerTimerTimer.setTime(1);
        masterTriggerTimerTimer.setPosition(gridPosition.copy());
    }

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
                masterTriggerTimerTimer.setTimerTarget(gridElement.getElementRowTriggerTimer().getIndex());
            }
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        masterTriggerTimerTimer.write(writer);
    }


    public McuTimer getMasterTriggerTimerTimer()
    {
        return this.masterTriggerTimerTimer;
    }
}
