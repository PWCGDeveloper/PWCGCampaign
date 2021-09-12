package pwcg.gui.campaign.activity;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.Component;
import javafx.scene.text.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javafx.scene.control.Button;
import javax.swing.JComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.JTextArea;

import pwcg.aar.AARCoordinator;
import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.TransferHandler;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.IRefreshableParentUI;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.event.AARReportMainPanel;
import pwcg.gui.rofmap.event.AARReportMainPanel.EventPanelReason;
import pwcg.gui.sound.SoundManager;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ImageResizingPanelBuilder;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.PwcgBorderFactory;
import pwcg.gui.utils.SpacerPanelFactory;

public class CampaignTransferScreen extends ImageResizingPanel implements ActionListener
{
    private static final long serialVersionUID = 1L;

    private SquadronMember squadronMemberToTransfer = null; 
    private IRefreshableParentUI parentScreen = null;
    private Campaign campaign = null;   
    private boolean passTime = false;   
    
	private JComboBox<String> cbService;
	private JComboBox<String> cbSquadron;
    private JComboBox<String> cbRole;
    private JTextArea tSquadronInfo;
	private Button acceptButton = null;
    private ArmedService service = null;

	public CampaignTransferScreen  (Campaign campaign, SquadronMember squadronMemberToTransfer, IRefreshableParentUI parentScreen, boolean passTime)
	{
        super("");
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

		this.parentScreen = parentScreen;
        this.squadronMemberToTransfer = squadronMemberToTransfer;
        this.campaign = campaign;
        this.passTime = passTime;
	}

	public void makePanels() throws PWCGException  
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.CampaignTransferScreen);
        this.setImageFromName(imagePath);

        service = this.squadronMemberToTransfer.determineService(campaign.getDate());

        this.add(BorderLayout.WEST, makeTransferNavPanel());
		this.add(BorderLayout.CENTER, makeTransferCenterPanel());
        this.add(BorderLayout.EAST, SpacerPanelFactory.makeDocumentSpacerPanel(1600));
	}

	private Pane makeTransferNavPanel() throws PWCGException  
	{
	    Pane transferrPanel  = new Pane(new BorderLayout());
		transferrPanel.setOpaque(false);
		
		Pane transferButtonPanel = new Pane(new GridLayout(0,1));
		transferButtonPanel.setOpaque(false);
		
		
        acceptButton = ButtonFactory.makeTranslucentMenuButton("Accept Transfer", "Accept Transfer", "Transfer to a new unit", this);
        transferButtonPanel.add(acceptButton);
        
        Label spacer = new Label("");
        transferButtonPanel.add(spacer);

        Button rejectButton = ButtonFactory.makeTranslucentMenuButton("Reject Transfer", "Reject Transfer", "Do not transfer", this);
        transferButtonPanel.add(rejectButton);

		transferrPanel.add(transferButtonPanel, BorderLayout.NORTH);
		
		return transferrPanel;
	}

    private Pane makeTransferCenterPanel() throws PWCGException  
    {
        Pane leaveCenterPanel = new Pane();
        leaveCenterPanel.setOpaque(false);
        leaveCenterPanel.setLayout(new BorderLayout());
        leaveCenterPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));

        Pane leaveNotification = makeTransferDocumentPanel();
        leaveCenterPanel.add(leaveNotification, BorderLayout.CENTER);

        return leaveCenterPanel;
    }

	private Pane makeTransferDocumentPanel() throws PWCGException  
	{
		ImageResizingPanel transferCenterPanel = null;
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        transferCenterPanel = ImageResizingPanelBuilder.makeImageResizingPanel(imagePath);
        transferCenterPanel.setLayout(new BorderLayout());
        transferCenterPanel.setBorder(PwcgBorderFactory.createStandardDocumentBorder());

        Color buttonBG = ColorMap.PAPER_BACKGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        GridBagLayout transferLayout = new GridBagLayout();
        
        Pane transferPanel = new Pane(transferLayout);
        transferPanel.setOpaque(false);

        List<Component> components = new ArrayList<Component>();
        int rowNum = 0;

        Label lName = new Label(squadronMemberToTransfer.getNameAndRank(), Label.LEFT);
        lName.setOpaque(false);
        lName.setFont(font);
        components.clear();
        components.add(lName);
        rowNum = addRow(transferPanel, components, rowNum);

        components.clear();
        components.add(ButtonFactory.makeDummy());
        rowNum = addRow(transferPanel, components, rowNum);

        rowNum = makeServiceChooser(buttonBG, font, transferPanel, components, rowNum);
        
        components.clear();
        components.add(ButtonFactory.makeDummy());
        rowNum = addRow(transferPanel, components, rowNum);

        rowNum = makeRoleChooser(buttonBG, font, transferPanel, components, rowNum);
        
        transferCenterPanel.add(transferPanel, BorderLayout.NORTH);
        
           
        Pane squadronPanel = createSquadronInfoPanel ();
        transferCenterPanel.add(squadronPanel, BorderLayout.SOUTH);

        evaluate();
        
        initializeValues();
		
		return transferCenterPanel;
	}

    private void initializeValues() throws PWCGException 
    {
        ArmedService playerService = squadronMemberToTransfer.determineService(campaign.getDate());
        cbService.setSelectedItem(playerService.getName());

        Squadron playerSquadron = squadronMemberToTransfer.determineSquadron();        

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

    private int makeRoleChooser(Color buttonBG, Font font, Pane transferPanel, List<Component> components, int rowNum)
    {
        Label lRole = new Label("Role: ", Label.LEFT);
        lRole.setOpaque(false);
        lRole.setFont(font);
        cbRole = new JComboBox<String>();
        cbRole.addItem(Role.ROLE_FIGHTER.getRoleDescription());
        cbRole.addItem(Role.ROLE_STRATEGIC_INTERCEPT.getRoleDescription());
        cbRole.addItem(Role.ROLE_DIVE_BOMB.getRoleDescription());
        cbRole.addItem(Role.ROLE_ATTACK.getRoleDescription());
        cbRole.addItem(Role.ROLE_BOMB.getRoleDescription());
        cbRole.addItem(Role.ROLE_STRAT_BOMB.getRoleDescription());
        cbRole.addItem(Role.ROLE_RECON.getRoleDescription());
        cbRole.addItem(Role.ROLE_SEA_PLANE.getRoleDescription());
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
        components.add(ButtonFactory.makeDummy());
        rowNum = addRow(transferPanel, components, rowNum);

        Label lTransfer = new Label("Requests a transfer to : ", Label.LEFT);
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

    private int makeServiceChooser(Color buttonBG, Font font, Pane transferPanel, List<Component> components, int rowNum) throws PWCGException
                    
    {
        Label lService = new Label("Service: ", Label.LEFT);
        lService.setOpaque(false);
        lService.setFont(font);
        cbService = new JComboBox<String>();
        
        List<ArmedService> services = null;
        if (squadronMemberToTransfer.determineCountry(campaign.getDate()).getSideNoNeutral() == Side.ALLIED)
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

    private Pane createSquadronInfoPanel () throws PWCGException 
    {
        Font font = PWCGMonitorFonts.getTypewriterFont();
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        
        Pane squadronPanel = new Pane(new GridLayout(0,3));
        squadronPanel.setOpaque(false);

        Label lDummy1 = new Label("     ");
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

        Label lDummy2 = new Label("     ");
        lDummy2.setOpaque(false);
        squadronPanel.add(lDummy2);
        
        return squadronPanel;
    }

	private int addRow(Pane panel, List<Component> components, int rowNum)
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
			panel.add(ButtonFactory.makeDummy(), constraints);
			
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
			panel.add(ButtonFactory.makeDummy(), constraints);
			
			++columnNum;
		}
		
		++rowNum;
		
		return rowNum;
	}

	private void evaluate() throws PWCGException 
	{		
		cbSquadron.removeAllItems();
		SquadronManager squadManager = PWCGContext.getInstance().getSquadronManager();
				
		List<Squadron> squadronList = squadManager.getPlayerFlyableSquadronsByService(service, campaign.getDate());
		
        String roleDesc = (String)cbRole.getSelectedItem();
        Role role = Role.getRoleFromDescription(roleDesc);
        
		for (int i = 0; i < squadronList.size(); ++i)
		{
			Squadron squad = squadronList.get(i);
			Date campaignDate = campaign.getDate();
			if(squad.getSquadronId() != squadronMemberToTransfer.getSquadronId())
			{
			    if (squad.determineSquadronPrimaryRole(campaignDate) == role)
			    {
			        String display = squad.determineDisplayName(campaign.getDate());
			        CampaignAces aces =  PWCGContext.getInstance().getAceManager().loadFromHistoricalAces(campaignDate);
			        List<Ace> squadronAces =  PWCGContext.getInstance().getAceManager().getActiveAcesForSquadron(aces, campaignDate, squad.getSquadronId());
			        if (!squadronMemberToTransfer.isCommander(campaignDate) || !squad.isCommandedByAce(squadronAces, campaignDate))
			        {
			            cbSquadron.addItem(display);
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
            Squadron squad = PWCGContext.getInstance().getSquadronManager().getSquadronByName(squadronName, campaign.getDate());
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
                SoundManager.getInstance().playSound("Stapling.WAV");
                if (passTime)
                {
                    acceptPlayerTransferWithTimePassed();
                }
                else
                {
                    acceptPlayerTransferWithNoTimePassed();
                }
            }
            else if (action.equalsIgnoreCase("Reject Transfer"))
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

    private void acceptPlayerTransferWithTimePassed() throws PWCGException 
    {        
        SoundManager.getInstance().playSound("Stapling.WAV");
        TransferEvent transferEvent = transferPlayer();        

        campaign.setCurrentMission(null);
        AARCoordinator.getInstance().submitTransfer(campaign, transferEvent.getLeaveTime());
        AARReportMainPanel eventDisplay = new AARReportMainPanel(campaign, parentScreen, EventPanelReason.EVENT_PANEL_REASON_TRANSFER, transferEvent);
        eventDisplay.makePanels();
        CampaignGuiContextManager.getInstance().pushToContextStack(eventDisplay);
    }

    private void acceptPlayerTransferWithNoTimePassed() throws PWCGException
    {
        transferPlayer();
        campaign.write();
        parentScreen.refreshInformation();
        CampaignGuiContextManager.getInstance().popFromContextStack();
    }

    private TransferEvent transferPlayer() throws PWCGException
    {
        String newSquadName = getSelectedSquad();
        TransferHandler transferHandler = new TransferHandler(campaign, squadronMemberToTransfer);
        Squadron newSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByName(newSquadName, campaign.getDate());
        TransferEvent transferEvent = transferHandler.transferPlayer(squadronMemberToTransfer.determineSquadron(), newSquadron);
        return transferEvent;
    }
}
