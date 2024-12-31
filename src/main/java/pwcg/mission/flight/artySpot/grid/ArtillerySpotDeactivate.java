package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuMissionStart;
import pwcg.mission.mcu.McuTimer;
import pwcg.core.utils.MathUtils;


public class ArtillerySpotDeactivate
{
    private McuMissionStart artySpotGridMissionBegin = new McuMissionStart();

    private McuTimer masterDeactivateTimer = new McuTimer();
    private McuDeactivate masterDeactivate = new McuDeactivate();
    private Campaign campaign;

    public ArtillerySpotDeactivate (Campaign campaign)
    {
    	this.campaign = campaign;
    }
    
    /**
     * @param position
     * @throws PWCGException 
     */
    public void create (Coordinate gridPosition) throws PWCGException 
    {
        // Put the deactivate MCUs outside of the grid
        Coordinate deactivatePosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), gridPosition, 0, 2000);
        
        artySpotGridMissionBegin.setName("GridMB");     
        artySpotGridMissionBegin.setDesc("Grid MB");        
        artySpotGridMissionBegin.setPosition(deactivatePosition);

        masterDeactivateTimer.setTime(0);
        masterDeactivateTimer.setName("GridDeactivateTimer");       
        masterDeactivateTimer.setDesc("Grid Deactivate Timer");     
        masterDeactivateTimer.setPosition(deactivatePosition);

        masterDeactivate.setName("GridDeactivate");     
        masterDeactivate.setDesc("Grid Deactivate");
        masterDeactivate.setPosition(deactivatePosition);
    }

    /**
     * Attack areas are target of deactivate to stop firing.
     * Deactivate all AttackAreas entities at once.
     */
    public void createTargets(ArtillerySpotGrid artySpotGrid)
    {

        // Deactivate on mission begin?
        artySpotGridMissionBegin.setMissionBeginTarget(masterDeactivateTimer.getIndex());
        
        // Hook the deactivate timer to the subtitle
        masterDeactivateTimer.setTimerTarget(masterDeactivate.getIndex());

        // Hook the deactivate to every column timer
        ArtillerySpotGridElement[][] gridElements = artySpotGrid.getGridElements();                
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS; ++rowIndex)
            {
                ArtillerySpotGridElement gridElement = gridElements[columnIndex][rowIndex];
                
                 // Deactivate every row and column trigger timer
                masterDeactivate.setDeactivateTarget(gridElement.getElementRowTriggerTimer().getIndex());
                masterDeactivate.setDeactivateTarget(gridElement.getElementColumnTriggerTimer().getIndex());
            }
        }
    }
    

    /**
     * @param writer
     * @throws PWCGException 
     */
    public void write(BufferedWriter writer) throws PWCGException 
    {
        artySpotGridMissionBegin.write(writer);
        masterDeactivateTimer.write(writer);
        masterDeactivate.write(writer);
    }

    /**
     * @return the masterDeactivateTimer
     */
    public McuTimer getMasterDeactivateTimer()
    {
        return this.masterDeactivateTimer;
    }
}
