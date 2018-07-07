package pwcg.mission.flight.plane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneSorter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class FlightPlaneTypeBuilder
{
    private int numPlanes;
    private Equipment equipmentForSquadron;
    
    public FlightPlaneTypeBuilder(Equipment equipmentForSquadron, int numPlanes)
    {
        this.equipmentForSquadron = equipmentForSquadron;
        this.numPlanes = numPlanes;
    }
    
    public List<EquippedPlane> getPlaneListForFlight() throws PWCGException 
    { 
        setNumberOfPlanesForLosses();        
        Set<Integer> selectedPlaneSerialNumbers = selectPlaneSerialNumbersForFlight();
        List<EquippedPlane> selectedPlanes = getSelectedPlanes(selectedPlaneSerialNumbers);
        List<EquippedPlane> sortedPlanes = sortSelectedPlanes(selectedPlanes);
        return sortedPlanes;
    }

    private void setNumberOfPlanesForLosses()
    {
        if (equipmentForSquadron.getActiveEquippedPlanes().size() < numPlanes)
        {
            numPlanes = equipmentForSquadron.getActiveEquippedPlanes().size();
        }
    }

    private Set<Integer> selectPlaneSerialNumbersForFlight()
    {
        List<EquippedPlane> equippedPlanes = new ArrayList<>(equipmentForSquadron.getActiveEquippedPlanes().values());
        Set<Integer> selectedPlaneSerialNumbers = new HashSet<>();
        while (selectedPlaneSerialNumbers.size() < numPlanes)
        {
            int index = RandomNumberGenerator.getRandom(equippedPlanes.size());
            EquippedPlane selectedPlane = equippedPlanes.get(index);
            selectedPlaneSerialNumbers.add(selectedPlane.getSerialNumber());
        }
        return selectedPlaneSerialNumbers;
    }

    private List<EquippedPlane> getSelectedPlanes(Set<Integer> selectedPlaneSerialNumbers)
    {
        List<EquippedPlane> selectedPlanes = new ArrayList<>();
        for (Integer planeSerialNumber : selectedPlaneSerialNumbers)
        {
            EquippedPlane selectedPlane = equipmentForSquadron.getActiveEquippedPlanes().get(planeSerialNumber);
            selectedPlanes.add(selectedPlane);
        }
        return selectedPlanes;
    }

    private List<EquippedPlane> sortSelectedPlanes(List<EquippedPlane> selectedPlanes) throws PWCGException
    {
        List<EquippedPlane> sortedPlanes = PlaneSorter.sortEquippedPlanesByGoodness(selectedPlanes);
        return sortedPlanes;
    }
}
