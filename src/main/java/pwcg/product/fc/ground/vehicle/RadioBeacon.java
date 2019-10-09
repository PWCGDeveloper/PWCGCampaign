package pwcg.product.fc.ground.vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.constants.Callsign;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;

class RadioBeacon extends Vehicle implements IVehicle
{
    private int beaconChannel = 1;
    private Callsign callSign = Callsign.NONE;
    private int spotter = -1;
    private int coopStart = 0;

    private static final List<VehicleDefinition> germanRadioBeacon = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\ndb\\", "ndb", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishRadioBeacon = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\ndb\\", "ndb", Country.BRITAIN));
        }
    };

    public RadioBeacon() 
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanRadioBeacon);
        allvehicleDefinitions.addAll(britishRadioBeacon);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = britishRadioBeacon;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanRadioBeacon;
        }
        
        displayName = "Radio Beacon";
        makeRandomVehicleInstance(vehicleSet);
    }

    public void initialize(Flight flight) throws PWCGException 
    {
        double airfieldOrientation = flight.getAirfield().getTakeoffLocation().getOrientation().getyOri();

        Coordinate RadioBeaconCoordMoveLeft = moveWindSockDownAndLeftOfRunway(flight, airfieldOrientation);

        Coordinate RadioBeaconPos = MathUtils.calcNextCoord(RadioBeaconCoordMoveLeft, airfieldOrientation, 200.0);

        setPosition(RadioBeaconPos);
        setOrientation(flight.getAirfield().getOrientation().copy());
        
        entity.setEnabled(1);
    }

	private Coordinate moveWindSockDownAndLeftOfRunway(Flight flight, double airfieldOrientation)
	        throws PWCGException, PWCGException
	{
		Double angleRadioBeaconLeft = MathUtils.adjustAngle(airfieldOrientation, -90);
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();
        int RadioBeaconDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
        Coordinate RadioBeaconCoordMoveLeft = MathUtils.calcNextCoord(flight.getAirfield().getTakeoffLocation().getPosition().copy(), angleRadioBeaconLeft, RadioBeaconDistance);
		return RadioBeaconCoordMoveLeft;
	}
	
	@Override
    public void write(BufferedWriter writer) throws PWCGIOException
	{
        try
        {
            writer.write("Vehicle");
            writer.newLine();
            writer.write("{");
            writer.newLine();
            
            writeInternals(writer);

            writeRadioBeacon(writer);

            writer.write("}");
            writer.newLine();
            writer.newLine();
            writer.newLine();
            
            entity.write(writer);
        }
        catch (IOException e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	private void writeRadioBeacon(BufferedWriter writer) throws IOException
    {
        writer.write("  CoopStart = " + coopStart + ";");
        writer.newLine();
        writer.write("  Spotter = " + spotter + ";");
        writer.newLine();
        writer.write("  BeaconChannel = " + beaconChannel + ";");
        writer.newLine();
        writer.write("  Callsign = " + callSign.getNum() + ";");
        writer.newLine();
    }
}
