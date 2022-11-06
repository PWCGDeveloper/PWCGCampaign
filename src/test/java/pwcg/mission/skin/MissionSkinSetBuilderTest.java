package pwcg.mission.skin;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightPlanes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;

@ExtendWith(MockitoExtension.class)
public class MissionSkinSetBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Squadron squadron;
    @Mock private IFlight flight;
    @Mock private FlightPlanes flightPlanes;
    @Mock private PlaneMcu plane1;
    @Mock private PlaneMcu plane2;
    @Mock private PlaneMcu plane3;
    @Mock private ICountry country;
    
    private List<PlaneMcu> planes = new ArrayList<>();
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        
        Mockito.when(flight.getCampaign()).thenReturn(campaign);

        Mockito.when(flight.getFlightPlanes()).thenReturn(flightPlanes);
        Mockito.when(flightPlanes.getPlanes()).thenReturn(planes);
        
        Mockito.when(flight.getSquadron()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronCountry(Mockito.any())).thenReturn(country);
        
        planes.clear();
        planes.add(plane1);
        planes.add(plane2);
        planes.add(plane3);
    }
    
    @Test
    public void buildMissionSkinSetForSummer() throws Exception
    {
        Mockito.when(flight.getSquadron()).thenReturn(squadron);
        Mockito.when(squadron.getSquadronId()).thenReturn(20111003);
        ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f4");
        Mockito.when(plane3.getType()).thenReturn("bf109f4");
     
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));

        MissionSkinSetBuilder skinSetBuilder = new MissionSkinSetBuilder(flight);
        MissionSkinSet missionSkinSet = skinSetBuilder.buildSummerMissionSkinSet();
        assert(missionSkinSet.getFactorySkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronSkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("bf109f4").size() > 0);
        //assert(missionSkinSet.getNonSquadronPersonalSkin("bf109f4").size() > 0);
    }
        
    @Test
    public void buildMissionSkinSetForWinter() throws PWCGException
    {
        Mockito.when(flight.getSquadron()).thenReturn(squadron);
        Mockito.when(squadron.getSquadronId()).thenReturn(10111011);
        ICountry country = CountryFactory.makeCountryByCountry(Country.RUSSIA);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(plane1.getType()).thenReturn("lagg3s29");
        Mockito.when(plane2.getType()).thenReturn("lagg3s29");
        Mockito.when(plane3.getType()).thenReturn("lagg3s29");
     
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));

        MissionSkinSetBuilder skinSetBuilder = new MissionSkinSetBuilder(flight);
        MissionSkinSet missionSkinSet = skinSetBuilder.buildWinterMissionSkinSet();
        assert(missionSkinSet.getFactorySkins("lagg3s29").size() > 0);
        assert(missionSkinSet.getSquadronSkins("lagg3s29").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("lagg3s29").size() > 0);
        //assert(missionSkinSet.getNonSquadronPersonalSkin("lagg3s29").size() > 0);
    }
    
    @Test
    public void buildMissionSkinSetForDiffentPlaneTypesInFlight() throws PWCGException
    {
        Mockito.when(flight.getSquadron()).thenReturn(squadron);
        Mockito.when(squadron.getSquadronId()).thenReturn(20111003);
        ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
        Mockito.when(squadron.getCountry()).thenReturn(country);

        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f4");
        Mockito.when(plane3.getType()).thenReturn("bf109f2");
     
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));

        MissionSkinSetBuilder skinSetBuilder = new MissionSkinSetBuilder(flight);
        MissionSkinSet missionSkinSet = skinSetBuilder.buildSummerMissionSkinSet();
        assert(missionSkinSet.getFactorySkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronSkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("bf109f4").size() > 0);
        //assert(missionSkinSet.getNonSquadronPersonalSkin("bf109f4").size() > 0);
        
        assert(missionSkinSet.getFactorySkins("bf109f2").size() > 0);
        assert(missionSkinSet.getSquadronSkins("bf109f2").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("bf109f2").size() > 0);
        //assert(missionSkinSet.getNonSquadronPersonalSkin("bf109f2").size() > 0);
    }
}
