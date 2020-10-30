package pwcg.aar.phase4.outofmission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadmember.Victory;

public class VictoriesBySquadronMember
{
    private Integer pilotSerialNumber;
    private List<Victory> victories = new ArrayList<>();
    
    public VictoriesBySquadronMember (int pilotSerialNumber)
    {
        this.pilotSerialNumber = pilotSerialNumber;
    }
    
    public void addVictory(Victory victory)
    {
        victories.add(victory);
    }
    
    public void addVictories(List<Victory> newVictories)
    {
        victories.addAll(newVictories);
    }

    public int getPilotName()
    {
        return pilotSerialNumber;
    }

    public List<Victory> getVictories()
    {
        return victories;
    }
}
