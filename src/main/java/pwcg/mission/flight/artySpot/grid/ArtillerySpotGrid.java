package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;

public class ArtillerySpotGrid 
{
	public static final int GRID_ELEMENTS = 8;
	public static final int GRID_SIZE_METERS = 300;
	
	private ArtillerySpotGridElement[][] gridElements = new ArtillerySpotGridElement[GRID_ELEMENTS][GRID_ELEMENTS];
	
	private ArtillerySpotMapGrid artySpotMapGrid = new ArtillerySpotMapGrid();
	private ArtillerySpotMedia artillerySpotMedia = null;
    private ArtillerySpotActivateSet artillerySpotActivateSet= null;
    private ArtillerySpotMasterTrigger artillerySpotMasterTrigger = null;
    private ArtillerySpotDeactivate artySpotDeactivate = null;
    private ArtillerySpotForceComplete artillerySpotForceComplete = null;

    private double placementAngle = 270;

	/**
	 * 
	 */
	public ArtillerySpotGrid ()
	{
	}
	
	/**
	 * @param position
	 * @throws PWCGException 
	 * @throws PWCGDataValidityException 
	 */
	public void create (ArtillerySpotArtilleryGroup friendlyArtillery, Coordinate gridPosition) throws PWCGException 
	{
	    placementAngle = calcPlacementAngle(gridPosition);
		
        // Forces artillery to complete firing task
        artillerySpotForceComplete = new ArtillerySpotForceComplete();
        artillerySpotForceComplete.create(gridPosition, friendlyArtillery);

		artillerySpotActivateSet = new ArtillerySpotActivateSet();
		artillerySpotActivateSet.createActivates();

		// The artillery spot master trigger
		// Triggers every row element timer
		artillerySpotMasterTrigger = new ArtillerySpotMasterTrigger();
		artillerySpotMasterTrigger.create(gridPosition);
		
		// The deactivate
		artySpotDeactivate = new ArtillerySpotDeactivate();
		artySpotDeactivate.create(gridPosition);
        
		// Create the media
		artillerySpotMedia = new ArtillerySpotMedia();
		artillerySpotMedia.createMedia(this, gridPosition);
		
        // Once everything is created, tie the targets together
		createElements(friendlyArtillery);
        setMcuPosition(gridPosition);
		
		artillerySpotActivateSet.createRowAndColumnActivates(this, gridPosition);
		artillerySpotActivateSet.createTargets(this);
		
		artySpotDeactivate.createTargets(this);

		artillerySpotMasterTrigger.createTargets(this);

        // The map grid is the overlay for the map
        artySpotMapGrid.create(this);
	}

    /**
     * Create the 64 grid elements
     */
    private void createElements(ArtillerySpotArtilleryGroup friendlyArtillery) 
    {
        for (int columnIndex = 0; columnIndex < GRID_ELEMENTS; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < GRID_ELEMENTS; ++rowIndex)
            {
                String name = ArtillerySpotHeader.columnNames[columnIndex] + "-" + ArtillerySpotHeader.rowNames[rowIndex];

                gridElements[columnIndex][rowIndex] = new ArtillerySpotGridElement();
                gridElements[columnIndex][rowIndex].createGridElement(
                                name, 
                                friendlyArtillery,
                                artySpotDeactivate);
            }
        }
    }

    
	/**
	 * @param writer
	 * @throws PWCGException 
	 * @throws PWCGDataValidityException 
	 */
	private void setMcuPosition(Coordinate gridPosition) throws PWCGException 
	{
        double placementAnglePlus90 = MathUtils.adjustAngle(placementAngle, 90);
        double placementAnglePlus180 = MathUtils.adjustAngle(placementAngle, 180);
        double placementAnglePlus270 = MathUtils.adjustAngle(placementAngle, 270);
		
		Coordinate upperLeft = MathUtils.calcNextCoord(gridPosition, placementAnglePlus270, 1200);
		upperLeft = MathUtils.calcNextCoord(upperLeft, placementAnglePlus180, 1200);
		
		// Now place the fire position elements
		for (int columnIndex = 0; columnIndex < GRID_ELEMENTS; ++columnIndex)
		{
			for (int rowIndex = 0; rowIndex < GRID_ELEMENTS; ++rowIndex)
			{
				Coordinate elementPosition = MathUtils.calcNextCoord(upperLeft, placementAngle, (columnIndex * GRID_SIZE_METERS));
				elementPosition = MathUtils.calcNextCoord(elementPosition, placementAnglePlus90, (rowIndex * GRID_SIZE_METERS));
				gridElements[columnIndex][rowIndex].setMcuPosition(elementPosition);
			}
		}
	}	
	
	/**
	 * @param gridPosition
	 * @return
	 * @throws PWCGException 
	 * @throws PWCGDataValidityException 
	 */
	private double calcPlacementAngle(Coordinate gridPosition) throws PWCGException
	{
	    double placementAngle = 270;
	    	    
	    Side friendlySide = PWCGContextManager.getInstance().getCampaign().determineCountry().getSide();
	    
	    // Angle is calculated north to south.  We really want left to right.  
        // Left to right on the allied side (central campaign) is south to north.
        // Left to right on the central side (allied campaign) is south to north.
	    if (friendlySide == Side.AXIS)
	    {
	        placementAngle = MathUtils.adjustAngle(placementAngle, 180);
	    }
	    
	    // Reverse it it if the map is Russia
	    if (PWCGContextManager.getInstance().getCurrentMap().getMapIdentifier() == FrontMapIdentifier.GALICIA_MAP)
	    {
            placementAngle = MathUtils.adjustAngle(placementAngle, 180);
	    }
	    
	    return placementAngle;
	}
	

	/**
	 * @param writer
	 * @throws PWCGIOException 
	 */
	public void write(BufferedWriter writer) throws PWCGIOException 
	{
	    artillerySpotMasterTrigger.write(writer);
		artillerySpotMedia.write(writer);
		artySpotMapGrid.write(writer);
        artillerySpotActivateSet.write(writer);
        artillerySpotForceComplete.write(writer);
        artySpotDeactivate.write(writer);
		
        for (int columnIndex = 0; columnIndex < GRID_ELEMENTS; ++columnIndex)
        {
            for (int rowIndex = 0; rowIndex < GRID_ELEMENTS; ++rowIndex)
            {
                gridElements[columnIndex][rowIndex].write(writer);
            }
        }
	}

    /**
     * @return the gridElements
     */
    public ArtillerySpotGridElement[][] getGridElements()
    {
        return this.gridElements;
    }

    /**
     * @return the placementAngle
     */
    public double getPlacementAngle()
    {
        return this.placementAngle;
    }

    /**
     * @return the artySpotMapGrid
     */
    public ArtillerySpotMapGrid getArtySpotMapGrid()
    {
        return this.artySpotMapGrid;
    }

    /**
     * @return the artillerySpotMedia
     */
    public ArtillerySpotMedia getArtillerySpotMedia()
    {
        return this.artillerySpotMedia;
    }

    /**
     * @return the artillerySpotActivateSet
     */
    public ArtillerySpotActivateSet getArtillerySpotActivateSet()
    {
        return this.artillerySpotActivateSet;
    }

    /**
     * @return the artillerySpotMasterTrigger
     */
    public ArtillerySpotMasterTrigger getArtillerySpotMasterTrigger()
    {
        return this.artillerySpotMasterTrigger;
    }

    /**
     * @return the artillerySpotForceComplete
     */
    public ArtillerySpotForceComplete getArtillerySpotForceComplete()
    {
        return this.artillerySpotForceComplete;
    }
    
}
