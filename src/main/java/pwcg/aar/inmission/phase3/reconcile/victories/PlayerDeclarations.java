package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeclarations
{
    private List<PlayerVictoryDeclaration> playerDeclarations = new ArrayList<>();
    
    public void addPlayerDeclaration (PlayerVictoryDeclaration victoryDeclaration)
    {
        playerDeclarations.add(victoryDeclaration);
    }

    public List<PlayerVictoryDeclaration> getPlayerDeclarations()
    {
        return playerDeclarations;
    }
}
