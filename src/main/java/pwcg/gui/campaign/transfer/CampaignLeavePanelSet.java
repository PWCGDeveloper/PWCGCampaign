package pwcg.gui.campaign.transfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.aar.ui.events.model.LeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.UiImageResolver;
import pwcg.gui.campaign.home.CampaignHome;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignLeavePanelSet extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private CampaignHome parent = null;
    private Campaign campaign = null;
	private JTextField tLeaveTime;

	public CampaignLeavePanelSet  (CampaignHome parent)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

		this.parent = parent;
        this.campaign = PWCGContext.getInstance().getCampaign();
	}
	
	public void makeVisible(boolean visible) 
	{
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImageMain("CampaignTable.jpg");
        this.setImage(imagePath);

	    this.add(BorderLayout.CENTER, makeLeaveCenterPanel());
	    this.add(BorderLayout.WEST, makeLeaveLeftPanel());
	}

	private JPanel makeLeaveLeftPanel() throws PWCGException  
	{
		JPanel leaverPanel = new JPanel(new BorderLayout());
		leaverPanel.setOpaque(false);

		JPanel leaveButtonPanel = new JPanel(new GridLayout(0,1));
		leaveButtonPanel.setOpaque(false);
		
        JButton acceptButton = PWCGButtonFactory.makeMenuButton("Accept Leave", "Accept Leave", this);
        leaveButtonPanel.add(acceptButton);

        JButton rejectButton = PWCGButtonFactory.makeMenuButton("Reject Leave", "Reject Leave", this);
        leaveButtonPanel.add(rejectButton);
		
		leaverPanel.add(leaveButtonPanel, BorderLayout.NORTH);
		
		return leaverPanel;
	}

	private JPanel makeLeaveCenterPanel() throws PWCGException  
	{
        JPanel leaveCenterPanel = new JPanel();
        leaveCenterPanel.setOpaque(false);
        leaveCenterPanel.setLayout(new BoxLayout(leaveCenterPanel, BoxLayout.PAGE_AXIS));
        leaveCenterPanel.setBorder(BorderFactory.createEmptyBorder(100,300,100,300));

        JPanel leaveNotification = makeLeaveLetterPanel();
        leaveCenterPanel.add(leaveNotification);

		return leaveCenterPanel;
	}

    private JPanel makeLeaveLetterPanel() throws PWCGException  
    {
        ImageResizingPanel leaveLetterPanel = null;
        String imagePath = UiImageResolver.getImageMain("document.png");
        leaveLetterPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);            
        leaveLetterPanel.setLayout(new BoxLayout(leaveLetterPanel, BoxLayout.PAGE_AXIS));
        leaveLetterPanel.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        JPanel leavePanel = new JPanel (new GridLayout(0, 1));
        leavePanel.setOpaque(false);

        makeBlankRows(leavePanel, 2);

        JPanel leavePlayerWoundInfoPanel = makePlayerWoundHealTimePanel();
        leavePanel.add(leavePlayerWoundInfoPanel);
        
        JPanel leaveRequestPanel = makeLeaveRequestRow();
        leavePanel.add(leaveRequestPanel);

        makeBlankRows(leavePanel, 13);

        leaveLetterPanel.add(leavePanel, BorderLayout.WEST);
        
        return leaveLetterPanel;
    }

    private JPanel makePlayerWoundHealTimePanel() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();

        JPanel leavePlayerWoundInfoPanel = new JPanel(new GridLayout(0, 1));
        leavePlayerWoundInfoPanel.setOpaque(false);
        for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            if (player.getRecoveryDate() != null)
            {
                int daysToHeal = DateUtils.daysDifference(campaign.getDate(), player.getRecoveryDate()) + 1;
                String playerWoundHealTimeDesc = player.getNameAndRank() + " requires " + daysToHeal + " to recover from his wounds";
                JLabel playerWoundHealTimeLabel = new JLabel (playerWoundHealTimeDesc, JLabel.LEFT);
                playerWoundHealTimeLabel.setFont(font);
                leavePlayerWoundInfoPanel.add(playerWoundHealTimeLabel);
            }
        }
       return leavePlayerWoundInfoPanel;
    }

    private JPanel makeLeaveRequestRow() throws PWCGException
    {
        Font font = PWCGMonitorFonts.getPrimaryFontLarge();
        Color buttonBG = ColorMap.PAPER_BACKGROUND;

        JLabel lLeave = new JLabel("Request Leave Time (days): ", JLabel.LEFT);
        lLeave.setOpaque(false);
        lLeave.setFont(font);

        tLeaveTime = new JTextField(5);
        tLeaveTime.setBackground(buttonBG);
        tLeaveTime.setOpaque(false);
        tLeaveTime.setFont(font);
        
        JPanel leaveRequestPanel = new JPanel (new GridLayout(1, 0));
        leaveRequestPanel.setOpaque(false);
        leaveRequestPanel.add(lLeave);
        leaveRequestPanel.add(tLeaveTime);
        
        return leaveRequestPanel;
    }

    private void makeBlankRows(JPanel leavePanel, int numDummyRows) throws PWCGException
    {
        for (int i = 0; i < numDummyRows; ++i)
        {
            JLabel lDummy1 = new JLabel("     ");
            leavePanel.add(lDummy1);
         }
    }

    private void makeBlankHorizonalSpace(JPanel leavePanel, int numSpaces) throws PWCGException
    {
        String space = " ";
        for (int i = 0; i < numSpaces; ++i)
        {
            space += " ";
        }
        
        JLabel lDummy1 = new JLabel(space);
        leavePanel.add(lDummy1);
    }




    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        try
        {
            if (action.equalsIgnoreCase("Accept Leave"))
            {
                pilotLeave();
            }
            else if (action.equalsIgnoreCase("Reject Leave"))
            {
                SoundManager.getInstance().playSound("Stapling.WAV");
                CampaignGuiContextManager.getInstance().popFromContextStack();
            }
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void pilotLeave() throws PWCGUserException, Exception
    {
        SoundManager.getInstance().playSound("Stapling.WAV");

        int leaveTimeDays = getLeaveTime();
        boolean isNewsWorthy = false;
        SquadronMember referencePlayer = campaign.findReferencePlayer();
        LeaveEvent leaveEvent = new LeaveEvent(campaign, leaveTimeDays, referencePlayer.getSquadronId(), referencePlayer.getSerialNumber(), campaign.getDate(), isNewsWorthy);
        parent.campaignTimePassedForLeave(leaveEvent.getLeaveTime());
    }

	public int getLeaveTime()  throws PWCGUserException, Exception
	{
		if (tLeaveTime.getText() == null || tLeaveTime.getText().length() == 0)
		{
			throw new PWCGUserException ("Enter leave in weeks continuing");
		}
		
		int leaveTime = Integer.valueOf(tLeaveTime.getText()).intValue();
		
		return leaveTime;
	}
}
