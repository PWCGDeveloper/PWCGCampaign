package pwcg.campaign.battle;

import java.util.ArrayList;
import java.util.List;

public class AmphibiousAssaults
{
    private List<AmphibiousAssault> amphibiousAssaults = new ArrayList<>();

    public List<AmphibiousAssault> getAmphibiousAssaults()
    {
        return amphibiousAssaults;
    }

    public void addAmphibiousAssault(AmphibiousAssault amphibiousAssault)
    {
        amphibiousAssaults.add(amphibiousAssault);
    }

    public AmphibiousAssault getAmphibiousAssault(String skirmishName)
    {
        for (AmphibiousAssault amphibiousAssault : amphibiousAssaults)
        {
            if (amphibiousAssault.getSkirmishName().equals(skirmishName))
            {
                return amphibiousAssault;
            }
        }
        return null;
    }
}
