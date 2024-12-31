package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuIconLineType;


public class ArtillerySpotMapGrid 
{
	private Campaign campaign;
	private McuIcon[][] gridIcons = new McuIcon[ArtillerySpotGrid.GRID_ELEMENTS + 1][ArtillerySpotGrid.GRID_ELEMENTS + 1];

    public ArtillerySpotMapGrid (Campaign campaign)
    {
    	this.campaign = campaign;
    }
    
    public void create (ArtillerySpotGrid artySpotGrid) throws PWCGException 
    {
        createIcons(artySpotGrid);
        setMcuTargets();
        setMcuPosition(artySpotGrid);
    }

    private void createIcons(ArtillerySpotGrid artySpotGrid)
    {
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++rowIndex)
            {
                McuIcon gridIcon = new McuIcon("", "");
                gridIcon.setEnabled(1);
                gridIcon.setLineType(McuIconLineType.ICON_LINE_TYPE_THIN);
                
                gridIcons[columnIndex][rowIndex] = gridIcon;
            }
        }
    }

    private void setMcuTargets () throws PWCGException 
    {        
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++rowIndex)
            {
                McuIcon gridIcon = gridIcons[columnIndex][rowIndex];
                // Point the icon right and down except on the last column
                if (columnIndex < ( ArtillerySpotGrid.GRID_ELEMENTS))
                {
                    McuIcon gridIconRight = gridIcons[columnIndex+1][rowIndex];
                    gridIcon.setIconTarget(gridIconRight.getIndex());
                }
                // Point the icon down and down except on the last row
                if (rowIndex < ( ArtillerySpotGrid.GRID_ELEMENTS))
                {
                    McuIcon gridIconDown = gridIcons[columnIndex][rowIndex+1];
                    gridIcon.setIconTarget(gridIconDown.getIndex());
                }
            }
        }
    }

    private void setMcuPosition (ArtillerySpotGrid artySpotGrid) throws PWCGException 
    {
        ArtillerySpotGridElement[][]  artySpotGridElements = artySpotGrid.getGridElements();
        double placementAngle = artySpotGrid.getPlacementAngle();
        
        double placementAnglePlus90 = MathUtils.adjustAngle(placementAngle, 90);
        double placementAnglePlus180 = MathUtils.adjustAngle(placementAngle, 180);
        double placementAnglePlus270 = MathUtils.adjustAngle(placementAngle, 270);
        
        // Starting at the first attack area position, shift up and left for the first icon
        ArtillerySpotGridElement artySpotGridElement = artySpotGridElements[0][0];
        Coordinate upperLeft = artySpotGridElement.getPosition();
        upperLeft = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAnglePlus270, (ArtillerySpotGrid.GRID_SIZE_METERS / 2));
        upperLeft = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAnglePlus180, (ArtillerySpotGrid.GRID_SIZE_METERS / 2));

        // Place the icons based on upper left
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++rowIndex)
            {
                McuIcon gridIcon = gridIcons[columnIndex][rowIndex];
                
                Coordinate iconPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAngle, (columnIndex * ArtillerySpotGrid.GRID_SIZE_METERS));
                iconPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), iconPosition, placementAnglePlus90, (rowIndex * ArtillerySpotGrid.GRID_SIZE_METERS));
                gridIcon.setPosition(iconPosition);
            }
        }
    }

    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int columnIndex = 0; columnIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < ArtillerySpotGrid.GRID_ELEMENTS + 1; ++rowIndex)
            {
                gridIcons[columnIndex][rowIndex].write(writer);
            }
        }
    }

}
