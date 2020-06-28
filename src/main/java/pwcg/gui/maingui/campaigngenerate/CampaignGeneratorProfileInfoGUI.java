package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;

public class CampaignGeneratorProfileInfoGUI extends JPanel
{
	private static final long serialVersionUID = 1L;

    private CampaignGeneratorPanelSet parent;

	public CampaignGeneratorProfileInfoGUI(CampaignGeneratorPanelSet parent, String imagePath) 
	{
        super();
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        
        this.parent = parent;       
	}
	

	public void makePanels() throws PWCGException 
	{
		try
		{			
			JPanel campaignProfileInfoPanel = new JPanel(new BorderLayout());
			campaignProfileInfoPanel.setOpaque(false);
			
			JPanel campaignProfileInfoGridPanel = createPorfileInfoPanel(); 
			campaignProfileInfoPanel.add(campaignProfileInfoGridPanel, BorderLayout.NORTH);

            this.add(createSpacingPanel(), BorderLayout.WEST);
			this.add(campaignProfileInfoPanel, BorderLayout.CENTER);
            this.add(createSpacingPanel(), BorderLayout.EAST);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private JPanel createSpacingPanel() throws PWCGException
    {
        JPanel spacingGridPanel = new JPanel(new GridLayout(1, 1));
        spacingGridPanel.setOpaque(false);

        spacingGridPanel.add(makeLabel("                      ")); 

        return spacingGridPanel;
    }

    private JPanel createPorfileInfoPanel() throws PWCGException
    {
        JPanel campaignProfileInfoGridPanel = new JPanel(new GridLayout(4, 1));
        campaignProfileInfoGridPanel.setOpaque(false);

        campaignProfileInfoGridPanel.add(makeLabel("     ")); 

        campaignProfileInfoGridPanel.add(makeLabel("Campaign Name: " + parent.getCampaignGeneratorDO().getCampaignName())); 
        
        campaignProfileInfoGridPanel.add(makeLabel("Campaign Mode: " + parent.getCampaignGeneratorDO().getCampaignMode().getCampaignModeName())); 

        campaignProfileInfoGridPanel.add(makeLabel("Service: " + parent.getCampaignGeneratorDO().getService().getName())); 

        return campaignProfileInfoGridPanel;
    }

	private JLabel makeLabel(String labelText) throws PWCGException
	{
	    JLabel label = new JLabel(labelText);
	    label.setOpaque(false);
	    label.setFont(PWCGMonitorFonts.getPrimaryFontLarge());
	    label.setForeground(ColorMap.CHALK_FOREGROUND);
        return label;
	}
}
