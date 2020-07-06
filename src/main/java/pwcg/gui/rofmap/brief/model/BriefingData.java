package pwcg.gui.rofmap.brief.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingData
{
    private String selectedTime = "08:30";
    private Map<Integer, BriefingFlight> briefingMissionFlights = new HashMap<>();
    private int selectedSquadronId = 0;
    private Mission mission;
    private Map<Integer, String> aiFlightsToDisplay = new HashMap<>();

    public BriefingData(Mission mission, Map<Integer, BriefingFlight> briefingMissionFlights)
    {
        this.mission = mission;
        this.briefingMissionFlights = briefingMissionFlights;
    }

    public BriefingFlight getActiveBriefingFlight()
    {
        return briefingMissionFlights.get(selectedSquadronId);
    }
    
    public IFlight getSelectedFlight()
    {
        IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(selectedSquadronId);
        return playerFlight;
    }

    public List<BriefingFlight> getBriefingFlights()
    {
        return new ArrayList<>(briefingMissionFlights.values());
    }
    
    public void changeSelectedFlight(int squadronId)
    {
        selectedSquadronId = squadronId;
    }

    public Mission getMission()
    {
        return mission;
    }
    
    public String getSelectedTime()
    {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime)
    {
        this.selectedTime = selectedTime;
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
