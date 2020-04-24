package pwcg.aar.prelim;

import pwcg.aar.AARFactory;
import pwcg.aar.prelim.claims.AARClaimPanelData;
import pwcg.aar.prelim.claims.AARClaimPanelEventTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.Side;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class AARPhase0Preliminary
{
    private Campaign campaign;
    private AARPreliminaryData aarPreliminarytData = new AARPreliminaryData();

    public AARPhase0Preliminary(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public AARPreliminaryData createAARPreliminaryData() throws PWCGException
    {        
        readPwcgMissionData();        
        tabulateClaimPanelData();   
        determineActiveCampaignMembersInMission();
        return aarPreliminarytData;
    }

    private void readPwcgMissionData() throws PWCGException
    {        
    	AARMostRecentLogSetFinder mostRecentLogSetFinder = AARFactory.makeMostRecentLogSetFinder(campaign);
    	mostRecentLogSetFinder.determineMostRecentAARLogFileMissionDataSetForCampaign();
        if (!mostRecentLogSetFinder.isLogSetComplete())
        {
        	throw new PWCGException ("Failed to find most recent log file data set");
        }

        aarPreliminarytData.setMissionLogFileSet(mostRecentLogSetFinder.getAarLogFileMissionFile());
        aarPreliminarytData.setPwcgMissionData(mostRecentLogSetFinder.getPwcgMissionData());
    }
    
    private void determineActiveCampaignMembersInMission() throws PWCGException
    {
        CampaignMembersInMissionFinder campaignMembersInMissionHandler = new CampaignMembersInMissionFinder();
        SquadronMembers campaignMembersInMission = campaignMembersInMissionHandler.determineCampaignMembersInMission(campaign, aarPreliminarytData.getPwcgMissionData());
        aarPreliminarytData.setCampaignMembersInMission(campaignMembersInMission);
    }

    private void tabulateClaimPanelData() throws PWCGException
    {
        if (campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE ||
            campaign.getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_COOP)
        {
            SquadronMember singlePlayer = campaign.findReferencePlayer();
            Side side = singlePlayer.determineSquadron().determineSquadronCountry(campaign.getDate()).getSide();
            AARClaimPanelEventTabulator claimPanelEventTabulator = new AARClaimPanelEventTabulator(campaign, aarPreliminarytData, side);
            AARClaimPanelData claimPanelData = claimPanelEventTabulator.tabulateForAARClaimPanel();
            aarPreliminarytData.setClaimPanelData(claimPanelData);
        }
    }
}
