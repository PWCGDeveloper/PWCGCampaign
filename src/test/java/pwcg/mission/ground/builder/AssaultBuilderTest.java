package pwcg.mission.ground.builder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionFlightBuilder;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.mcu.McuTREntity;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderGround;
import pwcg.mission.target.TargetType;

@RunWith(MockitoJUnitRunner.class)
public class AssaultBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private Squadron squadron;
    @Mock private ConfigManagerCampaign configManager;
    @Mock private MissionFlightBuilder missionFlightBuilder;
    @Mock private IFlight playerFlight;
    @Mock private ICountry country;
    @Mock private IFlightPlanes flightPlanes;
    @Mock private PlaneMcu playerPlane;
    @Mock private McuTREntity playerPlaneEntity;
        
    @Before
    public void setup() throws PWCGException
    {
        List<IFlight> playerFlights = new ArrayList<>();
        List<PlaneMcu> playerFlightPlanes = new ArrayList<>();
        
        playerFlights.add(playerFlight);
        playerFlightPlanes.add(playerPlane);
        
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getMissionFlightBuilder()).thenReturn(missionFlightBuilder);
        Mockito.when(missionFlightBuilder.getPlayerFlights()).thenReturn(playerFlights);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(playerFlight.getSquadron()).thenReturn(squadron);
        Mockito.when(country.getSide()).thenReturn(Side.AXIS);
        Mockito.when(playerFlight.getFlightPlanes()).thenReturn(flightPlanes);
        Mockito.when(flightPlanes.getPlanes()).thenReturn(playerFlightPlanes);
        Mockito.when(playerPlane.getEntity()).thenReturn(playerPlaneEntity);
        Mockito.when(playerPlaneEntity.getIndex()).thenReturn(100);

        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createLargeAssaultTest () throws PWCGException 
    {
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionBattle(
                CountryFactory.makeCountryByCountry(Country.GERMANY), 
                CountryFactory.makeCountryByCountry(Country.RUSSIA), 
                TargetType.TARGET_ASSAULT, new Coordinate (102000, 0, 100000), true);

        IGroundUnitCollection groundUnitGroup = AssaultBuilder.generateAssault(mission, targetDefinition);
        
        assert (groundUnitGroup.getGroundUnits().size() >= 10);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            if (groundUnit.getCountry().getCountry() == Country.GERMANY)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.Tank)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else
                {
                    throw new PWCGException("Unexpected unit type");
                }
            }
            else if (groundUnit.getCountry().getCountry() == Country.RUSSIA)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryAntiTank)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
                {
                    assert (groundUnit.getVehicles().size() >= 2);
                }
                else
                {
                    throw new PWCGException("Unexpected unit type");
                }
            }
            else
            {
                throw new PWCGException("Unit from unidentified nation in assault");
            }
        }
        groundUnitGroup.validate();
    }
}
