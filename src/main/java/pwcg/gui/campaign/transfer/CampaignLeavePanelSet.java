package pwcg.gui.campaign.transfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	
	
	
	/**
	 * @throws PWCGException
	 */
	public void makePanels() throws PWCGException  
	{
	    setCenterPanel(makeLeaveCenterPanel());
	    setLeftPanel(makeLeaveLeftPanel());
	}
	
	
	/**
	 * @return
	 * @throws PWCGException 
	 * @
	 */
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
			
			Color buttonBG = ColorMap.PAPER_BACKGROUND;

			
			Font font = MonitorSupport.getPrimaryFont();
	
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.HORIZONTAL;
			constraints.ipadx = 3;
			constraints.ipady = 3;
			GridBagLayout leaveLayout = new GridBagLayout();
			
			JPanel leavePanel = new JPanel(leaveLayout);
			leavePanel.setOpaque(false);

			JLabel lDummy = null;
			
			int numDummyRows = 5;
			for (int i = 0; i < numDummyRows; ++i)
			{
				lDummy = new JLabel("     ");
				lDummy.setOpaque(false);
				lDummy.setFont(font);
			    constraints.weightx = 0.15;
				constraints.gridx = 0;
				constraints.gridy = i;
				leavePanel.add(lDummy, constraints);
			}

			int numDummyCols = 3;
			for (int i = 0; i < numDummyCols; ++i)
			{
				lDummy = new JLabel("     ");
				lDummy.setOpaque(false);
				lDummy.setFont(font);
			    constraints.weightx = 0.15;
				constraints.gridx = i;
				constraints.gridy = numDummyRows + 0;
				leavePanel.add(lDummy, constraints);
			}
			
			SquadronMember player = campaign.getPlayers().get(0);

			JLabel lName = new JLabel(player.getNameAndRank(), JLabel.LEFT);
			lName.setOpaque(false);
			lName.setFont(font);
		    constraints.weightx = 0.15;
			constraints.gridx = numDummyCols + 0;
			constraints.gridy = numDummyRows + 0;
			leavePanel.add(lName, constraints);
							
			for (int i = 0; i < numDummyCols + 1; ++i)
			{
				lDummy = new JLabel("     ");
				lDummy.setOpaque(false);
				lDummy.setFont(font);
			    constraints.weightx = 0.15;
				constraints.gridx = numDummyCols + 1 + i;
				constraints.gridy = numDummyRows + 0;
				leavePanel.add(lDummy, constraints);
			}

			lDummy = new JLabel("     ");
			lDummy.setOpaque(false);
			lDummy.setFont(font);
		    constraints.weightx = 0.15;
			constraints.gridx = 0;
			constraints.gridy = numDummyRows + 1;
			leavePanel.add(lDummy, constraints);

			for (int i = 0; i < numDummyCols; ++i)
			{
				lDummy = new JLabel("     ");
				lDummy.setOpaque(false);
				lDummy.setFont(font);
			    constraints.weightx = 0.15;
				constraints.gridx = i;
				constraints.gridy = numDummyRows + 2;
				leavePanel.add(lDummy, constraints);
			}
			
			JLabel lLeave = new JLabel("Request Leave Time (weeks): ", JLabel.LEFT);
			lLeave.setOpaque(false);
			lLeave.setFont(font);
		    constraints.weightx = 0.15;
			constraints.gridx = numDummyCols + 0;
			constraints.gridy = numDummyRows + 2;
			leavePanel.add(lLeave, constraints);

			
			tLeaveTime = new JTextField(5);
			tLeaveTime.setBackground(buttonBG);
			tLeaveTime.setOpaque(false);
			tLeaveTime.setFont(font);
		    constraints.weightx = 0.15;
			constraints.gridx = numDummyCols + 1;
			constraints.gridy = numDummyRows + 2;
			leavePanel.add(tLeaveTime, constraints);
			
			for (int i = 0; i < numDummyCols; ++i)
			{
				lDummy = new JLabel("     ");
				lDummy.setOpaque(false);
				lDummy.setFont(font);
			    constraints.weightx = 0.15;
				constraints.gridx = numDummyCols + 2 + i;
				constraints.gridy = numDummyRows + 2;
				leavePanel.add(lDummy, constraints);
			}

			leaveCenterPanel.add(leavePanel, BorderLayout.NORTH);
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
		
		return leaveCenterPanel;
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
