package pwcg.gui.campaign.transfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.TransferHandler;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
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

public class CampaignTransferPanelSet extends PwcgGuiContext implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private CampaignHomeGUI parent = null;
	private Campaign campaign = null;	
    private JComboBox<String> cbService;
	private JComboBox<String> cbSquadron;
    private JComboBox<String> cbRole;
    private JTextArea tSquadronInfo;
	private JButton acceptButton = null;
    private ArmedService service = null;

	public CampaignTransferPanelSet  (CampaignHomeGUI parent)
	{
        super();

		this.parent = parent;
		this.campaign = PWCGContextManager.getInstance().getCampaign();
	}
	
	public void makeVisible(boolean visible) 
	{
	}
	
	public void makePanels() throws PWCGException  
	{
	    // Initialize things that might change based on the transfer
        service = this.campaign.getPlayer().determineService(campaign.getDate());

		// Not so great dependency - have to make the right panel first so accept button is not null
		// when evaluate is called
        setLeftPanel(makeTransferNavPanel());
		setCenterPanel(makeTransferCenterPanel());
	}
	
	
	/**
	 * @return
	 * @throws PWCGException 
	 * @
	 */
	private JPanel makeTransferNavPanel() throws PWCGException  
	{
        String imagePath = getSideImage("TransferNav.jpg");

		ImageResizingPanel transferrPanel = new ImageResizingPanel(imagePath);
		transferrPanel.setLayout(new BorderLayout());
		transferrPanel.setOpaque(false);
		
		JPanel transferButtonPanel = new JPanel(new GridLayout(0,1));
		transferButtonPanel.setOpaque(false);
		
		
        acceptButton = PWCGButtonFactory.makeMenuButton("Accept Transfer", "Accept Transfer", this);
        transferButtonPanel.add(acceptButton);

        JButton rejectButton = PWCGButtonFactory.makeMenuButton("Reject Transfer", "Reject Transfer", this);
        transferButtonPanel.add(rejectButton);

		transferrPanel.add(transferButtonPanel, BorderLayout.NORTH);
		
		return transferrPanel;
	}

	
	
	/**
	 * @return
	 * @
	 */
	private JPanel makeTransferCenterPanel()  
	{
		ImageResizingPanel transferCenterPanel = null;

		try
		{
            String imagePath = ContextSpecificImages.imagesMisc() + "Paper.jpg";
			transferCenterPanel = new ImageResizingPanel(imagePath);
			transferCenterPanel.setLayout(new BorderLayout());
			
			Color buttonBG = ColorMap.PAPER_BACKGROUND;

			Font font = MonitorSupport.getPrimaryFont();
	
			GridBagLayout transferLayout = new GridBagLayout();
			
			JPanel transferPanel = new JPanel(transferLayout);
			transferPanel.setOpaque(false);

			List<Component> components = new ArrayList<Component>();
			int rowNum = 0;

			int numDummyRows = 5;
			for (int i = 0; i < numDummyRows; ++i)
			{
				components.clear();
				components.add(PWCGButtonFactory.makeDummy());
				rowNum = addRow(transferPanel, components, rowNum);
			}

			// Transfer label
		     SquadronMember player = campaign.getPlayer();

			JLabel lName = new JLabel(player.getNameAndRank(), JLabel.LEFT);
			lName.setOpaque(false);
			lName.setFont(font);
			components.clear();
			components.add(lName);
			rowNum = addRow(transferPanel, components, rowNum);

            // A dummy row
            components.clear();
            components.add(PWCGButtonFactory.makeDummy());
            rowNum = addRow(transferPanel, components, rowNum);

            // Transfer Service
            rowNum = makeServiceChooser(buttonBG, font, transferPanel, components, rowNum);
            
            // A dummy row
            components.clear();
            components.add(PWCGButtonFactory.makeDummy());
            rowNum = addRow(transferPanel, components, rowNum);


            // Transfer role
            rowNum = makeRoleChooser(buttonBG, font, transferPanel, components, rowNum);
			
            // A dummy row
            components.clear();
            components.add(PWCGButtonFactory.makeDummy());
            rowNum = addRow(transferPanel, components, rowNum);
            
			transferCenterPanel.add(transferPanel, BorderLayout.NORTH);
			
	           
            // Squadron info
            JPanel squadronPanel = createSquadronInfoPanel ();
            transferCenterPanel.add(squadronPanel, BorderLayout.SOUTH);

            evaluate();
            
            initializeValues();
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
		
		return transferCenterPanel;
	}

    private void initializeValues() throws PWCGException 
    {
        ArmedService playerService = campaign.getPlayer().determineService(campaign.getDate());
        cbService.setSelectedItem(playerService.getName());

        Squadron playerSquadron = campaign.determineSquadron();        

        if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_BOMB))
        {
            cbRole.setSelectedItem(Role.ROLE_BOMB);
        }
        else if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_DIVE_BOMB))
        {
            cbRole.setSelectedItem(Role.ROLE_DIVE_BOMB);
        }
        else if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_ATTACK))
        {
            cbRole.setSelectedItem(Role.ROLE_ATTACK);
        }
        if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_SEA_PLANE))
        {
            cbRole.setSelectedItem(Role.ROLE_SEA_PLANE);
        }
        else if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_STRAT_BOMB))
        {
            cbRole.setSelectedItem(Role.ROLE_STRAT_BOMB);
        }
        else if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_RECON))
        {
            cbRole.setSelectedItem(Role.ROLE_RECON);
        }
        else if (playerSquadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_SEA_PLANE))
        {
            cbRole.setSelectedItem(Role.ROLE_SEA_PLANE);
        }
        else
        {
            cbRole.setSelectedItem(Role.ROLE_FIGHTER);
        }                
    }

    private int makeRoleChooser(Color buttonBG, Font font, JPanel transferPanel, List<Component> components, int rowNum)
    {
        JLabel lRole = new JLabel("Role: ", JLabel.LEFT);
        lRole.setOpaque(false);
        lRole.setFont(font);
        cbRole = new JComboBox<String>();
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_FIGHTER));
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_DIVE_BOMB));
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_ATTACK));
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_BOMB));
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_STRAT_BOMB));
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_RECON));
        cbRole.addItem(Role.roleToSDesc(Role.ROLE_SEA_PLANE));
        cbRole.setOpaque(false);
        cbRole.setBackground(buttonBG);
        cbRole.setSelectedIndex(0);
        cbRole.addActionListener(this);
        cbRole.setActionCommand("Role Changed");
        components.clear();
        components.add(lRole);
        components.add(cbRole);
        rowNum = addRow(transferPanel, components, rowNum);

        components.clear();
        components.add(PWCGButtonFactory.makeDummy());
        rowNum = addRow(transferPanel, components, rowNum);

        JLabel lTransfer = new JLabel("Requests a transfer to : ", JLabel.LEFT);
        lTransfer.setOpaque(false);
        lTransfer.setFont(font);
        cbSquadron = new JComboBox<String>();
        cbSquadron.setBackground(buttonBG);
        cbSquadron.setActionCommand("SquadronChanged");
        cbSquadron.addActionListener(this);
        components.clear();
        components.add(lTransfer);
        components.add(cbSquadron);
        rowNum = addRow(transferPanel, components, rowNum);
        return rowNum;
    }

    private int makeServiceChooser(Color buttonBG, Font font, JPanel transferPanel, List<Component> components, int rowNum) throws PWCGException
                    
    {
        JLabel lService = new JLabel("Service: ", JLabel.LEFT);
        lService.setOpaque(false);
        lService.setFont(font);
        cbService = new JComboBox<String>();
        
        List<ArmedService> services = null;
        if (campaign.determineCountry().getSideNoNeutral() == Side.ALLIED)
        {
            services = ArmedServiceFactory.createServiceManager().getAlliedServices(campaign.getDate());
        }
        else
        {
            services = ArmedServiceFactory.createServiceManager().getAxisServices(campaign.getDate());
        }
        
        for (ArmedService service : services)
        {
            addServiceName(service);
        }
 
        
        
        cbService.setOpaque(false);
        cbService.setBackground(buttonBG);
        cbService.setSelectedIndex(0);
        cbService.addActionListener(this);
        cbService.setActionCommand("Service Changed");
        components.clear();
        components.add(lService);
        components.add(cbService);
        rowNum =  addRow(transferPanel, components, rowNum);
        return rowNum;
    }

	private void addServiceName (ArmedService service)
	{
        cbService.addItem(service.getName());
	}

    private JPanel createSquadronInfoPanel () throws PWCGException 
    {
        Font font = MonitorSupport.getTypewriterFont();
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        
        JPanel squadronPanel = new JPanel(new GridLayout(0,3));
        squadronPanel.setOpaque(false);

        JLabel lDummy1 = new JLabel("     ");
        lDummy1.setOpaque(false);
        squadronPanel.add(lDummy1);
        
        // Squadron info
        tSquadronInfo = new JTextArea();
        tSquadronInfo.setBackground(bgColor);
        tSquadronInfo.setForeground(fgColor);
        tSquadronInfo.setFont(font);
        tSquadronInfo.setEditable(false);
        tSquadronInfo.setLineWrap(true);
        tSquadronInfo.setWrapStyleWord(true);
        tSquadronInfo.setText("");
        tSquadronInfo.setOpaque(false);
        squadronPanel.add(tSquadronInfo);

        JLabel lDummy2 = new JLabel("     ");
        lDummy2.setOpaque(false);
        squadronPanel.add(lDummy2);
        
        return squadronPanel;
    }

	private int addRow(JPanel panel, List<Component> components, int rowNum)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.ipadx = 3;
		constraints.ipady = 3;
		
		int numDummyCols = 3;
		int columnNum = 0;
		
		for (int i = 0; i < numDummyCols; ++i)
		{
		    constraints.weightx = 0.15;
			constraints.gridx = columnNum;
			constraints.gridy = rowNum;
			panel.add(PWCGButtonFactory.makeDummy(), constraints);
			
			++columnNum;
		}
		
		for (Component component : components)
		{
		    constraints.weightx = 0.2;
			constraints.gridx = columnNum;
			constraints.gridy = rowNum;
			panel.add(component, constraints);
			
			++columnNum;
		}
		
		
		for (int i = 0; i < numDummyCols; ++i)
		{
		    constraints.weightx = 0.15;
			constraints.gridx = columnNum;
			constraints.gridy = rowNum;
			panel.add(PWCGButtonFactory.makeDummy(), constraints);
			
			++columnNum;
		}
		
		++rowNum;
		
		return rowNum;
	}

	private void evaluate() throws PWCGException 
	{		
		cbSquadron.removeAllItems();
		SquadronManager squadManager = PWCGContextManager.getInstance().getSquadronManager();
				
		List<Squadron> squadronList = squadManager.getFlyableSquadronsByService(service, campaign.getDate());
		
        String roleDesc = (String)cbRole.getSelectedItem();
        Role role = Role.descToRole(roleDesc);
        
		for (int i = 0; i < squadronList.size(); ++i)
		{
			Squadron squad = squadronList.get(i);
			Date campaignDate = campaign.getDate();
			if (squad.isCanFly(campaignDate))
			{
				if(squad.getSquadronId() != campaign.getSquadronId())
				{
					if (squad.determineSquadronPrimaryRole(campaignDate) == role)
					{
					    String display = squad.determineDisplayName(campaign.getDate());
					    CampaignAces aces =  PWCGContextManager.getInstance().getAceManager().loadFromHistoricalAces(campaignDate);
					    List<Ace> squadronAces =  PWCGContextManager.getInstance().getAceManager().getActiveAcesForSquadron(aces, campaignDate, squad.getSquadronId());
					    if (!campaign.isPlayerCommander() || !squad.isCommandedByAce(squadronAces, campaignDate))
					    {
					        cbSquadron.addItem(display);
					    }
					}
				}
			}
		}
		
        String squadronName = (String)cbSquadron.getSelectedItem();
		setSquadronInfo(squadronName);
		
		if (cbSquadron.getItemCount() == 0)
		{
			acceptButton.setEnabled(false);
		}
		else
		{
			acceptButton.setEnabled(true);
		}
	}

    private void setSquadronInfo(String squadronName) throws PWCGException 
    {
        String squadronInfo = "";
        if (squadronName != null)
        {
            ICountry country = CountryFactory.makeCountryByService (service);

            Squadron squad = PWCGContextManager.getInstance().getSquadronManager().getSquadronByNameAndCountry(squadronName, country, campaign.getDate());
            squadronInfo = squad.determineSquadronInfo(campaign.getDate());
        }
        tSquadronInfo.setText(squadronInfo);
    }

	public String getSelectedSquad()
	{
		String squadName = (String)cbSquadron.getSelectedItem();
		return squadName;
	}

	public void actionPerformed(ActionEvent ae)
	{
        String action = ae.getActionCommand();

        try
		{
            if (action.equalsIgnoreCase("Service Changed"))
            {
                String serviceName = (String)cbService.getSelectedItem();
                if (serviceName != null)
                {
                    service = ArmedServiceFactory.createServiceManager().getArmedServiceByName(serviceName, campaign.getDate());
                }
                evaluate();
                return;
            }
            else if (action.equalsIgnoreCase("Role Changed"))
            {
                evaluate();
                return;
            }
            else if (action.equalsIgnoreCase("SquadronChanged"))
            {
                String squadronName = (String)cbSquadron.getSelectedItem();
                setSquadronInfo(squadronName);
            }
            else if (action.equalsIgnoreCase("Accept Transfer"))
            {
                acceptTransfer();
            }
            else if (action.equalsIgnoreCase("Reject Transfer"))
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

    public ArmedService getService()
    {
        return this.service;
    }

    private void acceptTransfer() throws PWCGException 
    {        
        SoundManager.getInstance().playSound("Stapling.WAV");
        
        String newSquadName = getSelectedSquad();
        ArmedService newService = getService();

        TransferHandler transferHandler = new TransferHandler(campaign);
        TransferEvent transferEvent = transferHandler.transferPlayer(newService, newSquadName);

        parent.campaignTimePassed(transferEvent.getLeaveTime(), transferEvent, EventPanelReason.EVENT_PANEL_REASON_TRANSFER);
    }
}
