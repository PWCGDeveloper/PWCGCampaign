package pwcg.gui.rofmap.debrief;

import java.util.Map;

import pwcg.aar.AARCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.core.exception.PWCGException;

public class AARSubmitter
{

    public String submitAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        AARCoordinator.getInstance().submitAAR(playerDeclarations);
        String aarError = AARCoordinator.getInstance().getErrorBundleFileName();
        return aarError;
    }
}
