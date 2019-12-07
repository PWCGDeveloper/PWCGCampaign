package pwcg.mission.ground.unittypes.infantry;

import java.io.BufferedWriter;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundElementFactory;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.org.IGroundElement;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.mcu.CoalitionFactory;
import pwcg.mission.mcu.McuFlare;
import pwcg.mission.mcu.group.FlareSequence;

public class GroundMachineGunFlareUnit extends GroundMachineGunUnit
{
    private FlareSequence flares = new FlareSequence();

    public GroundMachineGunFlareUnit(GroundUnitInformation pwcgGroundUnitInformation)
    {
        super(pwcgGroundUnitInformation);
    }   


    @Override
    protected int calcNumUnits() throws PWCGException
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 2);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

    @Override
    public void createGroundUnit() throws PWCGException
    {
        super.createGroundUnit();
        createFlares();
    }

    @Override
    protected void addElements()
    {
        IGroundElement areaFire = GroundElementFactory.createGroundElementDirectFire(pwcgGroundUnitInformation, vehicle);
        this.addGroundElement(areaFire);           
    }

    public void createFlares() throws PWCGException 
    {
        int flareColor = McuFlare.FLARE_COLOR_RED;        
        if (pwcgGroundUnitInformation.getCountry().getSide() == Side.ALLIED)
        {
            flareColor = McuFlare.FLARE_COLOR_GREEN;
        }
        
        Coalition friendlyCoalition  = CoalitionFactory.getFriendlyCoalition(pwcgGroundUnitInformation.getCountry());

        flares = new FlareSequence();
        flares.setFlare(friendlyCoalition, pwcgGroundUnitInformation.getPosition().copy(), flareColor, vehicle.getEntity().getIndex());
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {       
        super.write(writer);
        flares.write(writer);
    }

    public FlareSequence getFlares()
    {
        return flares;
    }
}	
