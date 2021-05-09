package pwcg.mission.ground.builder;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAASpotterBuilder
{    
    private static final int SPOTTER_RANGE = 20000;
    private FlightInformation flightInformation;
    
    public AAASpotterBuilder (FlightInformation flightInformation)
    {
        this.flightInformation  = flightInformation;
    }

    public GroundUnitCollection createAAASpotterBattery (Coordinate position) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return null;
        }
        
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, position, flightInformation.getCountry(), "Spotter");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition);
        
        GroundUnitCollection spotterMG = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        if (spotterMG != null)
        {
            ICountry ownerOfLocation = CountryDesignator.determineCountry(position, flightInformation.getCampaign().getDate());
            if (ownerOfLocation.getCountry() == flightInformation.getCountry().getCountry())
            {
                IGroundUnit groundUnit = spotterMG.getGroundUnits().get(0);
                IVehicle vehicle = groundUnit.getVehicles().get(0);
                vehicle.setSpotterRange(SPOTTER_RANGE);
            }
        }

        return spotterMG;
    }
}
