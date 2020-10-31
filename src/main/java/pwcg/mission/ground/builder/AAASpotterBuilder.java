package pwcg.mission.ground.builder;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAASpotterBuilder
{    
    private static final int SPOTTER_RANGE = 20000;
    private IFlightInformation flightInformation;
    
    public AAASpotterBuilder (IFlightInformation flightInformation)
    {
        this.flightInformation  = flightInformation;
    }

    public IGroundUnitCollection createAAASpotterBattery (Coordinate position) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return null;
        }
        
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, position, flightInformation.getCountry());
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(flightInformation.getCampaign(), targetDefinition);
        
        IGroundUnitCollection spotterMG = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        if (spotterMG != null)
        {
            CountryDesignator countryDesignator = new CountryDesignator();
            ICountry ownerOfLocation = countryDesignator.determineCountry(position, flightInformation.getCampaign().getDate());
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
