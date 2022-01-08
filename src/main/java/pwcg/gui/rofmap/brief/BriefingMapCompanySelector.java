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
import pwcg.mission.playerunit.PlayerUnit;

public class BriefingMapCompanySelector implements ActionListener
{
    private static final int NO_SQUADRONS = -2;
    private static final int ALL_SQUADRONS = -1;

    private Mission mission;
    private IBriefingCompanySelectedCallback companysSelectedCallback;
    private Map<Integer, JCheckBox> companyCheckBoxes = new HashMap<>();
    private Map<Integer, String> selectedSquadrons = new HashMap<>();
    private BriefingData briefingContext;

    public BriefingMapCompanySelector(Mission mission, IBriefingCompanySelectedCallback companysSelected,BriefingData briefingContext)
    {
        this.mission = mission;
        this.companysSelectedCallback = companysSelected;
        this.briefingContext = briefingContext;
    }

    public JPanel makeComboBox() throws PWCGException
    {
        JPanel companySelectorGrid = new JPanel(new GridLayout(0, 1));
        companySelectorGrid.setOpaque(false);

        addFlights(companySelectorGrid);
        JScrollPane companySelectorScroll = ScrollBarWrapper.makeScrollPane(companySelectorGrid);

        JPanel companySelectorPanel = new JPanel(new BorderLayout());
        companySelectorPanel.setOpaque(false);
        companySelectorPanel.add(companySelectorScroll, BorderLayout.NORTH);
        return companySelectorPanel;
    }

    private void addFlights(JPanel companySelectorGrid) throws PWCGException
    {
        PlayerUnit selectedFlight = briefingContext.getSelectedUnit();
        Side selectedFlightSide = selectedFlight.getCompany().determineSide();

        JButton checkBoxAll = PWCGButtonFactory.makeTranslucentMenuButton("All Squadrons", "" + ALL_SQUADRONS, "Show unit path for all companys", this);
        companySelectorGrid.add(checkBoxAll);

        JButton checkBoxNone = PWCGButtonFactory.makeTranslucentMenuButton("No Squadrons", "" + NO_SQUADRONS, "Show unit path for only your company", this);
        companySelectorGrid.add(checkBoxNone);

        for (PlayerUnit aiunit : mission.getUnits().getAiUnits())
        {
            Company company = aiunit.getCompany();
            Side companySide = company.getCountry().getSide();

            if (includeSquadron(selectedFlightSide, companySide))
            {
                JCheckBox checkBox = makeCheckBox(company.determineDisplayName(mission.getCampaign().getDate()), "" + company.getCompanyId());
                companyCheckBoxes.put(company.getCompanyId(), checkBox);
                companySelectorGrid.add(checkBox);
            }
        }
    }

    private boolean includeSquadron(Side playerFlightSide, Side companySide) throws PWCGException
    {
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        int showAllUnitsInBreifing = configManager.getIntConfigParam(ConfigItemKeys.ShowAllUnitsInBreifingKey);
        if (showAllUnitsInBreifing == 1)
        {
            return true;
        }
        
        if (playerFlightSide == companySide)
        {
            return true;
        }
        
        return false;
    }
    
    private JCheckBox makeCheckBox(String companyName, String actionCommand) throws PWCGException
    {
        Color fgColor = ColorMap.CHALK_FOREGROUND;
        Font font = PWCGMonitorFonts.getPrimaryFont();
        JCheckBox checkBox = PWCGButtonFactory.makeCheckBox(companyName, actionCommand, font, fgColor, this);
        return checkBox;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            int companyId = Integer.parseInt(ae.getActionCommand());
            if (companyId == ALL_SQUADRONS)
            {
                for (JCheckBox checkBox : companyCheckBoxes.values())
                {
                    checkBox.setSelected(true);
                }

                selectedSquadrons.clear();
                for (int companyIdFromCheckBox : companyCheckBoxes.keySet())
                {
                    addSquadronToSelected(companyIdFromCheckBox);
                }
            }
            else if (companyId == NO_SQUADRONS)
            {
                selectedSquadrons.clear();
                for (JCheckBox checkBox : companyCheckBoxes.values())
                {
                    checkBox.setSelected(false);
                }
            }
            else if (selectedSquadrons.containsKey(companyId))
            {
                selectedSquadrons.remove(companyId);
            }
            else
            {
                addSquadronToSelected(companyId);
            }
            companysSelectedCallback.companiesSelectedChanged(selectedSquadrons);
        }
        catch (PWCGException e)
        {
        }
    }

    private void addSquadronToSelected(int companyId) throws PWCGException
    {
        PlayerUnit unit = mission.getUnits().getPlayerUnitForCompany(companyId);
        Company company = unit.getCompany();
        selectedSquadrons.put(companyId, company.determineDisplayName(mission.getCampaign().getDate()));
    }
}
