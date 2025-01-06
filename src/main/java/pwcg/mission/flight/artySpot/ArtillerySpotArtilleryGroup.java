package pwcg.mission.flight.artySpot;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitInformationFactory;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryBattery;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.target.TargetType;

public class ArtillerySpotArtilleryGroup
{
	public static final int MAX_ARTILLERY_RANGE = 10000;
	
	private IGroundUnit artilleryBattery;
	private FlightInformation flightInformation;
	private IFlight artillerySpotFlight;
	
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
        double angleToTarget = MathUtils.adjustAngle(angleFromTarget, 180);
        
        Coordinate artilleryPosition = MathUtils.calcNextCoord(flightInformation.getCampaignMap(), targetPosition, angleFromTarget, 15000);
        
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
        		flightInformation.getCampaign(), 
        		flightInformation.getCountry(),
                TargetType.TARGET_ARTILLERY, 
                artilleryPosition, 
                targetPosition, 
                new Orientation(angleToTarget));

        artilleryBattery = new GroundArtilleryBattery(groundUnitInformation);
        artilleryBattery.createGroundUnit();
	}

	public List<IVehicle> getVehicles() 
	{
		return artilleryBattery.getVehicles();
	}

    public int getLeadIndex() 
    {
        return artilleryBattery.getVehicles().get(0).getEntity().getIndex();
    }

	public void write(BufferedWriter writer) throws PWCGException {
		artilleryBattery.write(writer);		
	}
}

