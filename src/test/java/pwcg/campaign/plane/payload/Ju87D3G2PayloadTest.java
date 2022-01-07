package pwcg.campaign.plane.payload;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PlaneAttributeMapping;
import pwcg.campaign.plane.TankType;
import pwcg.campaign.plane.TankTypeFactory;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;
import pwcg.product.bos.plane.payload.PlanePayloadFactory;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    Company squadron;
    @Mock
    ConfigManagerCampaign configManagerCampaign;

    @BeforeEach
    public void setupTest() throws PWCGException
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

    @Test
    public void validateStukaPayloadBeforeCannons() throws PWCGException
    {
        PlanePayloadFactory bosPayloadFactory = new PlanePayloadFactory();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        TankType bosTankType = planeTypeFactory.createTankTypeByType(PlaneAttributeMapping.JU87_D3.getTankType());

        System.out.println(bosTankType.getType());

        IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
        Assertions.assertTrue (payload != null);

        Assertions.assertTrue (payload.getAvailablePayloadDesignations(flight).size() == 8);

        List<PlanePayloadElement> unexpectedElements = Arrays.asList(PlanePayloadElement.BK37_AP_GUNPOD, PlanePayloadElement.BK37_HE_GUNPOD);
        for (PlanePayloadElement unexpectedElement : unexpectedElements)
        {
            Boolean found = false;
            for (PlanePayloadDesignation payloadDesignation : payload.getAvailablePayloadDesignations(flight))
            {
                for (PlanePayloadElement element : payloadDesignation.getPayloadElements())
                {
                    if (unexpectedElement == element)
                    {
                        found = true;
                    }
                }
            }
            Assertions.assertTrue (!found);
        }
    }

    @Test
    public void validateStukaPayloadAfterCannons() throws PWCGException
    {
        PlanePayloadFactory bosPayloadFactory = new PlanePayloadFactory();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        TankType bosTankType = planeTypeFactory.createTankTypeByType(PlaneAttributeMapping.JU87_D3.getTankType());

        System.out.println(bosTankType.getType());

        Date date = DateUtils.getDateYYYYMMDD("19430503");

        IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), date);
        Assertions.assertTrue (payload != null);

        Assertions.assertTrue (payload.getAvailablePayloadDesignations(flight).size() == 10);
        
        List<PlanePayloadElement> expectedElements = Arrays.asList(PlanePayloadElement.BK37_AP_GUNPOD, PlanePayloadElement.BK37_HE_GUNPOD);
        for (PlanePayloadElement expectedElement : expectedElements)
        {
            Boolean found = false;
            for (PlanePayloadDesignation payloadDesignation : payload.getAvailablePayloadDesignations(flight))
            {
                for (PlanePayloadElement element : payloadDesignation.getPayloadElements())
                {
                    if (expectedElement == element)
                    {
                        found = true;
                    }
                }
            }
            Assertions.assertTrue (found);
        }
    }

    private IPlanePayload getPayloadGeneratorForDiveBomber() throws PWCGException
    {
        TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByType(PlaneAttributeMapping.JU87_D3.getTankType());
        IPlanePayloadFactory payloadFactory = PWCGContext.getInstance().getPlanePayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPayload(plane.getType(), campaign.getDate());
        return payloadGenerator;
    }

    private IPlanePayload getPayloadGeneratorForAttack() throws PWCGException
    {
        TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByType(PlaneAttributeMapping.JU87_D3.getTankType());
        IPlanePayloadFactory payloadFactory = PWCGContext.getInstance().getPlanePayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPayload(plane.getType(), campaign.getDate());
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
        Assertions.assertTrue (expectedPayloadSet.contains(payloadId));
    }
}
