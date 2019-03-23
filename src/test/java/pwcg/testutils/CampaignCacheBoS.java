package pwcg.testutils;

import java.io.File;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignGenerator;
import pwcg.campaign.CampaignGeneratorModel;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;
import pwcg.mission.flight.FlightTypes;

public class CampaignCacheBoS extends CampaignCacheBase implements ICampaignCache
{
    public static final String WRITTEN = "Written";
    
    protected Campaign makeCampaignFromModel(CampaignGeneratorModel generatorModel) throws PWCGException
    {
        CampaignGenerator generator = new CampaignGenerator(generatorModel);
        Campaign campaign = generator.generate();          
        PWCGContextManager.getInstance().setCampaign(campaign);
        return campaign;
    }

    protected void initialize() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance();
        if (campaignProfiles.isEmpty())
        {
            loadCampaignProfiles();
        }
    }

    @Override
    protected void loadCampaignProfiles() throws PWCGException
    {
        makeProfile(SquadrontTestProfile.REGIMENT_503_PROFILE);
        makeProfile(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
        makeProfile(SquadrontTestProfile.JG_51_PROFILE_STALINGRAD);
        makeProfile(SquadrontTestProfile.JG_51_PROFILE_WEST);
        makeProfile(SquadrontTestProfile.KG53_PROFILE);
        makeProfile(SquadrontTestProfile.STG77_PROFILE);
        makeProfile(SquadrontTestProfile.TG2_PROFILE);
        makeProfile(SquadrontTestProfile.RAF_184_PROFILE);
        makeProfile(SquadrontTestProfile.REGIMENT_11_PROFILE);

        makeWrittenProfile();
    }
    
    private void makeWrittenProfile() throws PWCGException
    {
        String pwcgCampaignDir = PWCGContextManager.getInstance().getDirectoryManager().getPwcgCampaignsDir();
        String writtenDir = pwcgCampaignDir + TEST_CAMPAIGN_NAME;
        File writtenDirFile = new File(writtenDir);
        if (!writtenDirFile.exists())
        {
            Campaign writtenCampaign = makeCampaign(SquadrontTestProfile.JG_51_PROFILE_MOSCOW);
            writtenCampaign.write();
            
            MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        	for (SquadronMember player: writtenCampaign.getPersonnelManager().getAllPlayers().getSquadronMemberList())
        	{
        		participatingPlayers.addSquadronMember(player);
        	}

            Mission mission = new Mission();
            mission.initialize(writtenCampaign);
            mission.generate(participatingPlayers, FlightTypes.PATROL);
            mission.finalizeMission();
        }
    }
}
