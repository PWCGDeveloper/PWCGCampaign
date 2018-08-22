package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.campaign.plane.Role;

public class LogGroundUnit extends LogAIEntity
{
    public LogGroundUnit(int sequenceNumber)
    {
        super(sequenceNumber);
        this.setRole(Role.ROLE_GROUND_UNIT);
    }
}
