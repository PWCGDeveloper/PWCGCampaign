package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;

public class CampaignGeneratorProfileInfoGUI extends Pane
{
	private static final long serialVersionUID = 1L;

    private CampaignGeneratorScreen parent;

	public CampaignGeneratorProfileInfoGUI(CampaignGeneratorScreen parent, String imagePath) 
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
			Pane campaignProfileInfoPanel = new Pane(new BorderLayout());
			campaignProfileInfoPanel.setOpaque(false);
			
			Pane campaignProfileInfoGridPanel = createPorfileInfoPanel(); 
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

    private Pane createSpacingPanel() throws PWCGException
    {
        Pane spacingGridPanel = new Pane(new GridLayout(1, 1));
        spacingGridPanel.setOpaque(false);

        spacingGridPanel.add(makeLabel("                      ")); 

        return spacingGridPanel;
    }

    private Pane createPorfileInfoPanel() throws PWCGException
    {
        Pane campaignProfileInfoGridPanel = new Pane(new GridLayout(4, 1));
        campaignProfileInfoGridPanel.setOpaque(false);

        campaignProfileInfoGridPanel.add(makeLabel("     ")); 

        campaignProfileInfoGridPanel.add(makeLabel("Campaign Name: " + parent.getCampaignGeneratorDO().getCampaignName())); 
        
        campaignProfileInfoGridPanel.add(makeLabel("Campaign Mode: " + parent.getCampaignGeneratorDO().getCampaignMode().getCampaignModeName())); 

        campaignProfileInfoGridPanel.add(makeLabel("Service: " + parent.getCampaignGeneratorDO().getService().getName())); 

        return campaignProfileInfoGridPanel;
    }

	private Label makeLabel(String labelText) throws PWCGException
	{
	    Label label = new Label(labelText);
	    label.setOpaque(false);
	    label.setFont(PWCGMonitorFonts.getPrimaryFontLarge());
	    label.setForeground(ColorMap.CHALK_FOREGROUND);
        return label;
	}
}
