package pwcg.campaign.plane.payload;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.product.bos.plane.BosPlaneAttributeMapping;

@RunWith(MockitoJUnitRunner.class)
public class Ju87D3G2PayloadTest
{
    @Mock
    IFlight flight;
    @Mock
    FlightInformation flightInformation;
    @Mock
    TargetDefinition targetDefinition;
    @Mock
    Campaign campaign;
    @Mock
    Squadron squadron;
    @Mock
    ConfigManagerCampaign configManagerCampaign;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        Mockito.when(flight.getTargetDefinition()).thenReturn(targetDefinition);
    }

    @Test
    public void testDiveBombBeforeG2() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForDiveBomber();
        testDiveBombAndAttackBeforeG2(payloadGenerator);
    }

    @Test
    public void testDiveBombAfterG2() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForDiveBomber();
        testDiveBombAndAttackBeforeG2(payloadGenerator);
    }

    @Test
    public void testAttackMissionAttackSquadronBeforeG3() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForAttack();
        testDiveBombAndAttackBeforeG2(payloadGenerator);
    }

    @Test
    public void testAttackMissionAttackSquadronUsingG3() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForAttack();
        testAttackAfterG2(payloadGenerator);
    }

    private IPlanePayload getPayloadGeneratorForDiveBomber() throws PWCGException
    {
        PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.JU87_D3.getPlaneType());
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(plane.getType(), campaign.getDate());
        return payloadGenerator;
    }

    private IPlanePayload getPayloadGeneratorForAttack() throws PWCGException
    {
        PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.JU87_D3.getPlaneType());
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(plane.getType(), campaign.getDate());
        return payloadGenerator;
    }

    private void testDiveBombAndAttackBeforeG2(IPlanePayload payloadGenerator) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.DIVE_BOMB);

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(1));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(6));
    }

    private void testAttackAfterG2(IPlanePayload payloadGenerator) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(9));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(9));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(9));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(9));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(6));
    }

    private void runPayload(IPlanePayload payloadGenerator, List<Integer> expectedPayloadSet) throws PWCGException
    {
        int payloadId = payloadGenerator.createWeaponsPayload(flight);
        assert (expectedPayloadSet.contains(payloadId));
    }
}
