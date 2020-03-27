package pwcg.campaign.target;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.target.TargetType;
import pwcg.mission.target.TargetTypeAvailabilityInputs;
import pwcg.mission.target.TargetTypeGroundAttackGenerator;

@RunWith(MockitoJUnitRunner.class)
public class TargetTypeAttackGeneratorTest
{    
    @Mock private TargetTypeAvailabilityInputs targetTypeAvailabilityInputs;
    @Mock private Campaign campaign;
    @Mock private Squadron squadron;
    @Mock private ConfigManagerCampaign configManagerCampaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        Mockito.when(targetTypeAvailabilityInputs.getSide()).thenReturn(Side.AXIS);
        Mockito.when(targetTypeAvailabilityInputs.getTargetGeneralLocation()).thenReturn(new Coordinate(216336, 0, 184721));
        Mockito.when(targetTypeAvailabilityInputs.getPreferredDistance()).thenReturn(60000.0);
        Mockito.when(targetTypeAvailabilityInputs.getMaxDistance()).thenReturn(100000.0);  
        ICountry country = CountryFactory.makeCountryByCountry(Country.BRITAIN);
        Mockito.when(squadron.determineSquadronCountry(Matchers.any())).thenReturn(country); 
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManagerCampaign); 
        Mockito.when(configManagerCampaign.getIntConfigParam(Matchers.any())).thenReturn(10);
    }
    
    @Test
    public void kubanTargetDrifterAvailabilityTest() throws PWCGException
    {
        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(campaign.useMovingFrontInCampaign()).thenReturn(true);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
        testPlaceAndTarget(TargetType.TARGET_DRIFTER, true);
    }
    
    @Test
    public void kubanTargetShippingAvailabilityTest() throws PWCGException
    {
        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(campaign.useMovingFrontInCampaign()).thenReturn(true);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
        testPlaceAndTarget(TargetType.TARGET_SHIPPING, true);
    }
    
    @Test
    public void moscowNoTargetDrifterAvailabilityTest() throws PWCGException
    {
        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411001"));
        Mockito.when(campaign.useMovingFrontInCampaign()).thenReturn(true);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        testPlaceAndTarget(TargetType.TARGET_DRIFTER, false);
    }
    
    @Test
    public void moscowNoTargetShippingAvailabilityTest() throws PWCGException
    {
        Mockito.when(targetTypeAvailabilityInputs.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411001"));
        Mockito.when(campaign.useMovingFrontInCampaign()).thenReturn(true);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
        testPlaceAndTarget(TargetType.TARGET_SHIPPING, false);
    }

    private void testPlaceAndTarget(TargetType targetType, boolean assertion) throws PWCGException
    {
        TargetTypeGroundAttackGenerator targetTypeAttackGenerator = new TargetTypeGroundAttackGenerator(campaign, squadron, targetTypeAvailabilityInputs);        
        targetTypeAttackGenerator.formTargetPriorities();
        Map<TargetType, Integer> preferredTargetTypes = targetTypeAttackGenerator.getPreferredTargetTypes();
        assert(preferredTargetTypes.containsKey(targetType) == assertion);
    }

}
