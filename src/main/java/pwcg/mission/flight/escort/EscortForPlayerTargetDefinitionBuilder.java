package pwcg.mission.flight.escort;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class EscortForPlayerTargetDefinitionBuilder
{

    public static TargetDefinition buildEscortForPlayerTargetDefinition(
            IFlightInformation escortFlightInformation, 
            Coordinate playerRendezvous) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(escortFlightInformation);
        TargetDefinition targetDefinition  = targetDefinitionBuilder.buildTargetDefinition();
        Coordinate escortRendezvous = playerRendezvous.copy();
        escortRendezvous.setYPos(escortRendezvous.getYPos() + 500.0);
        return targetDefinition;
    }

}
