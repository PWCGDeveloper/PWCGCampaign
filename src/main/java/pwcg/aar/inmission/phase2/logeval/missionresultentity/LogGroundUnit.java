package pwcg.aar.inmission.phase2.logeval.missionresultentity;

import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.campaign.plane.PwcgRole;
import pwcg.core.exception.PWCGException;

public class LogGroundUnit extends LogAIEntity
{
    private LogTurrets turrets = new LogTurrets();

    public LogGroundUnit(int sequenceNumber)
    {
        super(sequenceNumber);
        this.setRole(PwcgRole.ROLE_GROUND_UNIT);
    }

    public LogTurret createTurret(IAType12 atype12) throws PWCGException
    {
        return turrets.createTurret(atype12, this);
    }

    public boolean ownsTurret(String turretId) throws PWCGException
    {
        return turrets.hasTurret(turretId);
    }
}
