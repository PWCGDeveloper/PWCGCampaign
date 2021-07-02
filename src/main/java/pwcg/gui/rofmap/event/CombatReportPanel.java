package pwcg.gui.rofmap.event;

import java.awt.BorderLayout;

import pwcg.campaign.Campaign;
import pwcg.campaign.CombatReport;
import pwcg.campaign.io.json.CombatReportIOJson;
import pwcg.core.exception.PWCGException;

public class CombatReportPanel extends AARDocumentIconPanel
{
	private static final long serialVersionUID = 1L;
	private CombatReport combatReport;

	public CombatReportPanel(CombatReport combatReport)
	{
		super();
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		this.combatReport = combatReport;
	}

    @Override
    protected String getHeaderText() throws PWCGException
    {
        return "Combats in the Air";
    }

    @Override
    protected String getFooterImagePath() throws PWCGException
    {
        return "";
    }

    @Override
    protected String getBodyText() throws PWCGException
    {
        String combatReportText = makeMissionResults();
        combatReportText += makeNarrative();
        return combatReportText;
    }

	private String makeMissionResults() throws PWCGException 
	{
		return "Remarks on flight and hostile aircraft\n\n" + combatReport.getHaReport();
	}

	private String makeNarrative() throws PWCGException 
	{
		return "\n\nNarrative\n" + combatReport.getNarrative();
	}

	public void setCombatReport(CombatReport combatReport) 
	{
		this.combatReport = combatReport;
	}

	public void writeCombatReport(Campaign campaign) throws PWCGException 
	{
		if (combatReport != null)
		{
			CombatReportIOJson.writeJson(campaign, combatReport);
		}
	}

    @Override
    public void finished()
    {        
    }
}
