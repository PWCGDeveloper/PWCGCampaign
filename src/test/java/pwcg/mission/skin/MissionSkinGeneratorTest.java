package pwcg.mission.skin;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

@ExtendWith(MockitoExtension.class)
public class MissionSkinGeneratorTest
{
    @Mock private Campaign campaign;
    @Mock private Squadron squadron;
    @Mock private IFlight flight;
    @Mock private FlightPlanes flightPlanes;
    @Mock private ICountry country;

    private PlaneMcu plane1;
    private List<PlaneMcu> planes = new ArrayList<>();

    public MissionSkinGeneratorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        plane1 = new PlaneMcu(campaign);
    }
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        Mockito.when(flight.getCampaign()).thenReturn(campaign);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420901"));

        Mockito.when(flight.getFlightPlanes()).thenReturn(flightPlanes);
        Mockito.when(flightPlanes.getPlanes()).thenReturn(planes);
        
        Mockito.when(flight.getSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronCountry(Mockito.any())).thenReturn(country);
    }

    @Test
    public void buildMissionSkinSetForSummerJu87() throws Exception
    {
        Mockito.when(squadron.getSquadronId()).thenReturn(20121002);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountryName()).thenReturn("Germany");
        
        planes.clear();
        plane1.setType("ju87d3");
        planes.add(plane1);

        MissionSkinSet missionSkinSet = MissionSkinGenerator.buildMissionSkinSet(flight);
        Assertions.assertTrue (missionSkinSet.getFactorySkins("ju87d3").size() > 0);
        Assertions.assertTrue (missionSkinSet.getSquadronSkins("ju87d3").size() > 0);
        Assertions.assertTrue (missionSkinSet.getSquadronPersonalSkins("ju87d3").size() > 0);
        Assertions.assertTrue (missionSkinSet.getNonSquadronPersonalSkin("ju87d3").size() > 0);
    }

    @Test
    public void buildMissionSkinSetForSummerIl2M42() throws Exception
    {
        Mockito.when(squadron.getSquadronId()).thenReturn(10121062);
        Mockito.when(squadron.getCountry()).thenReturn(country);
        Mockito.when(country.getCountryName()).thenReturn("Russia");
        
        planes.clear();
        plane1.setType("il2m42");
        planes.add(plane1);

        MissionSkinSet missionSkinSet = MissionSkinGenerator.buildMissionSkinSet(flight);
        Assertions.assertTrue (missionSkinSet.getFactorySkins("il2m42").size() > 0);
        Assertions.assertTrue (missionSkinSet.getSquadronSkins("il2m42").size() > 0);
        Assertions.assertTrue (missionSkinSet.getSquadronPersonalSkins("il2m42").size() > 0);
        Assertions.assertTrue (missionSkinSet.getNonSquadronPersonalSkin("il2m42").size() > 0);
    }

}
