package pwcg.gui.rofmap.brief;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.gui.colors.ColorMap;
import pwcg.gui.dialogs.PWCGMonitorFonts;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.utils.PWCGButtonFactory;
import pwcg.gui.utils.ScrollBarWrapper;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingMapSquadronSelector implements ActionListener
{
    private static final int NO_SQUADRONS = -2;
    private static final int ALL_SQUADRONS = -1;

    private Mission mission;
    private IBriefingSquadronSelectedCallback squadronsSelectedCallback;
    private Map<Integer, JCheckBox> squadronCheckBoxes = new HashMap<>();
    private Map<Integer, String> selectedSquadrons = new HashMap<>();
    private BriefingData briefingContext;

    public BriefingMapSquadronSelector(Mission mission, IBriefingSquadronSelectedCallback squadronsSelected,BriefingData briefingContext)
    {
        this.mission = mission;
        this.squadronsSelectedCallback = squadronsSelected;
        this.briefingContext = briefingContext;
    }

    public JPanel makeComboBox() throws PWCGException
    {
        JPanel squadronSelectorGrid = new JPanel(new GridLayout(0, 1));
        squadronSelectorGrid.setOpaque(false);

        addFlights(squadronSelectorGrid);
        JScrollPane squadronSelectorScroll = ScrollBarWrapper.makeScrollPane(squadronSelectorGrid);

        JPanel squadronSelectorPanel = new JPanel(new BorderLayout());
        squadronSelectorPanel.setOpaque(false);
        squadronSelectorPanel.add(squadronSelectorScroll, BorderLayout.NORTH);
        return squadronSelectorPanel;
    }

    private void addFlights(JPanel squadronSelectorGrid) throws PWCGException
    {
        IFlight selectedFlight = briefingContext.getSelectedFlight();
        Side selectedFlightSide = selectedFlight.getSquadron().determineSide();

        JButton checkBoxAll = PWCGButtonFactory.makeTranslucentMenuButton("All Squadrons", "" + ALL_SQUADRONS, "Show flight path for all squadrons", this);
        squadronSelectorGrid.add(checkBoxAll);

        JButton checkBoxNone = PWCGButtonFactory.makeTranslucentMenuButton("No Squadrons", "" + NO_SQUADRONS, "Show flight path for only your squadron", this);
        squadronSelectorGrid.add(checkBoxNone);

        for (IFlight aiflight : mission.getFlights().getAiFlights())
        {
            Company squadron = aiflight.getSquadron();
            Side squadronSide = squadron.getCountry().getSide();

            if (includeSquadron(selectedFlightSide, squadronSide))
            {
                JCheckBox checkBox = makeCheckBox(squadron.determineDisplayName(mission.getCampaign().getDate()), "" + squadron.getCompanyId());
                squadronCheckBoxes.put(squadron.getCompanyId(), checkBox);
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
    
    private JCheckBox makeCheckBox(String squadronName, String actionCommand) throws PWCGException
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(squadronName, actionCommand, font, fgColor, this);
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
                for (JCheckBox checkBox : squadronCheckBoxes.values())
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
                for (JCheckBox checkBox : squadronCheckBoxes.values())
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
        IFlight flight = mission.getFlights().getAiFlightForSquadron(squadronId);
        Company squadron = flight.getSquadron();
        selectedSquadrons.put(squadronId, squadron.determineDisplayName(mission.getCampaign().getDate()));
    }
}
