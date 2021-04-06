package pwcg.mission.flight.factory;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.campaign.skirmish.SkirmishManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

public class SkirmishFlightTypeFactoryTest
{
    @Before
    public void fighterFlightTests() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void hasSkirmishAndParaDropTest() throws PWCGException
    {
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19440917"));
        verifyParaDropOnDate(DateUtils.getDateYYYYMMDD("19440918"));
    }

    private void verifyParaDropOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(date);
        
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign.getDate());

        SkirmishFlightTypeFactory skirmishFlightTypeFactory = new SkirmishFlightTypeFactory(campaign, skirmishesForDate.get(0), null);
        Squadron c47Squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(102012437);
        boolean isPlayerFlight = false;
        FlightTypes flightType = skirmishFlightTypeFactory.getFlightType(c47Squadron, isPlayerFlight);
        assert(flightType == FlightTypes.PARATROOP_DROP);
    }

    @Test
    public void hasSkirmishAndCargoDropTest() throws PWCGException
    {
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440920"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440925"));
        verifyCargoDropsOnDate(DateUtils.getDateYYYYMMDD("19440928"));
    }

    private void verifyCargoDropsOnDate(Date date) throws PWCGException
    {
        Campaign campaign = CampaignCache.makeCampaign(SquadronTestProfile.RAF_184_PROFILE);
        campaign.setDate(date);
        
        
        SkirmishManager skirmishManager = new SkirmishManager(FrontMapIdentifier.BODENPLATTE_MAP);
        skirmishManager.initialize();
        List<Skirmish> skirmishesForDate = skirmishManager.getSkirmishesForDate(campaign.getDate());

        SkirmishFlightTypeFactory skirmishFlightTypeFactory = new SkirmishFlightTypeFactory(campaign, skirmishesForDate.get(0), null);
        Squadron c47Squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(102012437);
        boolean isPlayerFlight = false;
        FlightTypes flightType = skirmishFlightTypeFactory.getFlightType(c47Squadron, isPlayerFlight);
        assert(flightType == FlightTypes.CARGO_DROP);
    }
}
