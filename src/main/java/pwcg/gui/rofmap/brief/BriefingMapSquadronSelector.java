package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import javafx.scene.paint.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javax.swing.CheckBox;
import javafx.scene.layout.Pane;
import javax.swing.JScrollPane;

import pwcg.campaign.api.Side;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.utils.ButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingMapSquadronSelector implements ActionListener
{
    private static final int NO_SQUADRONS = -2;
    private static final int ALL_SQUADRONS = -1;

    private Mission mission;
    private IBriefingSquadronSelectedCallback squadronsSelectedCallback;
    private Map<Integer, CheckBox> squadronCheckBoxes = new HashMap<>();
    private Map<Integer, String> selectedSquadrons = new HashMap<>();
    private BriefingData briefingContext;

    public BriefingMapSquadronSelector(Mission mission, IBriefingSquadronSelectedCallback squadronsSelected,BriefingData briefingContext)
    {
        this.mission = mission;
        this.squadronsSelectedCallback = squadronsSelected;
        this.briefingContext = briefingContext;
    }

    public Pane makeComboBox() throws PWCGException
    {
        Pane squadronSelectorGrid = new Pane(new GridLayout(0, 1));
        squadronSelectorGrid.setOpaque(false);

        addFlights(squadronSelectorGrid);
        JScrollPane squadronSelectorScroll = ScrollBarWrapper.makeScrollPane(squadronSelectorGrid);

        Pane squadronSelectorPanel = new Pane(new BorderLayout());
        squadronSelectorPanel.setOpaque(false);
        squadronSelectorPanel.add(squadronSelectorScroll, BorderLayout.NORTH);
        return squadronSelectorPanel;
    }

    private void addFlights(Pane squadronSelectorGrid) throws PWCGException
    {
        IFlight selectedFlight = briefingContext.getSelectedFlight();
        Side selectedFlightSide = selectedFlight.getSquadron().determineSide();

        Button checkBoxAll = ButtonFactory.makeTranslucentMenuButton("All Squadrons", "" + ALL_SQUADRONS, "Show flight path for all squadrons", this);
        squadronSelectorGrid.add(checkBoxAll);

        Button checkBoxNone = ButtonFactory.makeTranslucentMenuButton("No Squadrons", "" + NO_SQUADRONS, "Show flight path for only your squadron", this);
        squadronSelectorGrid.add(checkBoxNone);

        for (IFlight aiflight : mission.getMissionFlights().getAiFlights())
        {
            Squadron squadron = aiflight.getSquadron();
            Side squadronSide = squadron.getCountry().getSide();

            if (includeSquadron(selectedFlightSide, squadronSide))
            {
                CheckBox checkBox = makeCheckBox(squadron.determineDisplayName(mission.getCampaign().getDate()), "" + squadron.getSquadronId());
                squadronCheckBoxes.put(squadron.getSquadronId(), checkBox);
                squadronSelectorGrid.add(checkBox);
            }
        }
    }

    private boolean includeSquadron(Side playerFlightSide, Side squadronSide) throws PWCGException
    {
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        int showAllFlightsInBreifingKey = configManager.getIntConfigParam(ConfigItemKeys.ShowAllFlightsInBreifingKey);
        if (showAllFlightsInBreifingKey == 1)
        {
            return true;
        }
        
        if (playerFlightSide == squadronSide)
        {
            return true;
        }
        
        return false;
    }
    
    private CheckBox makeCheckBox(String squadronName, String actionCommand) throws PWCGException
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        CheckBox checkBox = ButtonFactory.makeCheckBox(squadronName, actionCommand, fgColor, this);
        return checkBox;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            int squadronId = Integer.parseInt(ae.getActionCommand());
            if (squadronId == ALL_SQUADRONS)
            {
                for (CheckBox checkBox : squadronCheckBoxes.values())
                {
                    checkBox.setSelected(true);
                }

                selectedSquadrons.clear();
                for (int squadronIdFromCheckBox : squadronCheckBoxes.keySet())
                {
                    addSquadronToSelected(squadronIdFromCheckBox);
                }
            }
            else if (squadronId == NO_SQUADRONS)
            {
                selectedSquadrons.clear();
                for (CheckBox checkBox : squadronCheckBoxes.values())
                {
                    checkBox.setSelected(false);
                }
            }
            else if (selectedSquadrons.containsKey(squadronId))
            {
                selectedSquadrons.remove(squadronId);
            }
            else
            {
                addSquadronToSelected(squadronId);
            }
            squadronsSelectedCallback.squadronsSelectedChanged(selectedSquadrons);
        }
        catch (PWCGException e)
        {
        }
    }

    private void addSquadronToSelected(int squadronId) throws PWCGException
    {
        IFlight flight = mission.getMissionFlights().getAiFlightForSquadron(squadronId);
        Squadron squadron = flight.getSquadron();
        selectedSquadrons.put(squadronId, squadron.determineDisplayName(mission.getCampaign().getDate()));
    }
}
