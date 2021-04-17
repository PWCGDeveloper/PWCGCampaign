package pwcg.mission.ground.builder;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.org.GroundUnitCollection;

public interface IBattleBuilder
{

    List<GroundUnitCollection> generateBattle() throws PWCGException;

}