package pwcg.mission.flight.factory;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestMissionBuilderUtility;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SkirmishFlightTypeFactoryTest
{
    private Campaign campaign;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
    }

    @Test
    public void hasSkirmishAndParaDropTest() throws PWCGException
    {
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19440917"));
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19440918"));
    }

    @Test
    public void hasSkirmishAndCargoDropTest() throws PWCGException
    {

        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440920"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440925"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440928"));
    }

    private void verifyParaDropOnDate(Date date) throws PWCGException
    {
        campaign.setDate(date);
        
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        SkirmishFlightTypeFactory skirmishFlightTypeFactory = new SkirmishFlightTypeFactory(campaign, skirmishesForDate.get(0), null);
        Company c47Squadron = PWCGContext.getInstance().getCompanyManager().getCompany(102012437);
        boolean isPlayerFlight = false;
        FlightTypes flightType = skirmishFlightTypeFactory.getFlightType(c47Squadron, isPlayerFlight, PwcgRole.ROLE_TRANSPORT);
        assert(flightType == FlightTypes.PARATROOP_DROP);
    }

    private void verifyCargoDropsOnDate(Date date) throws PWCGException
    {
        campaign.setDate(date);
        
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign, TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));

        SkirmishFlightTypeFactory skirmishFlightTypeFactory = new SkirmishFlightTypeFactory(campaign, skirmishesForDate.get(0), null);
        Company c47Squadron = PWCGContext.getInstance().getCompanyManager().getCompany(102012437);
        boolean isPlayerFlight = false;
        FlightTypes flightType = skirmishFlightTypeFactory.getFlightType(c47Squadron, isPlayerFlight, PwcgRole.ROLE_TRANSPORT);
        assert(flightType == FlightTypes.CARGO_DROP);
    }
}
