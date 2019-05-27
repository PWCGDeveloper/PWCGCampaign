package pwcg.gui.maingui.campaigngenerate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;
import pwcg.core.utils.Logger.LogLevel;
import pwcg.gui.campaign.config.CampaignConfigurationSimpleGUIController;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.MonitorSupport;
import pwcg.gui.maingui.campaigngenerate.CampaignGeneratorState.CampaignGeneratorWorkflow;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGJButton;

public class CampaignGeneratorDataEntryGUI extends ImageResizingPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
    private static final Color jComboBoxBackgroundColor = ColorMap.PAPER_BACKGROUND;
    private static final Color textBoxBackgroundColor = ColorMap.CHALK_FOREGROUND;
    private static final Color labelColorSelected = ColorMap.BRITISH_RED;
    private static final Color labelColorNotSelected = ColorMap.CHALK_FOREGROUND;
    
	private static Font font = null;
	
    private ButtonGroup coopGroup = new ButtonGroup();
    private ButtonModel singlePlayerButtonModel = null;
    private ButtonModel coopButtonModel = null;
    private JTextField campaignNameTextBox;
    private JTextField playerNameTextBox;
	private JComboBox<String> cbRegion;
	private JComboBox<String> cbMap;
	private JComboBox<String> cbDate;
	private JComboBox<String> cbRole;
	private JComboBox<String> cbRank;
	private JComboBox<String> cbSquadron;
    
    private JLabel lCampaignType;
    private JLabel lCampaignName;
    private JLabel lPlayerName;
    private JLabel lRegion;
    private JLabel lMap;
    private JLabel lDate;
    private JLabel lRole;
    private JLabel lRank;
    private JLabel lSquad;

    private JTextArea squadronTextBox;

    private CampaignGeneratorDO campaignGeneratorDO = new CampaignGeneratorDO();
    private CampaignGeneratorState campaignGeneratorState;
    private IPilotGeneratorUI parent = null;

	public CampaignGeneratorDataEntryGUI(IPilotGeneratorUI parent) 
	{
        super(ContextSpecificImages.menuPathMain() + "CampaignGenCenter.jpg");
        this.parent = parent;	    
		this.setLayout(new BorderLayout());
	}
	

	public void makePanels() throws PWCGException 
	{
	    font = MonitorSupport.getPrimaryFontLarge();
	    
		if (campaignGeneratorDO.getService() == null)
		{
			return;
		}
		
		try
		{			
			GridBagConstraints labelConstraints = new GridBagConstraints();
			labelConstraints.fill = GridBagConstraints.HORIZONTAL;
			labelConstraints.weightx = 0.1;
			labelConstraints.ipadx = 1;
			labelConstraints.ipady = 0;
			
			GridBagConstraints dataConstraints = new GridBagConstraints();
			dataConstraints.fill = GridBagConstraints.HORIZONTAL;
			dataConstraints.weightx = 0.3;
			dataConstraints.ipadx = 2;
			dataConstraints.ipady = 0;
			
			GridBagLayout campaignGenerateLayout = new GridBagLayout();
			JPanel campaignGeneratePanel = new JPanel(campaignGenerateLayout);
			campaignGeneratePanel.setOpaque(false);

			int rowCount = 0;
			for (int i = 0; i < 3; ++i)
			{
			    rowCount = spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, i);
			}

	        
            rowCount = createCoopWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            coopGroup.setSelected(singlePlayerButtonModel, true);
            rowCount = spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount = createCampaignNameWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount = createPlayerNameWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createRegionWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createCampaignMapWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createCampaignStartDateWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount = createCampaignRoleWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			rowCount = createRankWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			rowCount = createSquadronWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
            rowCount =  spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			createNextStepWidget(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);

			rowCount = spacerFullRow(labelConstraints, dataConstraints, campaignGeneratePanel, rowCount);
			
			this.add(campaignGeneratePanel, BorderLayout.NORTH);
	          
            JPanel squadronPanel = createSquadronInfoPanel ();
            this.add(squadronPanel, BorderLayout.SOUTH);
            
            evaluateUI();
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}

    private int createSquadronWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lSquad = createCampaignGenMenuLabel("Squadron: ", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lSquad, labelConstraints);

        cbSquadron = new JComboBox<String>();
        cbSquadron.setOpaque(false);
        cbSquadron.setBackground(jComboBoxBackgroundColor);
        cbSquadron.setActionCommand("SquadronChanged");
        cbSquadron.addActionListener(this);
        cbSquadron.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbSquadron, dataConstraints);
        
        spacerColumn (campaignGeneratePanel, 3, rowCount);
        ++rowCount;
        return rowCount;
    }

    private int createRankWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lRank = createCampaignGenMenuLabel("Pilot Rank: ", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lRank, labelConstraints);

        cbRank = new JComboBox<String>();
        cbRank.setOpaque(false);
        cbRank.setBackground(jComboBoxBackgroundColor);
        cbRank.setFont(font);
        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbRank, dataConstraints);

        cbRank.setSelectedIndex(cbRank.getItemCount()-1);
        cbRank.setActionCommand("RankChanged");
        cbRank.addActionListener(this);

        spacerColumn (campaignGeneratePanel, 3, rowCount);
        
        int serviceId = campaignGeneratorDO.getService().getServiceId();
        Date campaignDate = campaignGeneratorDO.getStartDate();
        ArmedService dateCorrectedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaignDate);
        
        makeRankChoices(dateCorrectedService);
        

        ++rowCount;
        return rowCount;
    }

    private int createNextStepWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        JLabel lNextStep = createCampaignGenMenuLabel("Press for Next/Previous Step: ", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lNextStep, labelConstraints);

        Color fgColor = ColorMap.CHALK_FOREGROUND;

        PWCGJButton nextStepButton = new PWCGJButton("Next Step");      
        nextStepButton.setActionCommand("NextStep");
        nextStepButton.setOpaque(false);
        nextStepButton.setHorizontalAlignment(SwingConstants.LEFT);
        nextStepButton.addActionListener(this);
        nextStepButton.setBorderPainted(false);
        nextStepButton.setForeground(fgColor);
        nextStepButton.setFont(font);
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(nextStepButton, dataConstraints);

        ++rowCount;

        PWCGJButton previousStepButton = new PWCGJButton("Previous Step");      
        previousStepButton.setActionCommand("PreviousStep");
        previousStepButton.setOpaque(false);
        previousStepButton.setHorizontalAlignment(SwingConstants.LEFT);
        previousStepButton.addActionListener(this);
        previousStepButton.setBorderPainted(false);
        previousStepButton.setForeground(fgColor);
        previousStepButton.setFont(font);
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(previousStepButton, dataConstraints);
        
        ++rowCount;
        return rowCount;
    }

    private int createCampaignRoleWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lRole = createCampaignGenMenuLabel("Role: ", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lRole, labelConstraints);
        
        cbRole = new JComboBox<String>();
        
        setRolesInUI();
        
        cbRole.setOpaque(false);
        cbRole.setBackground(jComboBoxBackgroundColor);
        cbRole.setSelectedIndex(0);
        cbRole.addActionListener(this);
        cbRole.setActionCommand("RoleChanged");
        cbRole.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbRole, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount);

        ++rowCount;
        return rowCount;
    }

    private void setRolesInUI() throws PWCGException
    {
        final ActionListener[] actionListeners = cbRole.getActionListeners();
        for (final ActionListener listener : actionListeners)
        {
            cbRole.removeActionListener(listener);
        }   
        
        cbRole.removeAllItems();
        
        List<Role> availableRoles = getRolesForService();
        if (availableRoles.size() > 0)
        {
            for (Role role : availableRoles)
            {
                cbRole.addItem(Role.roleToSDesc(role));
            }
        }
        else
        {
            cbRole.addItem(Role.roleToSDesc(Role.ROLE_FIGHTER));
        }
        
        cbRole.addActionListener(this);
    }

    private List<Role> getRolesForService() throws PWCGException
    {
        Map<String, Role> rolesSorted = new TreeMap<String,Role>();
        
        Date date = campaignGeneratorDO.getStartDate();
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        List<Squadron> squadronsForService = squadronManager.getFlyableSquadronsByService(campaignGeneratorDO.getService(), date);
        
        for (Squadron squadron : squadronsForService)
        {            
            Role primaryRole = squadron.determineSquadronPrimaryRole(date);

            rolesSorted.put(Role.roleToSDesc(primaryRole), primaryRole);
        }
        
        List<Role> roles = new ArrayList<Role>();
        roles.addAll(rolesSorted.values());
        
        return roles;
    }

    private int createCampaignNameWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);

        lCampaignName = createCampaignGenMenuLabel("Campaign Name:", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lCampaignName, labelConstraints);

        campaignNameTextBox = new JTextField(50);
        campaignNameTextBox.setFont(font);
        campaignNameTextBox.setBackground(textBoxBackgroundColor);
        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(campaignNameTextBox, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount + 0);

        ++rowCount;
        return rowCount;
    }

    private int createPlayerNameWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);

        lPlayerName = createCampaignGenMenuLabel("Player Name:", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lPlayerName, labelConstraints);

        playerNameTextBox = new JTextField(50);
        playerNameTextBox.setFont(font);
        playerNameTextBox.setBackground(textBoxBackgroundColor);
        
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(playerNameTextBox, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount + 0);

        ++rowCount;
        return rowCount;
    }

    private JLabel createCampaignGenMenuLabel(String labelText, GridBagConstraints labelConstraints, JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        
        JLabel menuLabel = new JLabel(labelText, JLabel.RIGHT);
        menuLabel.setFont(font);
        menuLabel.setForeground(fgColor);
        menuLabel.setOpaque(false);
        
        labelConstraints.gridx = 1;
        labelConstraints.gridy = rowCount;
        
        return menuLabel;
    }

    private int createRegionWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByService(campaignGeneratorDO.getService());
        if (country.isCountry(Country.GERMANY) && PWCGContextManager.isRoF())
        {
        	spacerColumn (campaignGeneratePanel, 0, rowCount + 0);
        	
            lRegion = createCampaignGenMenuLabel("Region: ", labelConstraints, campaignGeneratePanel, rowCount);
            campaignGeneratePanel.add(lRegion, labelConstraints);

        	cbRegion = new JComboBox<String>();
        	cbRegion.addItem("None");
        	cbRegion.addItem(SquadronMember.PRUSSIA);
        	cbRegion.addItem(SquadronMember.BAVARIA);
        	cbRegion.addItem(SquadronMember.SAXONY);
        	cbRegion.addItem(SquadronMember.WURTTEMBURG);

        	cbRegion.setOpaque(false);
        	cbRegion.setBackground(jComboBoxBackgroundColor);
        	cbRegion.setSelectedIndex(0);
        	cbRegion.setActionCommand("RegionChanged");
        	cbRegion.addActionListener(this);
        	cbRegion.setFont(font);

        	dataConstraints.gridx = 2;
        	dataConstraints.gridy = rowCount;
        	campaignGeneratePanel.add(cbRegion, dataConstraints);

        	spacerColumn (campaignGeneratePanel, 3, rowCount + 0);
        	++rowCount;
        }
        
        return rowCount;
    }

    private int createCampaignMapWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
	        JPanel campaignGeneratePanel, int rowCount) throws PWCGException
	{
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lMap = createCampaignGenMenuLabel("Campaign Map: ", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lMap, labelConstraints);
        
        cbMap = new JComboBox<String>();
        cbMap.addItem("All Maps");
        for (PWCGMap map : PWCGContextManager.getInstance().getAllMaps())
        {
            cbMap.addItem(map.getMapName());
        }
        
        cbMap.setOpaque(false);
        cbMap.setBackground(jComboBoxBackgroundColor);
        cbMap.setSelectedIndex(0);
        cbMap.setActionCommand("MapChanged");
        cbMap.addActionListener(this);
        cbMap.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbMap, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount);

        ++rowCount;

        return rowCount;
	}

    private int createCampaignStartDateWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints,
                    JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);
        
        lDate = createCampaignGenMenuLabel("Campaign Start Date: ", labelConstraints, campaignGeneratePanel, rowCount);
        campaignGeneratePanel.add(lDate, labelConstraints);
        
        cbDate = new JComboBox<String>();
        makeStartDateChoices();
        
        cbDate.setOpaque(false);
        cbDate.setBackground(jComboBoxBackgroundColor);
        cbDate.setSelectedIndex(0);
        cbDate.setActionCommand("DateChanged");
        cbDate.addActionListener(this);
        cbDate.setFont(font);

        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(cbDate, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount);

        ++rowCount;

        return rowCount;
    }

	private int spacerFullRow (GridBagConstraints labelConstraints, GridBagConstraints dataConstraints, JPanel panel, int rowCount)
	{
		JLabel lDummy = new JLabel("     ");
		
		lDummy.setOpaque(false);
		labelConstraints.gridx = 0;
		labelConstraints.gridy = rowCount;
		panel.add(lDummy, labelConstraints);
		
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        panel.add(lDummy, dataConstraints);

        ++rowCount;
        return rowCount;
	}

    private JPanel createSquadronInfoPanel () throws PWCGException
    {
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        
        JPanel squadronPanel = new JPanel(new GridLayout(0,3));
        squadronPanel.setOpaque(false);

        JLabel lDummy1 = new JLabel("     ");
        lDummy1.setOpaque(false);
        squadronPanel.add(lDummy1);
        
        // Squadron info
        squadronTextBox = new JTextArea();
        squadronTextBox.setBackground(bgColor);
        squadronTextBox.setForeground(fgColor);
        squadronTextBox.setFont(font);
        squadronTextBox.setEditable(false);
        squadronTextBox.setLineWrap(true);
        squadronTextBox.setWrapStyleWord(true);
        squadronTextBox.setText("");
        squadronTextBox.setOpaque(false);
        squadronPanel.add(squadronTextBox);

        JLabel lDummy2 = new JLabel("     ");
        lDummy2.setOpaque(false);
        squadronPanel.add(lDummy2);
        
        return squadronPanel;
    }
    
    

    private int createCoopWidget(GridBagConstraints labelConstraints, GridBagConstraints dataConstraints, JPanel campaignGeneratePanel, int rowCount) throws PWCGException
    {
        spacerColumn (campaignGeneratePanel, 0, rowCount);

        JPanel coopButtonPanel = createCoopPanel();
                
        dataConstraints.gridx = 2;
        dataConstraints.gridy = rowCount;
        campaignGeneratePanel.add(coopButtonPanel, dataConstraints);

        spacerColumn (campaignGeneratePanel, 3, rowCount + 0);

        ++rowCount;
        return rowCount;
    }

    private JPanel createCoopPanel() throws PWCGException
    {
        JPanel coopButtonPanel = new JPanel(new BorderLayout());
        coopButtonPanel.setOpaque(false);

        JLabel spacerLabel = makeCoopLabel("          ");        
        coopButtonPanel.add(spacerLabel, BorderLayout.WEST);

        JPanel shapePanel = new JPanel(new BorderLayout());
        shapePanel.setOpaque(false);

        JPanel coopButtonPanelGrid = new JPanel(new GridLayout(0,1));
        coopButtonPanelGrid.setOpaque(false);
        
        lCampaignType = makeCoopLabel(CampaignConfigurationSimpleGUIController.CAMPAIGN_TYPE + ":");      
        coopButtonPanelGrid.add(lCampaignType);

        JRadioButton singlePlayerButton = PWCGButtonFactory.makeRadioButton("Single Player Mode", "Mission Mode: Single Player", "Select single player mode for generated missions", false, this);       
        coopButtonPanelGrid.add(singlePlayerButton);
        singlePlayerButtonModel = singlePlayerButton.getModel();
        coopGroup.add(singlePlayerButton);

        JRadioButton coopButton = PWCGButtonFactory.makeRadioButton("Coop Mode", "Mission Mode: Coop", "Select coop player mode for generated missions", false, this);              
        coopButtonPanelGrid.add(coopButton);
        coopButtonModel = coopButton.getModel();
        coopGroup.add(coopButton);

        coopButtonPanel.add(coopButtonPanelGrid, BorderLayout.SOUTH);
        
        shapePanel.add(coopButtonPanelGrid, BorderLayout.NORTH);
        coopButtonPanel.add(shapePanel, BorderLayout.CENTER);

        return coopButtonPanel;
    }

    private JLabel makeCoopLabel(String buttonName) throws PWCGException
    {
        Font font = MonitorSupport.getPrimaryFontLarge();

        JLabel button= new JLabel(buttonName);
        button.setOpaque(false);
        button.setFont(font);

        return button;
    }

	private void spacerColumn (JPanel panel, int column, int row)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0.2;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.gridx = column;
		constraints.gridy = row;

		JLabel lDummy = new JLabel("      ");
		lDummy.setOpaque(false);
		panel.add(lDummy, constraints);
	}

	public void evaluateUI() throws PWCGException 
	{
	    initializeWidgets();
        
        if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_TYPE)
        {
        	lCampaignType.setForeground(labelColorSelected);
        	singlePlayerButtonModel.setEnabled(true);
        	coopButtonModel.setEnabled(true);
        }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_CAMPAIGN_NAME)
        {
            lCampaignName.setForeground(labelColorSelected);
            campaignNameTextBox.setEnabled(true);
        }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_PLAYER_NAME)
        {
            lPlayerName.setForeground(labelColorSelected);
            playerNameTextBox.setEnabled(true);
        }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_REGION)
	    {
	        lRegion.setForeground(labelColorSelected);
            cbRegion.setEnabled(true);
	    }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_ROLE)
	    {
	        setRolesInUI();
	        
	        String selectedRole = Role.roleToSDesc(campaignGeneratorDO.getRole());
	        
	        cbRole.setSelectedItem(selectedRole);
            
	        lRole.setForeground(labelColorSelected);
            cbRole.setEnabled(true);
	    }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_MAP)
	    {
	        lMap.setForeground(labelColorSelected);
            cbMap.setEnabled(true);
	    }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_DATE)
	    {
	    	makeStartDateChoices();
	    	lDate.setForeground(labelColorSelected);
            cbDate.setEnabled(true);
	    }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_RANK)
	    {
	        cbRank.setSelectedItem(campaignGeneratorDO.getRank());
	        lRank.setForeground(labelColorSelected);
            cbRank.setEnabled(true);
	    }

        else if (campaignGeneratorState.getCurrentStep() == CampaignGeneratorWorkflow.CHOOSE_SQUADRON)
	    {
	        lSquad.setForeground(labelColorSelected);
	        
            int serviceId = campaignGeneratorDO.getService().getServiceId();
            Date campaignDate = campaignGeneratorDO.getStartDate();
            ArmedService dateCorrectedService = ArmedServiceFactory.createServiceManager().getArmedServiceById(serviceId, campaignDate);
            
	        makeSquadronChoices(campaignDate, dateCorrectedService);

	        String squadronName = (String)cbSquadron.getSelectedItem();
	        String squadronInfo = getSquadronInfo(campaignDate, squadronName);
	        this.squadronTextBox.setText(squadronInfo);

            cbSquadron.setEnabled(true);
	    }
	    
	    if (campaignGeneratorState.isComplete())
	    {
	        parent.enableCompleteAction(true);
	    }
	}

    private void initializeWidgets()
    {
        parent.enableCompleteAction(false);
	    
	    if (cbRegion != null)
	    {
	        cbRegion.setEnabled(false);
	    }
	    
	    singlePlayerButtonModel.setEnabled(false);
	    coopButtonModel.setEnabled(false);
	    campaignNameTextBox.setEnabled(false);
	    playerNameTextBox.setEnabled(false);
        cbRole.setEnabled(false);
        cbMap.setEnabled(false);
        cbDate.setEnabled(false);
        cbRank.setEnabled(false);
        cbSquadron.setEnabled(false);
        
        lCampaignType.setForeground(labelColorNotSelected);
        lCampaignName.setForeground(labelColorNotSelected);
        lPlayerName.setForeground(labelColorNotSelected);
        if (lRegion != null)
        {
            lRegion.setForeground(labelColorNotSelected);
        }
        lRole.setForeground(labelColorNotSelected);
        lMap.setForeground(labelColorNotSelected);
        lDate.setForeground(labelColorNotSelected);
        lRank.setForeground(labelColorNotSelected);
        lSquad.setForeground(labelColorNotSelected);
    }

    private String getSquadronInfo(Date campaignDate, String squadronName) throws PWCGException 
    {
        if (squadronName == null)
        {
            return "";
        }

        Squadron squad = PWCGContextManager.getInstance().getSquadronManager().getSquadronByName(squadronName, campaignDate);
        return squad.determineSquadronInfo(campaignDate);
    }

	private void makeRankChoices(ArmedService dateCorrectedService) 
	{
		cbRank.removeActionListener(this);
		cbRank.removeAllItems();

		IRankHelper ranks = RankFactory.createRankHelper();
		List<String> rankList = ranks.getRanksByService(dateCorrectedService);
		for (int i = 0; i < rankList.size(); ++i)
		{
			cbRank.addItem(rankList.get(i));
			Logger.log(LogLevel.DEBUG, "Add Rank = " + rankList.get(i));
		}
		
		cbRank.addActionListener(this);
	}

	private void makeStartDateChoices() throws PWCGException 
	{
	    try
	    {
    		cbDate.removeAllItems();
	    	for (String startDate : getDatesForMap())
	    	{
	    		cbDate.addItem(startDate);
	    	}
	    }
	    catch (Exception exp)
	    {
            Logger.logException(exp);
            throw exp;
	    }
	}
	
    
    private List<String> getDatesForMap() throws PWCGException
    {
    	List<String> startDates = new ArrayList<>();
        for (String startDate : PWCGContextManager.getInstance().getCampaignStartDates())
        {
            Date date = DateUtils.getDateWithValidityCheck(startDate);            
            if (!campaignGeneratorDO.getService().getServiceStartDate().after(date) && 
                 campaignGeneratorDO.getService().getServiceEndDate().after(date))
            {
            	PWCGMap map = PWCGContextManager.getInstance().getMapByMapId(campaignGeneratorDO.getFrontMap());
            	if (map == null)
            	{
                	startDates.add(startDate);
            	}
            	else
            	{
            	    
            		if (map.getFrontDatesForMap().isMapActive(date))
            		{
                    	startDates.add(startDate);
            		}
            	}

            }
        }
        
        return startDates;
    }


	private void makeSquadronChoices(Date campaignDate, ArmedService dateCorrectedService) throws PWCGException 
	{
	    try
	    {
    		cbSquadron.removeAllItems();
            CampaignGeneratorSquadronFilter squadronFilter = new CampaignGeneratorSquadronFilter();
	        String selectedRole = (String)cbRole.getSelectedItem();
            List<String> squadronNames = squadronFilter.makeSquadronChoices(campaignDate, dateCorrectedService, campaignGeneratorDO.getFrontMap(), selectedRole, campaignGeneratorDO.isCommandRank());
            
            for (String squadronName : squadronNames)
            {
				cbSquadron.addItem(squadronName);
    		}
	    }
	    catch (Exception exp)
	    {
            Logger.logException(exp);
            throw exp;
	    }
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
            String campaignName = (String)campaignNameTextBox.getText();
            campaignGeneratorDO.setCampaignName(campaignName);
            
            String playerName = (String)playerNameTextBox.getText();
            campaignGeneratorDO.setPlayerName(playerName);
            
            if (ae.getActionCommand().equalsIgnoreCase("RegionChanged"))
            {
                String region = (String)cbRegion.getSelectedItem();
                campaignGeneratorDO.setRegion(region);
            }
            else if (ae.getActionCommand().equalsIgnoreCase("RoleChanged"))
            {
                String roleDesc = (String)cbRole.getSelectedItem();
                Role role = Role.descToRole(roleDesc);
                campaignGeneratorDO.setRole(role);
            }
            else if (ae.getActionCommand().equalsIgnoreCase("MapChanged"))
			{
		        String mapName = (String)cbMap.getSelectedItem();
		        PWCGMap map = PWCGContextManager.getInstance().getMapByMapName(mapName);
		        if (map == null)
		        {
				    campaignGeneratorDO.setFrontMap(null);
		        }
		        else
		        {
				    campaignGeneratorDO.setFrontMap(map.getMapIdentifier());
		        }
			}
            else if (ae.getActionCommand().equalsIgnoreCase("DateChanged"))
			{
                Date campaignDate =  getDateFromComboBox();
			    campaignGeneratorDO.setStartDate(campaignDate);
			}
			else if (ae.getActionCommand().equalsIgnoreCase("RankChanged"))
			{
		        String rank = (String)cbRank.getSelectedItem();
		        campaignGeneratorDO.setRank(rank);
			}
            else if (ae.getActionCommand().equalsIgnoreCase("SquadronChanged"))
            {
                String squadronName = (String)cbSquadron.getSelectedItem();
                if (squadronName != null)
                {
                    campaignGeneratorDO.setSquadName(squadronName);
                    String squadronInfo = getSquadronInfo(campaignGeneratorDO.getStartDate(), squadronName);
                    this.squadronTextBox.setText(squadronInfo);
                }
            }
            else if (ae.getActionCommand().equalsIgnoreCase("NextStep"))
            {
                campaignGeneratorState.goToNextStep();
                evaluateUI() ;
            }
            else if (ae.getActionCommand().equalsIgnoreCase("PreviousStep"))
            {
                campaignGeneratorState.goToPreviousStep();
                evaluateUI() ;
            }
            else if (ae.getActionCommand().contains("Single"))
            {
                campaignGeneratorDO.setCoop(false);
                coopGroup.setSelected(singlePlayerButtonModel, true);
            }
            else if (ae.getActionCommand().contains("Coop"))
            {
                campaignGeneratorDO.setCoop(true);
                coopGroup.setSelected(coopButtonModel, true);
           }
            
            revalidate();
            repaint();
		}
		catch (Exception e)
		{
			Logger.logException(e);
			ErrorDialog.internalError(e.getMessage());
		}
	}


    private Date getDateFromComboBox() throws PWCGException 
	{
    	Date campaignDate = null;
        String dateStr = (String)cbDate.getSelectedItem();
        if (dateStr != null)
        {
        	campaignDate = DateUtils.getDateWithValidityCheck(dateStr);
        }
        
        return campaignDate;
	}

    public CampaignGeneratorDO getCampaignGeneratorDO()
    {
        return campaignGeneratorDO;
    }

    public void setCampaignGeneratorDO(CampaignGeneratorDO campaignGeneratorDO) throws PWCGException
    {
        this.campaignGeneratorDO = campaignGeneratorDO;
        
        campaignGeneratorState = new CampaignGeneratorState(campaignGeneratorDO);
    }
}
