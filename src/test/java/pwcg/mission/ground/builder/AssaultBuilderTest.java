package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlights;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

@ExtendWith(MockitoExtension.class)
public class AssaultBuilderTest
{
    @Mock
    private Campaign campaign;
    @Mock
    private Mission mission;
    @Mock
    private Squadron squadron;
    @Mock
    private ConfigManagerCampaign configManager;
    @Mock
    private MissionFlights missionFlightBuilder;
    @Mock
    private IFlight playerFlight;
    @Mock
    private ICountry country;
    @Mock
    private FlightPlanes flightPlanes;
    @Mock
    private PlaneMcu playerPlane;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        List<IFlight> playerFlights = new ArrayList<>();
        List<PlaneMcu> playerFlightPlanes = new ArrayList<>();

        playerFlights.add(playerFlight);
        playerFlightPlanes.add(playerPlane);

        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getFlights()).thenReturn(missionFlightBuilder);
        Mockito.when(mission.getCampaignMap()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP);
        Mockito.when(missionFlightBuilder.getPlayerFlights()).thenReturn(playerFlights);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(playerFlight.getSquadron()).thenReturn(squadron);
        Mockito.when(country.getSide()).thenReturn(Side.AXIS);
        Mockito.when(playerFlight.getFlightPlanes()).thenReturn(flightPlanes);
        Mockito.when(flightPlanes.getPlanes()).thenReturn(playerFlightPlanes);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(campaign.getCampaignMap()).thenReturn(FrontMapIdentifier.STALINGRAD_MAP);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createLargeAssaultWithoutBattleTest() throws PWCGException
    {
        try (MockedStatic<RandomNumberGenerator> mocked = Mockito.mockStatic(RandomNumberGenerator.class))
        {
            mocked.when(() -> RandomNumberGenerator.getRandom(100)).thenReturn(49);

            Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
            createLargeAssaultTest();
            GroundUnitCollection groundUnitGroup = createLargeAssaultTest();
            validate(groundUnitGroup);
        }
    }

    @Test
    public void createLargeAssaultDuringBattleTest() throws PWCGException
    {
        try (MockedStatic<RandomNumberGenerator> mocked = Mockito.mockStatic(RandomNumberGenerator.class))
        {
            mocked.when(() -> RandomNumberGenerator.getRandom(100)).thenReturn(79);

            Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420901"));
            GroundUnitCollection groundUnitGroup = createLargeAssaultTest();
            validate(groundUnitGroup);
        }
    }

    @Test
    public void createLargeAssaultDuringBattleWithAggressorDefendingTest() throws PWCGException
    {
        try (MockedStatic<RandomNumberGenerator> mocked = Mockito.mockStatic(RandomNumberGenerator.class))
        {
            mocked.when(() -> RandomNumberGenerator.getRandom(100)).thenReturn(81);

            Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420901"));
            GroundUnitCollection groundUnitGroup = createLargeAssaultTest();
            validate(groundUnitGroup);
        }
    }

    public GroundUnitCollection createLargeAssaultTest() throws PWCGException
    {
        Coordinate assaultPosition = new Coordinate(150000, 0, 150000);
        GroundUnitCollection groundUnitGroup = AssaultBuilder.generateAssault(mission, assaultPosition);

        Assertions.assertTrue(groundUnitGroup.getGroundUnits().size() >= 10);
        groundUnitGroup.validate();
        return groundUnitGroup;
    }

    private void validate(GroundUnitCollection groundUnitGroup) throws PWCGException
    {
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryAntiTank)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.Tank)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.Infantry)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
            {
                Assertions.assertTrue(groundUnit.getVehicles().size() >= 2);
            }
            else
            {
                throw new PWCGException("Unexpected unit type: " + groundUnit.getVehicleClass());
            }
        }
    }
}
