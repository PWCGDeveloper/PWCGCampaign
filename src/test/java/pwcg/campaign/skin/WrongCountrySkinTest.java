package pwcg.campaign.skin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.testutils.SquadronTestProfile;
import pwcg.testutils.TestCampaignFactoryBuilder;
import pwcg.testutils.TestMissionBuilderUtility;

public class WrongCountrySkinTest
{
    @Test
    public void earlyBritishSkintest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(WrongCountrySkinTest.class.getCanonicalName(), SquadronTestProfile.RAF_BOB_PROFILE);
        campaign.setDateWithMapUpdate(DateUtils.getDateYYYYMMDD("19410901"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        boolean skinsAreCorrect = true;
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                if (plane.getType().contains("hurricane"))
                {
                    if (!plane.getSkin().getSkinName().toLowerCase().equals("hurricanemkii_blank_03"))
                    {
                        skinsAreCorrect = false;
                    }
                }
                
                if (plane.getType().contains("spitfire"))
                {
                    if (!plane.getSkin().getSkinName().toLowerCase().equals("spitfiremkvb_blank_01"))
                    {
                        skinsAreCorrect = false;
                    }
                }                
            }
        }

        Assertions.assertTrue(skinsAreCorrect);        
    }
    @Test
    public void midBritishSkintest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Campaign campaign = TestCampaignFactoryBuilder.makeCampaign(WrongCountrySkinTest.class.getCanonicalName(), SquadronTestProfile.RAF_BOB_PROFILE);
        campaign.setDateWithMapUpdate(DateUtils.getDateYYYYMMDD("19431001"));
        MissionGenerator missionGenerator = new MissionGenerator(campaign);
        Mission mission = missionGenerator.makeMission(TestMissionBuilderUtility.buildTestParticipatingHumans(campaign));
        mission.finalizeMission();

        boolean skinsAreCorrect = true;
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
            {
                if (plane.getType().contains("hurricane"))
                {
                    if (!plane.getSkin().getSkinName().toLowerCase().equals("hurricanemkii_blank_04#1"))
                    {
                        skinsAreCorrect = false;
                    }
                }
                
                if (plane.getType().contains("spitfire"))
                {
                    if (!plane.getSkin().getSkinName().toLowerCase().equals("spitfiremkvb_blank_02"))
                    {
                        skinsAreCorrect = false;
                    }
                }                
            }
        }

        Assertions.assertTrue(skinsAreCorrect);        
    }

}
