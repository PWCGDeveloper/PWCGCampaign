package pwcg.gui.rofmap.intelmap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.CampaignGuiContextManager;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.dialogs.PWCGMonitorSupport;
import pwcg.gui.rofmap.MapGUI;
import pwcg.gui.rofmap.MapScroll;
import pwcg.gui.utils.ContextSpecificImages;
import pwcg.gui.utils.ImagePanelLayout;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;

public class IntelMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
	private static final long serialVersionUID = 1L;

    private ButtonGroup mapButtonGroup = new ButtonGroup();
    private Campaign campaign = null;

	public IntelMapGUI(Date mapDate) throws PWCGException  
	{
		super(mapDate);
		setLayout(new BorderLayout());
		this.campaign = PWCGContext.getInstance().getCampaign();
	}

	public void makePanels() 
	{
		try
		{
			Color bg = ColorMap.MAP_BACKGROUND;
			setSize(200, 200);
			setOpaque(false);
			setBackground(bg);

			// Initialize to the players current map
	        List<FrontMapIdentifier> airfieldMaps = AirfieldManager.getMapIdForAirfield(campaign.findReferencePlayer().determineSquadron().determineCurrentAirfieldName(campaign.getDate()));
            PWCGContext.getInstance().changeContext(airfieldMaps.get(0));
								
			setRightPanel(makeRightPanel(-1));
			setCenterPanel(createMapPanel());
			setLeftPanel(makeNavigationPanel());		
			
	        centerMapAt(null);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError (e.getMessage());
		}
	}

    private JPanel createMapPanel() throws PWCGException, PWCGException
    {
        JPanel intelMapCenterPanel = new JPanel(new BorderLayout());

        IntelMapPanel mapPanel = new IntelMapPanel(this, campaign);
        mapScroll = new MapScroll(mapPanel);  
        mapPanel.setData();
        mapPanel.setMapBackground(100);

        Campaign campaign = PWCGContext.getInstance().getCampaign();
        makeMapPanelPoints(campaign.getDate());
        
        centerIntelMap();
        
        intelMapCenterPanel.add(mapScroll.getMapScrollPane());
        return intelMapCenterPanel;
    }

    private JPanel makeNavigationPanel() throws PWCGException  
    {
        String imagePath = getSideImage(campaign, "IntelMapNav.jpg");

        ImageResizingPanel intelNavPanel = new ImageResizingPanel(imagePath);
        intelNavPanel.setLayout(new BorderLayout());
        intelNavPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        JButton finished = PWCGButtonFactory.makeMenuButton("Finished", "Finished", this);
        buttonPanel.add(finished);
        
        JLabel spacer1 = PWCGButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer1);

        JPanel radioButtonPanel = new JPanel( new GridLayout(0,1));
        radioButtonPanel.setOpaque(false);
        
        JLabel spacer2 = PWCGButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer2);

        intelNavPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return intelNavPanel;
    }
    
	private void makeMapPanelPoints(Date date) throws PWCGException  
	{
	    IntelMapPanel mapPanel = (IntelMapPanel)mapScroll.getMapPanel();
	    mapPanel.setData();
	}

	private void centerIntelMap()
	{
        IntelMapPanel mapPanel = (IntelMapPanel)mapScroll.getMapPanel();
        
		Point initialPosition = null;
	    for (IntelSquadronMapPoint mapPoint : mapPanel.getSquadronPoints().values())
	    {
	    	if (mapPoint.isPlayerSquadron)
	    	{
				initialPosition = mapPanel.coordinateToPoint(mapPoint.coord);
	    	}
	    }
	    
		centerMapAt(initialPosition);

	}

    public void updateInfoPanel(int squadId) throws PWCGException 
    {
        JPanel updatedSquadronInfoPanel = makeRightPanel(squadId);

        CampaignGuiContextManager.getInstance().changeCurrentContext(null, null, updatedSquadronInfoPanel);
    }

    private JPanel makeRightPanel(int squadId) throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        JPanel infoPanel = new ImagePanelLayout(imagePath, new BorderLayout());
        infoPanel.setOpaque(false);
        infoPanel.setBackground(bgColor);                
        JPanel squadronInfoPanel = makeInfoPanel(squadId);
        JPanel mapCheckBoxesPanel = makeMapCheckBoxes();
        infoPanel.add(mapCheckBoxesPanel, BorderLayout.NORTH);
        infoPanel.add(squadronInfoPanel, BorderLayout.CENTER);
        
        return infoPanel;
    }

	private JPanel makeInfoPanel(int squadId) throws PWCGException 
	{
		JPanel squadDescriptionPanel = new JPanel(new BorderLayout());
		squadDescriptionPanel.setOpaque(false);
		JPanel descriptionGrid = makeSquadronDescriptionGrid();
		squadDescriptionPanel.add(descriptionGrid, BorderLayout.NORTH);
		makeSquadronText(squadId, squadDescriptionPanel);
		return squadDescriptionPanel;
	}

	private JPanel makeSquadronDescriptionGrid() throws PWCGException
	{
		Font fontMain = PWCGMonitorFonts.getPrimaryFont();

        JPanel descriptionGrid = new JPanel(new GridLayout(0,1));
        descriptionGrid.setOpaque(false);

        JLabel spaceLabel = new JLabel("     ");
        spaceLabel.setFont(fontMain);
        spaceLabel.setOpaque(false);
        descriptionGrid.add(spaceLabel);
        
        JLabel header = new JLabel("Squadron Information");
        header.setFont(fontMain);
        header.setOpaque(false);
        descriptionGrid.add(header);
		return descriptionGrid;
	}

	private void makeSquadronText(int squadId, JPanel squadDescriptionPanel) throws PWCGException
	{
		Font font = PWCGMonitorFonts.getPrimaryFontSmall();

		String squadronText = "";
		Squadron squadron =  PWCGContext.getInstance().getSquadronManager().getSquadron(squadId);
		if (squadron != null)
		{
			squadronText = squadron.determineSquadronDescription(mapDate);
		}

		Dimension screenSize = PWCGMonitorSupport.getPWCGFrameSize();
		int preferredPanelLength = screenSize.height - 100;

		JTextPane squadDesc = new JTextPane();
		squadDesc.setFont(font);
		squadDesc.setOpaque(false);
		squadDesc.setText(squadronText);
		squadDesc.setPreferredSize(new Dimension(300, preferredPanelLength));

		squadDescriptionPanel.add(squadDesc, BorderLayout.CENTER);
	}

    private JPanel makeMapCheckBoxes() throws PWCGException
    {
        JPanel mapPanel = new JPanel(new BorderLayout());
        mapPanel.setOpaque(false);
                
        JPanel mapGrid = new JPanel( new GridLayout(0,1));
        mapGrid.setOpaque(false);
        
        mapPanel.add(mapGrid, BorderLayout.NORTH);

        JLabel mapLabel = PWCGButtonFactory.makeMenuLabelLarge("Choose Map");
        mapGrid.add(mapLabel);
        
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            PWCGMap moscowMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.MOSCOW_MAP);
            if (moscowMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.MOSCOW_MAP_NAME, MAP_DELIMITER + PWCGMap.MOSCOW_MAP_NAME, mapButtonGroup));
            }
            
            PWCGMap stalingradMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.STALINGRAD_MAP);
            if (stalingradMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.STALINGRAD_MAP_NAME, MAP_DELIMITER + PWCGMap.STALINGRAD_MAP_NAME, mapButtonGroup));
            }
            
            PWCGMap kubanMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.KUBAN_MAP);
            if (kubanMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.KUBAN_MAP_NAME, MAP_DELIMITER + PWCGMap.KUBAN_MAP_NAME, mapButtonGroup));
            }
            
            PWCGMap east1944Map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.EAST1944_MAP);
            if (east1944Map.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.EAST1944_MAP_NAME, MAP_DELIMITER + PWCGMap.EAST1944_MAP_NAME, mapButtonGroup));
            }
            
            PWCGMap east1945Map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.EAST1945_MAP);
            if (east1945Map.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.EAST1945_MAP_NAME, MAP_DELIMITER + PWCGMap.EAST1945_MAP_NAME, mapButtonGroup));
            }
            
            PWCGMap bodenplatteMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.BODENPLATTE_MAP);
            if (bodenplatteMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.BODENPLATTE_MAP_NAME, MAP_DELIMITER + PWCGMap.BODENPLATTE_MAP_NAME, mapButtonGroup));
            }
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            PWCGMap arrasMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.ARRAS_MAP);
            if (arrasMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                mapGrid.add(makeRadioButton(PWCGMap.ARRAS_MAP_NAME, MAP_DELIMITER + PWCGMap.ARRAS_MAP_NAME, mapButtonGroup));
            }
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }
        
        return mapPanel;
    }

    private JRadioButton makeRadioButton(String buttonText, String commandString, ButtonGroup buttonGroup) throws PWCGException 
    {
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        JRadioButton button = new JRadioButton(buttonText);
        button.setHorizontalAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setFont(font);
        button.setActionCommand(commandString);

        buttonGroup.add(button);

        return button;
    }   

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{		
		try
		{
			String action = arg0.getActionCommand();
			if (action.contains("Finished"))
			{				
                CampaignGuiContextManager.getInstance().popFromContextStack();
			}	
            else if (action.startsWith(MAP_DELIMITER))
            {
                int indexOfMapName = MAP_DELIMITER.length();
                String mapName = action.substring(indexOfMapName);
                FrontMapIdentifier mapIdentifier = PWCGMap.getFrontMapIdentifierForName(mapName);
                PWCGContext.getInstance().changeContext(mapIdentifier);
                JPanel mapCenterPanel = createMapPanel();
                this.setCenterPanel(mapCenterPanel); 

                centerMapAt(null);
            }
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			
			try
			{
                CampaignGuiContextManager.getInstance().popFromContextStack();
			}
			catch (Exception e2)
			{
	            PWCGLogger.logException(e2);
			}
		}
	}
}
