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
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPlanes;
import pwcg.mission.flight.plane.PlaneMcu;

@ExtendWith(MockitoExtension.class)
public class MissionSkinSetBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Company squadron;
    @Mock private IFlight flight;
    @Mock private IFlightPlanes flightPlanes;
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
        
        Mockito.when(flight.getCompany()).thenReturn(squadron);
        Mockito.when(squadron.determineSquadronCountry(Mockito.any())).thenReturn(country);
        
        planes.clear();
        planes.add(plane1);
        planes.add(plane2);
        planes.add(plane3);
    }
    
    @Test
    public void buildMissionSkinSetForSummer() throws Exception
    {
        Mockito.when(squadron.getCompanyId()).thenReturn(20111003);
        Mockito.when(country.getCountryName()).thenReturn("Germany");

        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f4");
        Mockito.when(plane3.getType()).thenReturn("bf109f4");
     
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));

        MissionSkinSet missionSkinSet = MissionSkinSetBuilder.buildSummerMissionSkinSet(flight);
        assert(missionSkinSet.getFactorySkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronSkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("bf109f4").size() > 0);
        assert(missionSkinSet.getNonSquadronPersonalSkin("bf109f4").size() > 0);
    }
        
    @Test
    public void buildMissionSkinSetForWinter() throws PWCGException
    {
        Mockito.when(squadron.getCompanyId()).thenReturn(10111011);
        Mockito.when(country.getCountryName()).thenReturn("Russia");

        Mockito.when(plane1.getType()).thenReturn("lagg3s29");
        Mockito.when(plane2.getType()).thenReturn("lagg3s29");
        Mockito.when(plane3.getType()).thenReturn("lagg3s29");
     
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420101"));

        MissionSkinSet missionSkinSet = MissionSkinSetBuilder.buildWinterMissionSkinSet(flight);
        assert(missionSkinSet.getFactorySkins("lagg3s29").size() > 0);
        assert(missionSkinSet.getSquadronSkins("lagg3s29").size() == 0);
        assert(missionSkinSet.getSquadronPersonalSkins("lagg3s29").size() > 0);
        assert(missionSkinSet.getNonSquadronPersonalSkin("lagg3s29").size() > 0);
    }
    
    @Test
    public void buildMissionSkinSetForDiffentTankTypesInFlight() throws PWCGException
    {
        Mockito.when(squadron.getCompanyId()).thenReturn(20111003);
        Mockito.when(country.getCountryName()).thenReturn("Germany");

        Mockito.when(plane1.getType()).thenReturn("bf109f4");
        Mockito.when(plane2.getType()).thenReturn("bf109f4");
        Mockito.when(plane3.getType()).thenReturn("bf109f2");
     
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420501"));

        MissionSkinSet missionSkinSet = MissionSkinSetBuilder.buildSummerMissionSkinSet(flight);
        assert(missionSkinSet.getFactorySkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronSkins("bf109f4").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("bf109f4").size() > 0);
        assert(missionSkinSet.getNonSquadronPersonalSkin("bf109f4").size() > 0);
        
        assert(missionSkinSet.getFactorySkins("bf109f2").size() > 0);
        assert(missionSkinSet.getSquadronSkins("bf109f2").size() > 0);
        assert(missionSkinSet.getSquadronPersonalSkins("bf109f2").size() > 0);
        assert(missionSkinSet.getNonSquadronPersonalSkin("bf109f2").size() > 0);
    }
}
