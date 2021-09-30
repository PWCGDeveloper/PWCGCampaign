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
import pwcg.campaign.plane.PwcgRoleCategory;
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
public class FW190A5G3PayloadTest
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

        Mockito.when(flight.getSquadron()).thenReturn(squadron);
        Mockito.when(flight.getCampaign()).thenReturn(campaign);
        Mockito.when(flight.getTargetDefinition()).thenReturn(targetDefinition);
    }

    @Test
    public void testPatrolPayloadBeforeWingGuns() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(0));
    }

    @Test
    public void testPatrolPayloadAfterWingGuns() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(4));
    }

    @Test
    public void testInterceptPayloadBeforeWingGunsAndPods() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.INTERCEPT);
        runPayload(payloadGenerator, Arrays.asList(0));
    }

    @Test
    public void testInterceptPayloadAfterWingGunsAndPods() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.INTERCEPT);
        runPayload(payloadGenerator, Arrays.asList(4, 5));
    }

    @Test
    public void testAttackMissionFighterSquadronBeforeG3() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        testFw190A5WithOrdnance(payloadGenerator);
    }

    @Test
    public void testAttackMissionFighterSquadronAfterG3() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        testFw190A5WithOrdnance(payloadGenerator);
    }

    @Test
    public void testAttackMissionAttackSquadronBeforeG3() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430101"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForAttack();
        testFw190A5WithOrdnance(payloadGenerator);
    }

    @Test
    public void testAttackMissionAttackSquadronUsingG3() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430801"));
        IPlanePayload payloadGenerator = getPayloadGeneratorForAttack();
        testFw190G3WithOrdnance(payloadGenerator);
    }

    private IPlanePayload getPayloadGeneratorForFighter() throws PWCGException
    {
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.FIGHTER);
        PlaneType fw190A5 = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.FW190_A5.getPlaneType());
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(fw190A5.getType(), campaign.getDate());
        return payloadGenerator;
    }

    private IPlanePayload getPayloadGeneratorForAttack() throws PWCGException
    {
        Mockito.when(squadron.determineSquadronPrimaryRoleCategory(Mockito.any())).thenReturn(PwcgRoleCategory.ATTACK);
        PlaneType fw190A5 = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByType(BosPlaneAttributeMapping.FW190_A5.getPlaneType());
        IPayloadFactory payloadFactory = PWCGContext.getInstance().getPayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPlanePayload(fw190A5.getType(), campaign.getDate());
        return payloadGenerator;
    }

    private void testFw190A5WithOrdnance(IPlanePayload payloadGenerator) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(1));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(3));
    }

    private void testFw190G3WithOrdnance(IPlanePayload payloadGenerator) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        runPayload(payloadGenerator, Arrays.asList(6));
    }

    private void runPayload(IPlanePayload payloadGenerator, List<Integer> expectedPayloadSet) throws PWCGException
    {
        int payloadId = payloadGenerator.createWeaponsPayload(flight);
        assert (expectedPayloadSet.contains(payloadId));
    }
}
