package pwcg.campaign.plane.payload;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.TankType;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;
import pwcg.mission.target.TargetDefinition;

@ExtendWith(MockitoExtension.class)
public class WW2PayloadTest
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
    ICountry country;

    @Mock
    ConfigManagerCampaign configManagerCampaign;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        Mockito.when(flight.getTargetDefinition()).thenReturn(targetDefinition);
        Mockito.when(flight.getCompany()).thenReturn(squadron);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountry()).thenReturn(Country.RUSSIA);

        Mockito.when(flight.getCampaign()).thenReturn(campaign);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
    }

    @Test
    public void payloadTest() throws PWCGException
    {
        IPlanePayloadFactory payloadFactory = PWCGContext.getInstance().getPlanePayloadFactory();

        for (TankType planeType : PWCGContext.getInstance().getTankTypeFactory().getAllPlanes())
        {
            IPlanePayload payloadGenerator = payloadFactory.createPayload(planeType.getType(), campaign.getDate());
            testPatrolPayload(payloadGenerator);
            testInterceptPayload(payloadGenerator);
            testBombPayloads(payloadGenerator);
        }
    }

    private void testPatrolPayload(IPlanePayload payloadGenerator) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.PATROL);
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_NONE);
        runPayload(payloadGenerator);
    }

    private void testInterceptPayload(IPlanePayload payloadGenerator) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(FlightTypes.INTERCEPT);
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_NONE);
        runPayload(payloadGenerator);
    }

    private void testBombPayloads(IPlanePayload payloadGenerator) throws PWCGException
    {
        testBombPayload(payloadGenerator, FlightTypes.GROUND_ATTACK);

        testBombPayload(payloadGenerator, FlightTypes.DIVE_BOMB);

        testBombPayload(payloadGenerator, FlightTypes.BOMB);

        testBombPayload(payloadGenerator, FlightTypes.LOW_ALT_BOMB);
    }

    private void testBombPayload(IPlanePayload payloadGenerator, FlightTypes flightType) throws PWCGException
    {
        Mockito.when(flight.getFlightType()).thenReturn(flightType);
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_SOFT);
        runPayload(payloadGenerator);
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_ARMORED);
        runPayload(payloadGenerator);
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_MEDIUM);
        runPayload(payloadGenerator);
        Mockito.when(targetDefinition.getTargetCategory()).thenReturn(TargetCategory.TARGET_CATEGORY_HEAVY);
        runPayload(payloadGenerator);
    }

    private void runPayload(IPlanePayload payloadGenerator) throws PWCGException
    {
        int payloadId = payloadGenerator.createWeaponsPayload(flight);
        PlanePayloadDesignation payloadDesignation = payloadGenerator.getSelectedPayloadDesignation();
        Assertions.assertTrue (payloadDesignation.getPayloadId() == payloadId);
    }
}
