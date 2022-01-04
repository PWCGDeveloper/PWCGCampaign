package pwcg.gui.campaign.activity;

import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.SquadronSummaryStatistics;
import pwcg.core.exception.PWCGException;

public class CampaignSquadronLogSummaryPageBuilder
{
    private Campaign campaign = null;
    private int logsForSquadronId = 0;

    public CampaignSquadronLogSummaryPageBuilder (Campaign campaign, int logsForSquadronId)
	{        
        this.campaign = campaign;
        this.logsForSquadronId = logsForSquadronId;
	}

    public Map<Integer, StringBuffer> buildSummaryPage() throws PWCGException
	{
        SquadronSummaryStatistics squadronSummaryStatistics = new SquadronSummaryStatistics(campaign, logsForSquadronId);
        squadronSummaryStatistics.calculateStatistics();
        
		Map<Integer, StringBuffer> squadronDetailPages = new TreeMap<Integer, StringBuffer>();

		StringBuffer summaryPageBuffer = new StringBuffer("\n\n");
        
        summaryPageBuffer.append("Squadron Members Killed: " + squadronSummaryStatistics.getNumKilled() + "\n");
        summaryPageBuffer.append("Squadron Members Captured: " + squadronSummaryStatistics.getNumCaptured() + "\n");
        summaryPageBuffer.append("Squadron Members Lost To Wounds: " + squadronSummaryStatistics.getNumMaimed() + "\n");
        summaryPageBuffer.append("Squadron Members Lost Total: " + squadronSummaryStatistics.getCrewMembersLostTotal() + "\n");

        summaryPageBuffer.append("Squadron Air To Air Victories: " + squadronSummaryStatistics.getNumAirToAirVictories() + "\n");
        summaryPageBuffer.append("Squadron Tank Kills: " + squadronSummaryStatistics.getNumTankKills() + "\n");
        summaryPageBuffer.append("Squadron Train Kills: " + squadronSummaryStatistics.getNumTrainKills() + "\n");
        summaryPageBuffer.append("Squadron Ground Kills: " + squadronSummaryStatistics.getNumGroundUnitKills() + "\n");
        summaryPageBuffer.append("Squadron Total; Ground Kills: " + squadronSummaryStatistics.getGroundKillsTotal() + "\n");

		squadronDetailPages.put(0, summaryPageBuffer);
		
        return squadronDetailPages;
	}
}
