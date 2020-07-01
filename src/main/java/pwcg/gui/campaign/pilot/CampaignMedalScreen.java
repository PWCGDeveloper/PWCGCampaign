package pwcg.gui.campaign.pilot;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalText;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.dialogs.PWCGMonitorSupport.MonitorSize;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignMedalScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember pilot;
    private JTextArea medalTextArea;

    public CampaignMedalScreen(SquadronMember pilot)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.pilot = pilot;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignMedalScreen);
        this.setImage(imagePath);

        this.add(BorderLayout.WEST, makenavigationPanel());
	    this.add(BorderLayout.CENTER, makeCenterPanel());
	    
        this.add(BorderLayout.EAST, initializeTextPanelPanel());
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
		CampaignPilotMedalBox medalBoxPanel = new CampaignPilotMedalBox(this, pilot);
		medalBoxPanel.makePanels();

        JPanel pilotMedalBoxPanel = new JPanel(new BorderLayout());
        pilotMedalBoxPanel.setOpaque(false);
        pilotMedalBoxPanel.add(medalBoxPanel, BorderLayout.CENTER);
		return pilotMedalBoxPanel;
	}
    
    private JPanel initializeTextPanelPanel() throws PWCGException
    {
        List<Medal> pilotMedals = pilot.getMedals();
        if (!pilotMedals.isEmpty())
        {
            return makeTextPanelPanel(pilotMedals.get(0));
        }
        
        int percentForRightSpace = calcPercentForRightSpacer();
        return SpacerPanelFactory.makeSpacerPercentPanel(percentForRightSpace);
    }

	   
    private JPanel makeTextPanelPanel(Medal medal) throws PWCGException  
    {
        JPanel medalTextPanel = new JPanel(new BorderLayout());
        medalTextPanel.setOpaque(false);
       
        JPanel medalDescriptionPanel = formMedalTextPanel(medal);
        
        medalTextPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(4), BorderLayout.NORTH);
        medalTextPanel.add(medalDescriptionPanel, BorderLayout.CENTER);
        medalTextPanel.add(SpacerPanelFactory.makeSpacerPercentPanel(20), BorderLayout.NORTH);

        return medalTextPanel;
    }


    private JPanel formMedalTextPanel(Medal medal) throws PWCGException
    {
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        ImageResizingPanel medalTextPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        medalTextPanel.setBorder(PwcgBorderFactory.createDocumentBorderWithExtraSpaceFromTop());

        String medalText = MedalText.getTextForMedal(pilot, medal);

        medalTextArea = createMedalTextArea();
        medalTextArea.setText(medalText);
        medalTextPanel.add(medalTextArea, BorderLayout.WEST);

        return medalTextPanel;
    }

    private JTextArea createMedalTextArea() throws PWCGException
    {
        JTextArea medalTextArea;
        Font font = PWCGMonitorFonts.getTypewriterFont();
        medalTextArea = new JTextArea();
        medalTextArea.setFont(font);
        medalTextArea.setOpaque(false);
        medalTextArea.setLineWrap(true);
        medalTextArea.setWrapStyleWord(true);
        medalTextArea.setText("");
        medalTextArea.setPreferredSize(PwcgBorderFactory.createSideTextPreferredSize());

        return medalTextArea;
    }
    
    public void setMedalText(Medal medal)
    {
        medalTextArea.setText(MedalText.getTextForMedal(pilot, medal));
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
