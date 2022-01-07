package pwcg.mission.playerunit.payload.tank;

import java.util.Date;

import pwcg.campaign.tank.TankType;
import pwcg.campaign.tank.payload.ITankPayload;
import pwcg.campaign.tank.payload.TankPayload;
import pwcg.campaign.tank.payload.TankPayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.PlayerUnit;

public class Sdkfz184 extends TankPayload implements ITankPayload
{
    public Sdkfz184(TankType tankType, Date date)
    {
        super(tankType, date);
    }

    protected void initialize()
	{        
        setAvailablePayload(0, "1", TankPayloadElement.STANDARD);
	}
      
    @Override
    public ITankPayload copy()
    {
        Sdkfz184 clone = new Sdkfz184(getTankType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForTank(PlayerUnit unit) throws PWCGException
    {
        return 0;
    }
}
