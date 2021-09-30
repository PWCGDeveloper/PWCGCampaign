package pwcg.dev.weaponsmodanalyzer;

import java.util.ArrayList;
import java.util.List;

public class  ModLimitation
{
    String plane;
    List<ModLimitationPeriod> modLimitationPeriods = new ArrayList<>();
    
    public void print()
    {
        System.out.println(plane);
        for (ModLimitationPeriod period: modLimitationPeriods)
        {
            period.print();
        }
    }    
}
