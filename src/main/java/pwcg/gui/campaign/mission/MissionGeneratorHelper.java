package pwcg.gui.campaign.mission;

import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.campaign.home.GuiMissionInitiator;
import pwcg.gui.rofmap.brief.BriefingDescriptionScreen;
import pwcg.gui.rofmap.brief.CampaignHomeGuiBriefingWrapper;
import pwcg.gui.sound.MusicManager;
import pwcg.gui.sound.SoundManager;
import pwcg.mission.Mission;
import pwcg.mission.MissionHumanParticipants;

public class MissionGeneratorHelper
{

    public static void showBriefingMap(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper,
            MissionHumanParticipants participatingPlayers, Map<Integer, PwcgRole> squadronRoleOverride) throws PWCGException
    {
        MusicManager.playMissionBriefingTheme();
        SoundManager.getInstance().playSound("Typewriter.WAV");

        GuiMissionInitiator missionInitiator = new GuiMissionInitiator(campaign, participatingPlayers);
        Mission mission = missionInitiator.makeMission(squadronRoleOverride);

        BriefingDescriptionScreen briefingMap = new BriefingDescriptionScreen(campaignHomeGuiBriefingWrapper, mission);
        briefingMap.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(briefingMap);
    }

    public static void scrubMission(Campaign campaign, CampaignHomeGuiBriefingWrapper campaignHomeGuiBriefingWrapper) throws PWCGException
    {
        campaign.setCurrentMission(null);
        campaignHomeGuiBriefingWrapper.refreshCampaignPage();
        CampaignGuiContextManager.getInstance().backToCampaignHome();
    }

    static MissionHumanParticipants buildParticipatingPlayersSinglePlayer(Campaign campaign) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        CrewMember referencePlayer = campaign.findReferencePlayer();
        participatingPlayers.addCrewMember(referencePlayer);
        return participatingPlayers;
    }
}
