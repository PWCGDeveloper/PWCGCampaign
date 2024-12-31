package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;


public class ArtillerySpotActivateSet
{
    private ArtillerySpotActivate[] columnActivates = new ArtillerySpotActivate[ArtillerySpotGrid.GRID_ELEMENTS];
    private ArtillerySpotActivate[] rowActivates = new ArtillerySpotActivate[ArtillerySpotGrid.GRID_ELEMENTS];
    private Campaign campaign;

    public ArtillerySpotActivateSet (Campaign campaign)
    {
    	this.campaign = campaign;
    }
    
    
    public void createActivates() throws PWCGException
    {       
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            columnActivates[i] = new ArtillerySpotActivate();
            rowActivates[i] = new ArtillerySpotActivate();
        }
    }
    
    public void createRowAndColumnActivates(ArtillerySpotGrid artySpotGrid, Coordinate gridPosition) throws PWCGException 
    {
        double placementAngle = artySpotGrid.getPlacementAngle();
        
        double placementAnglePlus90 = MathUtils.adjustAngle(placementAngle, 90);
        double placementAnglePlus180 = MathUtils.adjustAngle(placementAngle, 180);
        double placementAnglePlus270 = MathUtils.adjustAngle(placementAngle, 270);
        
        Coordinate upperLeft = MathUtils.calcNextCoord(campaign.getCampaignMap(), gridPosition, placementAnglePlus270, 1200);
        upperLeft = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAnglePlus180, 1200);
            
        // Column starts above upper left and moves right
        Coordinate columnPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAnglePlus270, ArtillerySpotGrid.GRID_SIZE_METERS);
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {

            String columnName = ArtillerySpotHeader.columnNames[i];
            columnPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), columnPosition, placementAngle, ArtillerySpotGrid.GRID_SIZE_METERS);
            columnActivates[i].create(columnPosition, "Column", columnName);
        }
        
        // Row starts to the left of upper left and moves down
        Coordinate rowPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAnglePlus180, ArtillerySpotGrid.GRID_SIZE_METERS);
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            String rowName = ArtillerySpotHeader.rowNames[i];
            rowPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), rowPosition, placementAnglePlus90, ArtillerySpotGrid.GRID_SIZE_METERS);
            rowActivates[i].create(columnPosition, "Row", rowName);
        }
    }


    /**
     * Associate the activate trigger with the row trigger
     */
    public void createTargets(ArtillerySpotGrid artySpotGrid) 
    {
        ArtillerySpotGridElement[][] gridElements = artySpotGrid.getGridElements();                
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS; ++columnIndex)
        {
            // Force complete ends last fire mission
            columnActivates[columnIndex].addActivateTimerTarget(artySpotGrid.getArtillerySpotForceComplete().getForceCompleteTimer().getIndex());
            
            // Master trigger timer activates every element column timer
            columnActivates[columnIndex].addActivateTimerTarget(artySpotGrid.getArtillerySpotMasterTrigger().getMasterTriggerTimerTimer().getIndex());
            
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS; ++rowIndex)
            {
                ArtillerySpotGridElement gridElement = gridElements[columnIndex][rowIndex];
                
                // Row header activates a row of triggers
                rowActivates[rowIndex].addActivateTarget(gridElement.getElementRowTriggerTimer().getIndex());
                
                // Column header activates a column of triggers
                columnActivates[columnIndex].addActivateTarget(gridElement.getElementColumnTriggerTimer().getIndex());
            }
        }
    }
    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            columnActivates[i].write(writer);
        }
        
        for (int i = 0; i < ArtillerySpotGrid.GRID_ELEMENTS; ++i)
        {
            rowActivates[i].write(writer);
        }
        
        // TODO RESORE for deactivate
        //artySpotDeactivate.write(writer);
    }

    public ArtillerySpotActivate[] getRowActivates()
    {
        return this.rowActivates;
    }

    /**
     * @return the columnActivates
     */
    public ArtillerySpotActivate[] getColumnActivates()
    {
        return this.columnActivates;
    }
    
    

}
