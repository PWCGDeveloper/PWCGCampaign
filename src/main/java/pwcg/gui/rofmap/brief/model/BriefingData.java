package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.mission.Mission;
import pwcg.mission.playerunit.PlayerUnit;

public class BriefingData
{
    private String missionTime = "08:30";
    private Map<Integer, BriefingUnit> briefingMissionFlights = new HashMap<>();
    private int selectedCompanyId = 0;
    private Mission mission;
    private Map<Integer, String> aiFlightsToDisplay = new HashMap<>();

    public BriefingData(Mission mission, Map<Integer, BriefingUnit> briefingMissionFlights)
    {
        this.mission = mission;
        this.briefingMissionFlights = briefingMissionFlights;
    }

    public BriefingUnit getActiveBriefingUnit()
    {
        return briefingMissionFlights.get(selectedCompanyId);
    }
    
    public PlayerUnit getSelectedUnit()
    {
        PlayerUnit playerFlight = mission.getUnits().getPlayerUnitForCompany(selectedCompanyId);
        return playerFlight;
    }

    public List<BriefingUnit> getBriefingUnits()
    {
        return new ArrayList<>(briefingMissionFlights.values());
    }
    
    public void changeSelectedUnit(int companyId)
    {
        selectedCompanyId = companyId;
    }

    public Mission getMission()
    {
        return mission;
    }
    
    public String getMissionTime()
    {
        return missionTime;
    }

    public void setMissionTime(String selectedTime)
    {
        this.missionTime = selectedTime;
    }
    
    public void setAiFlightsToDisplay(Map<Integer, String> aiFlightsToDisplay)
    {
        this.aiFlightsToDisplay = aiFlightsToDisplay;
    }

    public void clearAiFlightsToDisplay()
    {
        aiFlightsToDisplay.clear();
    }

    public Map<Integer, String> getAiFlightsToDisplay()
    {
        return aiFlightsToDisplay;
    }
}
