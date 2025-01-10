package pwcg.mission.flight.artySpot;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleFactory;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuMissionStart;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotArtilleryGroup
{
	public static final int MAX_ARTILLERY_RANGE = 10000;
	public static final int NUM_ARTILLERY = 4;

	private IFlight artillerySpotFlight;
	private FlightInformation flightInformation;
    private McuMissionStart artilleryBegin = new McuMissionStart();
    private McuTimer artilleryTimer = new McuTimer();
    private McuActivate artilleryActivate = new McuActivate();
    private List<IVehicle> artillery = new ArrayList<>();

	public ArtillerySpotArtilleryGroup (IFlight artillerySpotFlight)
	{
		this.artillerySpotFlight = artillerySpotFlight;
		this.flightInformation = artillerySpotFlight.getFlightInformation();
	}
	
	public void build() throws PWCGException 
	{
		Coordinate targetPosition = artillerySpotFlight.getTargetDefinition().getPosition();
		
        FrontLinesForMap frontlines = PWCGContext.getInstance().getMap(flightInformation.getCampaignMap()).getFrontLinesForMap(
        		flightInformation.getCampaign().getDate());
        double angleFromTarget = frontlines.findClosestFriendlyPositionAngle(targetPosition, flightInformation.getCountry().getSide());
        
        createHowitzers(angleFromTarget);
        buildMcus(artillery.get(0).getPosition());
        linkMcus();
	}

    private void linkMcus() 
    {
    	artilleryBegin.setMissionBeginTarget(artilleryTimer.getIndex());
    	artilleryTimer.setTimerTarget(artilleryActivate.getIndex());
    	for (IVehicle howitzer : artillery)
    	{
    		artilleryActivate.setObject(howitzer.getLinkTrId());
    	}
	}
    
    private void buildMcus(Coordinate artilleryPosition)
    {
    	artilleryBegin.setName("ArtillerySpotArtilleryGroup Mission Begin");     
    	artilleryBegin.setDesc("ArtillerySpotArtilleryGroup Mission Begin");        
    	artilleryBegin.setPosition(artilleryPosition);

    	artilleryTimer.setName("ArtillerySpotArtilleryGroup Timer");     
    	artilleryTimer.setDesc("ArtillerySpotArtilleryGroup Timer");        
    	artilleryTimer.setPosition(artilleryPosition);

    	artilleryActivate.setName("ArtillerySpotArtilleryGroup Activate");     
    	artilleryActivate.setDesc("ArtillerySpotArtilleryGroup Activate");        
    	artilleryActivate.setPosition(artilleryPosition);
    }

	private void createHowitzers(double angleFromTarget) throws PWCGException 
    {
		
        List<Coordinate> howitzerPositions = createVehicleStartPositions(angleFromTarget);
        List<IVehicle> howitzers = VehicleFactory.createVehicles(
        		flightInformation.getCountry(), flightInformation.getCampaign().getDate(), 
        		VehicleClass.ArtilleryHowitzer,
        		NUM_ARTILLERY);
	    
        for (int i = 0; i < NUM_ARTILLERY; ++i)
        {
        	IVehicle howitzer = howitzers.get(i);
        	Coordinate howitzerPosition = howitzerPositions.get(i);
        	
            howitzer.setPosition(howitzerPosition);
            double angleToTarget = MathUtils.adjustAngle(angleFromTarget, 180);
            howitzer.setOrientation(new Orientation(angleToTarget));
            howitzer.setCountry(flightInformation.getCountry());
            howitzer.populateEntity();
            howitzer.getEntity().enableEntity();
            artillery.add(howitzer);
        }
    }

    private List<Coordinate> createVehicleStartPositions(double angleFromTarget) throws PWCGException 
    {
        List<Coordinate> gunPositions = new ArrayList<>();
        
		Coordinate targetPosition = artillerySpotFlight.getTargetDefinition().getPosition();
        Coordinate howitzerStartPosition = MathUtils.calcNextCoord(flightInformation.getCampaignMap(), targetPosition, angleFromTarget, 9000);
 
        double gunSpacing = 30.0;        
        double placementOrientation = MathUtils.adjustAngle (angleFromTarget, 90.0);        
        for (int i = 0; i < NUM_ARTILLERY; ++i)
        {   
            howitzerStartPosition = MathUtils.calcNextCoord(flightInformation.getCampaignMap(), howitzerStartPosition, placementOrientation, gunSpacing);
            gunPositions.add(howitzerStartPosition);
        }
        return gunPositions;
    }
 
    public List<Integer> getArtilleryIds() 
	{
		List<Integer> artilleryIds = new ArrayList<>();
		for (IVehicle howitzer :  artillery)
		{
			artilleryIds.add(howitzer.getLinkTrId());
		}
		return artilleryIds;
	}

	public void write(BufferedWriter writer) throws PWCGException {
		for (IVehicle howitzer :  artillery)
		{
			howitzer.write(writer);
		}
		artilleryBegin.write(writer);
		artilleryTimer.write(writer);
		artilleryActivate.write(writer);
	}
}

