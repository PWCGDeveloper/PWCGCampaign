package pwcg.mission.flight.artySpot.grid;

import java.io.BufferedWriter;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;


public class ArtillerySpotGrid 
{
	public static final int GRID_ELEMENTS = 8;
	public static final int GRID_SIZE_METERS = 200;
	
	private ArtillerySpotGridElement[][] gridElements = new ArtillerySpotGridElement[GRID_ELEMENTS][GRID_ELEMENTS];
	
	private ArtillerySpotMapGrid artySpotMapGrid;
	private ArtillerySpotMedia artillerySpotMedia = null;
    private ArtillerySpotActivateSet artillerySpotActivateSet= null;
    private ArtillerySpotMasterTrigger artillerySpotMasterTrigger = null;
    private ArtillerySpotDeactivate artySpotDeactivate = null;
    private ArtillerySpotForceComplete artillerySpotForceComplete = null;

    private double placementAngle = 270;
    private FlightInformation flightInformation;
    private Campaign campaign;

    public ArtillerySpotGrid (FlightInformation flightInformation)
    {
    	this.flightInformation = flightInformation;
    	this.campaign = flightInformation.getCampaign();
    	this.artySpotMapGrid = new ArtillerySpotMapGrid(campaign);
    }

	public void create (ArtillerySpotArtilleryGroup friendlyArtillery, Coordinate gridPosition) throws PWCGException 
	{
	    placementAngle = calcGridPlacementAngle(gridPosition);
		
        // Forces artillery to complete firing task
        artillerySpotForceComplete = new ArtillerySpotForceComplete(campaign);
        artillerySpotForceComplete.create(gridPosition, friendlyArtillery);

		artillerySpotActivateSet = new ArtillerySpotActivateSet(campaign);
		artillerySpotActivateSet.createActivates();

		// The artillery spot master trigger
		// Triggers every row element timer
		artillerySpotMasterTrigger = new ArtillerySpotMasterTrigger();
		artillerySpotMasterTrigger.create(gridPosition);
		
		// The deactivate
		artySpotDeactivate = new ArtillerySpotDeactivate(campaign);
		artySpotDeactivate.create(gridPosition);
        
		// Create the media
		artillerySpotMedia = new ArtillerySpotMedia(campaign);
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

	private void setMcuPosition(Coordinate gridPosition) throws PWCGException 
	{
        double placementAnglePlus90 = MathUtils.adjustAngle(placementAngle, 90);
        double placementAnglePlus180 = MathUtils.adjustAngle(placementAngle, 180);
        double placementAnglePlus270 = MathUtils.adjustAngle(placementAngle, 270);
		
		Coordinate upperLeft = MathUtils.calcNextCoord(campaign.getCampaignMap(), gridPosition, placementAnglePlus270, 1200);
		upperLeft = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAnglePlus180, 1200);
		
		// Now place the fire position elements
		for (int columnIndex = 0; columnIndex < GRID_ELEMENTS; ++columnIndex)
		{
			for (int rowIndex = 0; rowIndex < GRID_ELEMENTS; ++rowIndex)
			{
				Coordinate elementPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), upperLeft, placementAngle, (columnIndex * GRID_SIZE_METERS));
				elementPosition = MathUtils.calcNextCoord(campaign.getCampaignMap(), elementPosition, placementAnglePlus90, (rowIndex * GRID_SIZE_METERS));
				gridElements[columnIndex][rowIndex].setMcuPosition(elementPosition);
			}
		}
	}	

	private double calcGridPlacementAngle(Coordinate gridPosition) throws PWCGException
	{
        FrontLinesForMap frontLinesForMap = PWCGContext.getInstance().getMapByMapId(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        double placementAngle = frontLinesForMap.findClosestEnemyPositionAngle(gridPosition, flightInformation.getCountry().getSide());
	    return placementAngle;
	}

	public void write(BufferedWriter writer) throws PWCGException 
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

    public ArtillerySpotGridElement[][] getGridElements()
    {
        return this.gridElements;
    }

    public double getPlacementAngle()
    {
        return this.placementAngle;
    }

    public ArtillerySpotMapGrid getArtySpotMapGrid()
    {
        return this.artySpotMapGrid;
    }

    public ArtillerySpotMedia getArtillerySpotMedia()
    {
        return this.artillerySpotMedia;
    }

    public ArtillerySpotActivateSet getArtillerySpotActivateSet()
    {
        return this.artillerySpotActivateSet;
    }

    public ArtillerySpotMasterTrigger getArtillerySpotMasterTrigger()
    {
        return this.artillerySpotMasterTrigger;
    }

    public ArtillerySpotForceComplete getArtillerySpotForceComplete()
    {
        return this.artillerySpotForceComplete;
    }
    
}
