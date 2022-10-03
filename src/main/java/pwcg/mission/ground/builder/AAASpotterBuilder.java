package pwcg.mission.ground.builder;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.CountryDesignator;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

public class AAASpotterBuilder
{    
    private static final int SPOTTER_RANGE = 20000;
    
    public static GroundUnitCollection createAAASpotterBattery (Coordinate position, Campaign campaign, ICountry spotterCountry) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            return null;
        }
        
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, position, spotterCountry, "Spotter");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        
        GroundUnitCollection spotterMG = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_TINY);
        ICountry ownerOfLocation = CountryDesignator.determineCountry(position, campaign.getDate());
        if (ownerOfLocation.getCountry() == spotterCountry.getCountry())
        {
            SpotterDecorator.createSpotter(spotterMG, SPOTTER_RANGE);
        }
        
        return spotterMG;
    }
}
