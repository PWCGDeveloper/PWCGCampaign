package pwcg.mission.ground.vehicle;

import pwcg.core.exception.PWCGException;

public interface IVehicleDefinition
{
    String getScriptDir();

    String getModelDir();

    String getVehicleType();

    int getRarityWeight();

    String getDisplayName();

    String getAssociatedBlock();

    boolean shouldUse(VehicleRequestDefinition requestDefinition) throws PWCGException;

}