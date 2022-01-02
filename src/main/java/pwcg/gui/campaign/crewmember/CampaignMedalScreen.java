package pwcg.gui.campaign.crewmember;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.medals.MedalText;
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

    private Campaign campaign;
    private CrewMember crewMember;
    private JTextArea medalTextArea;

    public CampaignMedalScreen(Campaign campaign, CrewMember crewMember)
    {
        super("");
        this.setLayout(new BorderLayout());

        this.campaign = campaign;
        this.crewMember = crewMember;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignMedalScreen);
        this.setImageFromName(imagePath);

        this.add(BorderLayout.WEST, makeNavigationPanel());
        this.add(BorderLayout.CENTER, makeCenterPanel());     
	}

    private JPanel makeCenterPanel() throws PWCGException
    {
        JPanel medalCenterPanel = new JPanel(new GridLayout(0,2));
        medalCenterPanel.setOpaque(false);
        medalCenterPanel.add(makeMedalBoxPanel());     
        medalCenterPanel.add(makePaperDollPanel());

        return medalCenterPanel;
    }

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        JPanel medalNavPanel = new JPanel(new BorderLayout());
        medalNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finishedButton = PWCGButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished admiring your medals", this);
        buttonPanel.add(finishedButton);

        medalNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return medalNavPanel;
    }

    public JPanel makeMedalBoxPanel() throws PWCGException 
    {   
        JPanel openMedalBoxPanel = new JPanel(new BorderLayout());
        openMedalBoxPanel.setOpaque(false);

        int numSpacers = calcNumSpacers();
        
        openMedalBoxPanel.add(SpacerPanelFactory.createVerticalSpacerPanel(numSpacers), BorderLayout.NORTH);
        openMedalBoxPanel.add(makeMedalBox(), BorderLayout.CENTER);
        makeMedalTextPanelPanel();

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
		CampaignCrewMemberMedalBox medalBoxPanel = new CampaignCrewMemberMedalBox(this, campaign, crewMember);
		medalBoxPanel.makePanels();

        JPanel crewMemberMedalBoxPanel = new JPanel(new BorderLayout());
        crewMemberMedalBoxPanel.setOpaque(false);
        crewMemberMedalBoxPanel.add(medalBoxPanel, BorderLayout.CENTER);
		return crewMemberMedalBoxPanel;
	}
    
    private JPanel makeMedalTextPanelPanel() throws PWCGException
    {
        List<Medal> crewMemberMedals = crewMember.getMedals();
        if (!crewMemberMedals.isEmpty())
        {
            return makeTextPanelPanel(crewMemberMedals.get(0));
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

        String medalText = MedalText.getTextForMedal(crewMember, medal);

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
        medalTextArea.setText(MedalText.getTextForMedal(crewMember, medal));
    }
    

    private JPanel makePaperDollPanel() throws PWCGException
    {
        CampaignPaperDollPanel paperDollPanel = new CampaignPaperDollPanel(campaign, crewMember);
        paperDollPanel.makePaperDollPanel();
        return paperDollPanel;
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
