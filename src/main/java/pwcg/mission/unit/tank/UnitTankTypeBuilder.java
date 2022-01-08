package pwcg.mission.unit.tank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pwcg.campaign.tank.Equipment;
import pwcg.campaign.tank.EquippedTank;
import pwcg.campaign.tank.TankSorter;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class UnitTankTypeBuilder
{
    private int numTanks;
    private Equipment equipmentForCompany;
    
    public UnitTankTypeBuilder(Equipment equipmentForSquadron, int numTanks)
    {
        this.equipmentForCompany = equipmentForSquadron;
        this.numTanks = numTanks;
    }
    
    public List<EquippedTank> getTankListForFlight() throws PWCGException 
    { 
        setNumberOfTanksForLosses();        
        Set<Integer> selectedTankSerialNumbers = selectTankSerialNumbersForFlight();
        List<EquippedTank> selectedTanks = getSelectedTanks(selectedTankSerialNumbers);
        List<EquippedTank> sortedTanks = sortSelectedTanks(selectedTanks);
        return sortedTanks;
    }

    private void setNumberOfTanksForLosses()
    {
        if (equipmentForCompany.getActiveEquippedTanks().size() < numTanks)
        {
            numTanks = equipmentForCompany.getActiveEquippedTanks().size();
        }
    }

    private Set<Integer> selectTankSerialNumbersForFlight()
    {
        List<EquippedTank> equippedTanks = new ArrayList<>(equipmentForCompany.getActiveEquippedTanks().values());
        Set<Integer> selectedTankSerialNumbers = new HashSet<>();
        while (selectedTankSerialNumbers.size() < numTanks)
        {
            int index = RandomNumberGenerator.getRandom(equippedTanks.size());
            EquippedTank selectedTank = equippedTanks.get(index);
            selectedTankSerialNumbers.add(selectedTank.getSerialNumber());
        }
        return selectedTankSerialNumbers;
    }

    private List<EquippedTank> getSelectedTanks(Set<Integer> selectedTankSerialNumbers)
    {
        List<EquippedTank> selectedTanks = new ArrayList<>();
        for (Integer planeSerialNumber : selectedTankSerialNumbers)
        {
            EquippedTank selectedTank = equipmentForCompany.getActiveEquippedTanks().get(planeSerialNumber);
            selectedTanks.add(selectedTank);
        }
        return selectedTanks;
    }

    private List<EquippedTank> sortSelectedTanks(List<EquippedTank> selectedTanks) throws PWCGException
    {
        List<EquippedTank> sortedTanks = TankSorter.sortEquippedTanksByGoodness(selectedTanks);
        return sortedTanks;
    }
}
