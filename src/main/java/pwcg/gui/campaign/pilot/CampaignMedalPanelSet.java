package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignMedalPanelSet extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember pilot;

    public CampaignMedalPanelSet(SquadronMember pilot)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.pilot = pilot;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImageMain("TableTop.jpg");
        this.setImage(imagePath);

        this.add(BorderLayout.WEST, makenavigationPanel());
	    this.add(BorderLayout.CENTER, makeCenterPanel());
	    
        int percentForRightSpace = calcPercentForRightSpacer();
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeSpacerPercentPanel(percentForRightSpace));
	}

    private JPanel makenavigationPanel() throws PWCGException  
    {
        JPanel pilotDesktopNavPanel = new JPanel(new BorderLayout());
        pilotDesktopNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(finishedButton);

        pilotDesktopNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return pilotDesktopNavPanel;
    }

    public JPanel makeCenterPanel() throws PWCGException 
    {   
        JPanel openMedalBoxPanel = new JPanel(new BorderLayout());
        openMedalBoxPanel.setOpaque(false);

        int numSpacers = calcNumSpacers();
        
        openMedalBoxPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(numSpacers), BorderLayout.NORTH);
        openMedalBoxPanel.add(makeMedalBox(), BorderLayout.CENTER);
        openMedalBoxPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(numSpacers), BorderLayout.SOUTH);

        return openMedalBoxPanel;
    }   
    
    private int calcPercentForRightSpacer()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameWidth();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            return 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            return 6;
        }
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return 10;
        }
        else
        {
            return 20;            
        }
    }

    private int calcNumSpacers()
    {
        MonitorSize monitorSize = PWCGMonitorSupport.getFrameHeight();
        if (monitorSize == MonitorSize.FRAME_VERY_SMALL)
        {
            return 1;
        }
        else if (monitorSize == MonitorSize.FRAME_SMALL)
        {
            return 2;
        }
        if (monitorSize == MonitorSize.FRAME_MEDIUM)
        {
            return 3;
        }
        else
        {
            return 5;
        }
    }

	private JPanel makeMedalBox() throws PWCGException 
	{
		CampaignPilotMedalPanel medalBoxPanel = new CampaignPilotMedalPanel(pilot);
		medalBoxPanel.makePanels();

        JPanel pilotMedalBoxPanel = new JPanel(new BorderLayout());
        pilotMedalBoxPanel.setOpaque(false);
        pilotMedalBoxPanel.add(medalBoxPanel, BorderLayout.CENTER);
		return pilotMedalBoxPanel;
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			String action = ae.getActionCommand();
			
            if (action.startsWith("Finished"))
            {
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
			
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}
}
