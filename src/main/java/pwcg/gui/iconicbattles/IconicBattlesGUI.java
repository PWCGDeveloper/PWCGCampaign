package pwcg.gui.iconicbattles;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javafx.scene.control.ButtonGroup;
import javafx.scene.layout.Pane;
import javax.swing.RadioButton ;
import javax.swing.SwingConstants;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.skirmish.IconicMissionsManager;
import pwcg.campaign.skirmish.IconicSingleMission;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.ButtonFactory;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.ground.vehicle.VehicleDefinition;

public class IconicBattlesGUI extends ImageResizingPanel implements ActionListener
{
    public static final int AXIS_STAND_IN_SQUADRON_ID = 999999999;
    public static final int ALLIED_STAND_IN_SQUADRON_ID = 999999998;
    private static final long serialVersionUID = 1L;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private IconicBattlesGeneratorData iconicBattleData = new IconicBattlesGeneratorData();

	public IconicBattlesGUI(String iconicBattleKey) 
	{		
        super("");
        
        iconicBattleData.setIconicBattleKey(iconicBattleKey);
        
        setLayout(new BorderLayout());
	}

	public void makeGUI() throws PWCGException 
	{
        String imagePath = UiImageResolver.getImage(ScreenIdentifier.Document);
        this.setImageFromName(imagePath);
        this.setBorder(BorderFactory.createEmptyBorder(50,50,50,100));
        
        Pane squadronPanel = makeSquadronPanel();
        this.add(squadronPanel, BorderLayout.CENTER);
	}

    public Pane makeSquadronPanel() throws PWCGException  
    {
        Pane configPanel = new Pane(new BorderLayout());
        configPanel.setOpaque(false);

        Pane buttonPanel = new Pane(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(ButtonFactory.makePaperLabelLarge("Iconic Mission Squadrons:"));
        buttonPanel.add(ButtonFactory.makePaperLabelLarge("   "));

        IconicSingleMission iconicMission = IconicMissionsManager.getInstance().getSelectedMissionProfile(iconicBattleData.getIconicBattleKey());
        
        Set<Country> countriesInBattle = new HashSet<>();
        for (Integer squadronId : iconicMission.getIconicBattleParticipants())
        {
            String description = formDescription(squadronId);
            buttonPanel.add(makeCategoryRadioButton(description, squadronId));
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            countriesInBattle.add(squadron.getCountry().getCountry());
        }
        
        buttonPanel.add(ButtonFactory.makePaperLabelLarge("   "));
        buttonPanel.add(ButtonFactory.makePaperLabelLarge("Iconic Mission Vehicles:"));
        buttonPanel.add(ButtonFactory.makePaperLabelLarge("   "));

        Date battleDate = DateUtils.getDateYYYYMMDD(iconicMission.getDateString());        
        List<VehicleDefinition> matchingTrucks = getVehicleDefinitionsOfType(VehicleClass.TruckAAAPlayer, countriesInBattle, battleDate);
        addVehicleRadioButtons(buttonPanel, matchingTrucks);

        List<VehicleDefinition> matchingTanks = getVehicleDefinitionsOfType(VehicleClass.TankPlayer, countriesInBattle, battleDate);
        addVehicleRadioButtons(buttonPanel, matchingTanks);
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private void addVehicleRadioButtons(Pane buttonPanel, List<VehicleDefinition> matchingVehicles) throws PWCGException
    {
        for (VehicleDefinition matchingVehicle : matchingVehicles)
        {
            ICountry country = CountryFactory.makeCountryByCountry(matchingVehicle.getCountries().get(0));
            if (country.getSide() == Side.ALLIED)
            {
                buttonPanel.add(makeCategoryRadioButton(matchingVehicle.getVehicleName(), ALLIED_STAND_IN_SQUADRON_ID));
            }
            else
            {
                buttonPanel.add(makeCategoryRadioButton(matchingVehicle.getVehicleName(), AXIS_STAND_IN_SQUADRON_ID));
            }
        }
    }
    
    List<VehicleDefinition> getVehicleDefinitionsOfType(VehicleClass vehicleClass, Set<Country> countriesInBattle, Date battleDate) throws PWCGException
    {
        return PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionsOfTypeBySide(vehicleClass, countriesInBattle, battleDate);
    }

    private String formDescription(Integer squadronId) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        Date iconicBattleDate = DateUtils.getDateYYYYMMDD(iconicBattleData.getIconicBattleKey());
        String description = squadron.determineDisplayName(iconicBattleDate) + " flying " + squadron.determineBestPlane(iconicBattleDate).getDisplayName();
        return description;
    }

    private RadioButton  makeCategoryRadioButton(String buttonText, Integer squadronId) throws PWCGException 
    {
        Color fgColor = ColorMap.PAPER_FOREGROUND;

        Font font = PWCGMonitorFonts.getPrimaryFont();

        RadioButton  button = new RadioButton (buttonText);
        button.setActionCommand(buttonText + ":" + squadronId);
        button.setAlignment(SwingConstants.LEFT );
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(this);
        button.setOpaque(false);
        button.setForeground(fgColor);
        button.setFont(font);

        buttonGroup.add(button);

        return button;
    }


    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String action = ae.getActionCommand();
            {
                String[] parts = action.split(":");
                iconicBattleData.setSelectedVehicleOrSquadron(parts[0]);
                iconicBattleData.setSelectedSquadron(Integer.valueOf(parts[1]));
            }
        }
        catch (Throwable e)
        {
            PWCGLogger.logException(e);
            ErrorDialog.internalError(e.getMessage());
        }
    }

    public IconicBattlesGeneratorData getIconicBattleData()
    {
        return iconicBattleData;
    }
}
