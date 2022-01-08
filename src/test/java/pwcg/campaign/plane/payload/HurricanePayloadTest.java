package pwcg.campaign.plane.payload;

import java.util.Arrays;
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
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
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
public class HurricanePayloadTest
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

        Mockito.when(flight.getCompany()).thenReturn(squadron);
        Mockito.when(flight.getTargetDefinition()).thenReturn(targetDefinition);
    }

    @Test
    public void testVVSPatrolPayloadBeforeShvak() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411001"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(0));
    }

    @Test
    public void testVVSPatrolPayloadAfterShvak() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420420"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(0, 17));
    }

    @Test
    public void testVVSPatrolPayloadAfterHispano() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430103"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(17, 12));
    }

    @Test
    public void testRAFPatrolPayloadBeforeHispano() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19400101"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(0));
    }

    @Test
    public void testRAFPatrolPayloadAfterHispano() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19410601"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        runPayload(payloadGenerator, Arrays.asList(12));
    }

    @Test
    public void testAttackMissionVVSBeforeShvak() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19411001"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(4));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(4));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(4));
    }

    @Test
    public void testAttackMissionVVSAfterShvak() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420420"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(2, 18));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(4, 19));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(2, 23));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(4, 18));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(4, 18));
    }

    @Test
    public void testAttackMissionVVSAfterHispano() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430103"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(13, 18));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(14, 19));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(13, 23));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(14, 18));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(14, 18));
    }

    @Test
    public void testAttackMissionRAFBeforeHispano() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19400801"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(4));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(2));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(4));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(4));
    }


    @Test
    public void testAttackMissionRAFAfterHispano() throws PWCGException
    {
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19410601"));
        Mockito.when(squadron.getCountry()).thenReturn(CountryFactory.makeCountryByCountry(Country.BRITAIN));
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        IPlanePayload payloadGenerator = getPayloadGeneratorForFighter();

        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator, Arrays.asList(13));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator, Arrays.asList(14, 15));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator, Arrays.asList(13));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator, Arrays.asList(14));
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_STRUCTURE);
        runPayload(payloadGenerator, Arrays.asList(4));
    }

    @Test
    public void validateHurricaneModsEarly() throws PWCGException 
    {
        PlanePayloadFactory bosPayloadFactory = new PlanePayloadFactory();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        TankType bosTankType = planeTypeFactory.createTankTypeByType(PlaneAttributeMapping.HURRICANE_MKII.getTankType());
            
        System.out.println(bosTankType.getType());
            
        IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19420801"));
        assert(payload != null);
            
        assert(payload.getSelectedModifications().size() == 1);
        List<PlanePayloadElement> expectedElements = Arrays.asList(PlanePayloadElement.MIRROR);
        for (PlanePayloadElement element : payload.getSelectedModifications())
        {
            verifyExpectedModification(element, expectedElements);
        }
    }

    @Test
    public void validateHurricaneModsLate() throws PWCGException 
    {
        PlanePayloadFactory bosPayloadFactory = new PlanePayloadFactory();
        TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();

        TankType bosTankType = planeTypeFactory.createTankTypeByType(PlaneAttributeMapping.HURRICANE_MKII.getTankType());
            
        System.out.println(bosTankType.getType());
            
        IPlanePayload payload = bosPayloadFactory.createPayload(bosTankType.getType(), DateUtils.getDateYYYYMMDD("19430103"));
        assert(payload != null);
            
        assert(payload.getSelectedModifications().size() == 2);
        List<PlanePayloadElement> expectedElements = Arrays.asList(PlanePayloadElement.MIRROR, PlanePayloadElement.LB_14_BOOST);
        for (PlanePayloadElement element : payload.getSelectedModifications())
        {
            verifyExpectedModification(element, expectedElements);
        }
    }
    
    private void verifyExpectedModification(PlanePayloadElement element, List<PlanePayloadElement> expectedElements)
    {
        assert(expectedElements.contains(element));
    }
    
    private IPlanePayload getPayloadGeneratorForFighter() throws PWCGException
    {
        TankType fw190A5 = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByType(PlaneAttributeMapping.HURRICANE_MKII.getTankType());
        IPlanePayloadFactory payloadFactory = PWCGContext.getInstance().getPlanePayloadFactory();
        IPlanePayload payloadGenerator = payloadFactory.createPayload(fw190A5.getType(), campaign.getDate());
        return payloadGenerator;
    }

    private void runPayload(IPlanePayload payloadGenerator, List<Integer> expectedPayloadSet) throws PWCGException
    {
        int payloadId = payloadGenerator.createWeaponsPayload(flight);
        Assertions.assertTrue (expectedPayloadSet.contains(payloadId));
    }
}
