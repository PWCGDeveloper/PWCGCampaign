package pwcg.gui.campaign.transfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pwcg.aar.ui.events.model.LeaveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.PwcgGuiContext;
import pwcg.gui.campaign.home.CampaignHomeGUI;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.rofmap.event.AARMainPanel.EventPanelReason;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class CampaignLeavePanelSet extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

	private CampaignHomeGUI parent = null;
	private Campaign campaign = null;
	private JTextField tLeaveTime;

	public CampaignLeavePanelSet  (CampaignHomeGUI parent)
	{
        super();

		this.parent = parent;
		this.campaign =     PWCGContextManager.getInstance().getCampaign();
	}
	
	public void makeVisible(boolean visible) 
	{
	}

	public void makePanels() throws PWCGException  
	{
	    setCenterPanel(makeLeaveCenterPanel());
	    setLeftPanel(makeLeaveLeftPanel());
	}

	private JPanel makeLeaveLeftPanel() throws PWCGException  
	{
        String imagePath = getSideImage("LeaveNav.jpg");

		ImageResizingPanel leaverPanel = new ImageResizingPanel(imagePath);
		leaverPanel.setLayout(new BorderLayout());
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

	private JPanel makeLeaveCenterPanel()  
	{
        ImageResizingPanel leaveCenterPanel = null;
        try
        {
            String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
            leaveCenterPanel = new ImageResizingPanel(imagePath);            
            leaveCenterPanel.setLayout(new BorderLayout());
    
            JPanel leavePanel = new JPanel (new GridLayout(0, 3));
            leavePanel.setOpaque(false);

            makeBlankRows(leavePanel, 6);

            JPanel leavePlayerWoundInfoPanel = makePlayerWoundHealTimePanel();
            leavePanel.add(new JLabel (" "));
            leavePanel.add(leavePlayerWoundInfoPanel);
            leavePanel.add(new JLabel (" "));

            makeBlankRows(leavePanel, 2);
            
            JPanel leaveRequestPanel = makeLeaveRequestRow();
            leavePanel.add(new JLabel (" "));
            leavePanel.add(leaveRequestPanel);
            leavePanel.add(new JLabel (" "));

			leaveCenterPanel.add(leavePanel, BorderLayout.NORTH);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
		
		return leaveCenterPanel;
	}

    private JPanel makePlayerWoundHealTimePanel() throws PWCGException
    {
        Font font = MonitorSupport.getPrimaryFontLarge();
        Color buttonBG = ColorMap.PAPER_BACKGROUND;

        JPanel leavePlayerWoundInfoPanel = new JPanel(new GridLayout(0, 1));
        leavePlayerWoundInfoPanel.setOpaque(false);
        for (SquadronMember player : campaign.getPlayers())
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
        Font font = MonitorSupport.getPrimaryFontLarge();
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
            JLabel lDummy2 = new JLabel("     ");
            JLabel lDummy3 = new JLabel("     ");
            leavePanel.add(lDummy1);
            leavePanel.add(lDummy2);
            leavePanel.add(lDummy3);
         }
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
            Logger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    private void pilotLeave() throws PWCGUserException, Exception
    {
        SoundManager.getInstance().playSound("Stapling.WAV");

        int leaveTimeWeeks = getLeaveTime();
        
        if (leaveTimeWeeks < 0 || leaveTimeWeeks > 26)
        {
            throw new PWCGUserException ("Your country cannot spare you for that long");
        }

        LeaveEvent leaveEvent = new LeaveEvent();
        leaveEvent.setLeaveTime(leaveTimeWeeks * 7);

        parent.campaignTimePassed(leaveEvent.getLeaveTime(), leaveEvent, EventPanelReason.EVENT_PANEL_REASON_LEAVE);
    }

	public int getLeaveTime()  throws PWCGUserException, Exception
	{
		if (tLeaveTime.getText() == null || tLeaveTime.getText().length() == 0)
		{
			throw new PWCGUserException ("Enter leave in weeks continuing");
		}
		
		int leaveTime = new Integer(tLeaveTime.getText()).intValue();
		
		return leaveTime;
	}
}
