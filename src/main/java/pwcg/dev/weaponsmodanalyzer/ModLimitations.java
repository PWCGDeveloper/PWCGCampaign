package pwcg.dev.weaponsmodanalyzer;

import java.util.ArrayList;
import java.util.List;

public class  ModLimitations
{
    List<ModLimitation> modLimitations = new ArrayList<>();
    
    public void print()
    {
        for (ModLimitation modLimitation: modLimitations)
        {
            modLimitation.print();
        }
    }    
}
