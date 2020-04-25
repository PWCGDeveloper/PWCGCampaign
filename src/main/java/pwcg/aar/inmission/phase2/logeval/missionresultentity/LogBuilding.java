package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.campaign.plane.Role;
import pwcg.core.exception.PWCGException;

public class LogBuilding extends LogAIEntity
{
    public LogBuilding(int sequenceNumber)
    {
        super(sequenceNumber);
        this.setRole(Role.ROLE_GROUND_UNIT);
    }

    @Override
    public void initializeEntityFromEvent(IAType12 atype12) throws PWCGException {
        super.initializeEntityFromEvent(atype12);

        String type = atype12.getType();
        this.setName(type);
        this.setVehicleType(type.substring(0, type.indexOf("[")));
    }
}
