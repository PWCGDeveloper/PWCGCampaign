package pwcg.gui.rofmap.intelmap;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.Dimension;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javafx.scene.control.ButtonGroup;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
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
import pwcg.gui.utils.ButtonFactory;

public class IntelMapGUI extends MapGUI implements ActionListener
{
    private static String MAP_DELIMITER = "Map: ";
	private static final long serialVersionUID = 1L;

    private ButtonGroup mapButtonGroup = new ButtonGroup();
    private Campaign campaign = null;
    private Pane rightPanel;

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

	        List<FrontMapIdentifier> airfieldMaps = AirfieldManager.getMapIdForAirfield(campaign.findReferencePlayer().determineSquadron().determineCurrentAirfieldName(campaign.getDate()));
            PWCGContext.getInstance().changeContext(airfieldMaps.get(0));
								
			this.add(BorderLayout.EAST, makeRightPanel(-1));
			this.add(BorderLayout.CENTER, createMapPanel());
			this.add(BorderLayout.WEST, makeNavigationPanel());		
			
	        centerMapAt(null);
		}
		catch (Exception e)
		{
			PWCGLogger.logException(e);
			ErrorDialog.internalError (e.getMessage());
		}
	}

    private Pane createMapPanel() throws PWCGException, PWCGException
    {
        Pane intelMapCenterPanel = new Pane(new BorderLayout());

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

    private Pane makeNavigationPanel() throws PWCGException  
    {
        Pane intelNavPanel = new Pane();
        intelNavPanel.setLayout(new BorderLayout());
        intelNavPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        Button finished = ButtonFactory.makeTranslucentMenuButton("Finished", "Finished", "Finished reading intel map", this);
        buttonPanel.add(finished);
        
        Label spacer1 = ButtonFactory.makeMenuLabelLarge("");
        buttonPanel.add(spacer1);

        Pane radioButtonPanel = new Pane( new GridLayout(0,1));
        radioButtonPanel.setOpaque(false);
        
        Label spacer2 = ButtonFactory.makeMenuLabelLarge("");
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
        if (rightPanel != null)
        {
            this.remove(rightPanel);
        }

        makeRightPanel(squadId);
        this.add(BorderLayout.EAST, rightPanel);
        CampaignGuiContextManager.getInstance().refreshCurrentContext(this);
        
        this.revalidate();
        this.repaint();
    }

    private Pane makeRightPanel(int squadId) throws PWCGException 
    {
        String imagePath = ContextSpecificImages.imagesMisc() + "PaperPart.jpg";
        Color bgColor = ColorMap.PAPER_BACKGROUND;
        rightPanel = new ImagePanelLayout(imagePath, new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBackground(bgColor); 

        Pane mapCheckBoxesPanel = makeMapCheckBoxes();
        rightPanel.add(mapCheckBoxesPanel, BorderLayout.NORTH);

        Pane squadronInfoPanel = makeInfoPanel(squadId);
        rightPanel.add(squadronInfoPanel, BorderLayout.CENTER);
        
        return rightPanel;
    }

	private Pane makeInfoPanel(int squadId) throws PWCGException 
	{
		Pane squadDescriptionPanel = new Pane(new BorderLayout());
		squadDescriptionPanel.setOpaque(false);
		
		Pane descriptionGrid = makeSquadronDescriptionGrid();
		squadDescriptionPanel.add(descriptionGrid, BorderLayout.NORTH);
		
		JTextPane squadDesc = makeIntelSquadronDescription(squadId);
		squadDescriptionPanel.add(squadDesc, BorderLayout.CENTER);
		
		return squadDescriptionPanel;
	}

	private Pane makeSquadronDescriptionGrid() throws PWCGException
	{
		Font fontMain = PWCGMonitorFonts.getPrimaryFont();

        Pane descriptionGrid = new Pane(new GridLayout(0,1));
        descriptionGrid.setOpaque(false);

        Label spaceLabel = new Label("     ");
        spaceLabel.setFont(fontMain);
        spaceLabel.setOpaque(false);
        descriptionGrid.add(spaceLabel);
        
        Label header = new Label("Squadron Information");
        header.setFont(fontMain);
        header.setOpaque(false);
        descriptionGrid.add(header);
		return descriptionGrid;
	}

	private JTextPane makeIntelSquadronDescription(int squadId) throws PWCGException
	{
		Font font = PWCGMonitorFonts.getTypewriterFont();

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
        return squadDesc;
	}

    private Pane makeMapCheckBoxes() throws PWCGException
    {
        Pane mapPanel = new Pane(new BorderLayout());
        mapPanel.setOpaque(false);
                
        Pane mapGrid = new Pane( new GridLayout(0,1));
        mapGrid.setOpaque(false);
        
        mapPanel.add(mapGrid, BorderLayout.NORTH);

        Label mapLabel = ButtonFactory.makeMenuLabelLarge("Choose Map");
        mapGrid.add(mapLabel);
        
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            PWCGMap moscowMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.MOSCOW_MAP);
            if (moscowMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.MOSCOW_MAP);
            }
            
            PWCGMap stalingradMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.STALINGRAD_MAP);
            if (stalingradMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.STALINGRAD_MAP);
            }
            
            PWCGMap kubanMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.KUBAN_MAP);
            if (kubanMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.KUBAN_MAP);
            }
            
            PWCGMap east1944Map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.EAST1944_MAP);
            if (east1944Map.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.EAST1944_MAP);
            }
            
            PWCGMap east1945Map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.EAST1945_MAP);
            if (east1945Map.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.EAST1945_MAP);
            }
            
            PWCGMap bodenplatteMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.BODENPLATTE_MAP);
            if (bodenplatteMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.BODENPLATTE_MAP);
            }
        }
        else if (PWCGContext.getProduct() == PWCGProduct.FC)
        {
            PWCGMap arrasMap = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.ARRAS_MAP);
            if (arrasMap.getFrontDatesForMap().isMapActive(campaign.getDate()))
            {
                addToMapGrid(mapGrid, FrontMapIdentifier.ARRAS_MAP);
            }
        }
        else
        {
            throw new PWCGException("No valid product selected");
        }
        
        return mapPanel;
    }

    private void addToMapGrid(Pane mapGrid, FrontMapIdentifier mapIdentifier) throws PWCGException
    {
        mapGrid.add(makeRadioButton(mapIdentifier.getMapName(), MAP_DELIMITER + mapIdentifier.getMapName(), mapButtonGroup));
    }

    private RadioButton  makeRadioButton(String buttonText, String commandString, ButtonGroup buttonGroup) throws PWCGException 
    {
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Color bgColor = ColorMap.PAPER_BACKGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button = new RadioButton (buttonText);
        button.setAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
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
                FrontMapIdentifier mapIdentifier = FrontMapIdentifier.getFrontMapIdentifierForName(mapName);
                PWCGContext.getInstance().changeContext(mapIdentifier);
                Pane mapCenterPanel = createMapPanel();
                this.add(BorderLayout.CENTER, mapCenterPanel); 

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
