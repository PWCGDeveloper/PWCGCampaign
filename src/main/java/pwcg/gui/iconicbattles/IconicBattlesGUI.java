package pwcg.gui.iconicbattles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.skirmish.IconicMissionsManager;
import pwcg.campaign.skirmish.IconicSingleMission;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.gui.ScreenIdentifier;
import pwcg.gui.UiImageResolver;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.ErrorDialog;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.utils.ImageResizingPanel;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.PWCGLabelFactory;
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
        
        JPanel squadronPanel = makeSquadronPanel();
        this.add(squadronPanel, BorderLayout.CENTER);
	}

    public JPanel makeSquadronPanel() throws PWCGException  
    {
        JPanel configPanel = new JPanel(new BorderLayout());
        configPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(new GridLayout(0,1));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(PWCGLabelFactory.makePaperLabelLarge("Iconic Mission Squadrons"));
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        IconicSingleMission iconicMission = IconicMissionsManager.getInstance().getSelectedMissionProfile(iconicBattleData.getIconicBattleKey());
        
        Set<Country> countriesInBattle = new HashSet<>();
        for (Integer squadronId : iconicMission.getIconicBattleParticipants())
        {
            String description = formDescription(squadronId);            
            String commandText = description + ":" + squadronId;
            buttonPanel.add(makeCategoryRadioButton(description, commandText));
            Company squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
            countriesInBattle.add(squadron.getCountry().getCountry());
        }
        
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());
        buttonPanel.add(PWCGLabelFactory.makePaperLabelLarge("Iconic Mission Vehicles"));
        buttonPanel.add(PWCGLabelFactory.makeDummyLabel());

        Date battleDate = DateUtils.getDateYYYYMMDD(iconicMission.getDateString());        
        List<VehicleDefinition> matchingTrucks = getPlayerVehicleDefinitionsOfType(VehicleClass.TruckAAA, countriesInBattle, battleDate);
        addVehicleRadioButtons(buttonPanel, matchingTrucks);

        List<VehicleDefinition> matchingTanks = getPlayerVehicleDefinitionsOfType(VehicleClass.Tank, countriesInBattle, battleDate);
        addVehicleRadioButtons(buttonPanel, matchingTanks);
        
        add (buttonPanel);

        configPanel.add(buttonPanel, BorderLayout.NORTH);
        
        return configPanel;
    }

    private void addVehicleRadioButtons(JPanel buttonPanel, List<VehicleDefinition> matchingVehicles) throws PWCGException
    {
        for (VehicleDefinition matchingVehicle : matchingVehicles)
        {                    
            ICountry country = CountryFactory.makeCountryByCountry(matchingVehicle.getCountries().get(0));
            if (country.getSide() == Side.ALLIED)
            {
                String commandText = matchingVehicle.getVehicleName() + ":" + ALLIED_STAND_IN_SQUADRON_ID;
                buttonPanel.add(makeCategoryRadioButton(matchingVehicle.getVehicleName(), commandText));
            }
            else
            {
                String commandText = matchingVehicle.getVehicleName() + ":" + AXIS_STAND_IN_SQUADRON_ID;
                buttonPanel.add(makeCategoryRadioButton(matchingVehicle.getVehicleName(), commandText));
            }
        }
    }
    
    private List<VehicleDefinition> getPlayerVehicleDefinitionsOfType(VehicleClass vehicleClass, Set<Country> countriesInBattle, Date battleDate) throws PWCGException
    {
        return PWCGContext.getInstance().getVehicleDefinitionManager().getPlayerVehicleDefinitionsOfTypeForCountries(vehicleClass, countriesInBattle, battleDate);
    }

    private String formDescription(Integer squadronId) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        Date iconicBattleDate = DateUtils.getDateYYYYMMDD(iconicBattleData.getIconicBattleKey());
        String description = squadron.determineDisplayName(iconicBattleDate) + " flying " + squadron.determineBestPlane(iconicBattleDate).getDisplayName();
        return description;
    }

    private JRadioButton makeCategoryRadioButton(String buttonText, String commandText) throws PWCGException 
    {
        Color fgColor = ColorMap.PAPER_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JRadioButton button = PWCGButtonFactory.makeRadioButton(buttonText, commandText, "", font, fgColor, false, this);
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
