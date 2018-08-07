package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

public class PlayerDeclarations
{
    private List<PlayerVictoryDeclaration> declarations = new ArrayList<>();
    
    public void addDeclaration (PlayerVictoryDeclaration victoryDeclaration)
    {
        declarations.add(victoryDeclaration);
    }

    public List<PlayerVictoryDeclaration> getDeclarations()
    {
        return declarations;
    }
}
