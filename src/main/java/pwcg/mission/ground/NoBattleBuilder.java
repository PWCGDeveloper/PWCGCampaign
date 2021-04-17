package pwcg.mission.ground;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.builder.IBattleBuilder;
import pwcg.mission.ground.org.GroundUnitCollection;

public class NoBattleBuilder implements IBattleBuilder
{
    @Override
    public List<GroundUnitCollection> generateBattle() throws PWCGException
    {
        return new ArrayList<GroundUnitCollection>();
    }
}
