package pwcg.mission.flight.artySpot;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.product.rof.ground.vehicle.Artillery;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionStringHandler;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuDeactivate;
import pwcg.mission.mcu.McuSubtitle;
import pwcg.mission.mcu.McuTimer;

public class ArtillerySpotArtilleryGroup extends GroundUnit
{	
	protected ArrayList<Artillery> arty = new ArrayList<Artillery>();

	protected McuTimer activateTimer = new McuTimer();
	protected McuActivate activate = new McuActivate();
	protected McuTimer deactivateTimer = new McuTimer();
	protected McuDeactivate deactivate = new McuDeactivate();
    protected Campaign campaign;
    protected Coordinate targetPosition;

    private List<McuSubtitle> subTitleList = new ArrayList<McuSubtitle>();

    
	public ArtillerySpotArtilleryGroup (Campaign campaign, GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super(pwcgGroundUnitInformation);
	    pwcgGroundUnitInformation.setTargetType(TacticalTarget.TARGET_ARTILLERY);
		
		this.campaign = campaign;
	}

	public List<IVehicle> getVehicles() 
	{
		List<IVehicle> vehicles = new ArrayList<IVehicle>();
		vehicles.addAll(arty);
		return vehicles;
	}

    public int getLeadIndex() 
    {
        return arty.get(0).getEntity().getIndex();
    }

	@Override
	public void createUnitMission() throws PWCGException  
	{
		createArtillery();
		createActivation();
		createGroundTargetAssociations();
	}

    protected void createArtillery() throws PWCGException 
    {
        Artillery gunType = new Artillery();
        gunType.makeRandomVehicleFromSet(pwcgGroundUnitInformation.getCountry());

        int numArtillery = calcNumUnits();
                
        for (int i = 0; i < numArtillery; ++i)
        {
            Artillery gun = gunType.copy();
            gun.getEntity().setEnabled(1);

            Coordinate position = getRandomPosition();
            gun.setPosition(position);

            gun.setOrientation(pwcgGroundUnitInformation.getOrientation().copy());
            
            arty.add(gun);
            
            if (i > 0)
            {
                gun.getEntity().setTarget(arty.get(0).getEntity().getIndex());
            }
        }       
    }

	private int calcNumUnits()
    {
	    if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
	    {
	        setMinMaxRequested(1, 1);
	    }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            setMinMaxRequested(2, 2);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(3, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(4, 5);
        }
	    
	    return calculateForMinMaxRequested();
    }

    protected void createActivation()
	{
		activate.setName(pwcgGroundUnitInformation.getName() + ": Activate");		
		activate.setDesc("Activate for " + pwcgGroundUnitInformation.getName());
		activate.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		
		activateTimer = new McuTimer();
		activateTimer.setName(pwcgGroundUnitInformation.getName() + ": Activate Timer");		
		activateTimer.setDesc("Activate Timer for " + pwcgGroundUnitInformation.getName());
		activateTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());

		deactivate.setName(pwcgGroundUnitInformation.getName() + ": Deactivate");		
		deactivate.setDesc("Deactivate for " + pwcgGroundUnitInformation.getName());
		deactivate.setPosition(pwcgGroundUnitInformation.getPosition().copy());
		
		deactivateTimer = new McuTimer();
		deactivateTimer.setName(pwcgGroundUnitInformation.getName() + ": Deactivate Timer");		
		deactivateTimer.setDesc("Deactivate Timer for " + pwcgGroundUnitInformation.getName());
		deactivateTimer.setPosition(pwcgGroundUnitInformation.getPosition().copy());
	}

    public void createSubtitles()
    {
        McuSubtitle artilleryReadySubtitle = new McuSubtitle();
        artilleryReadySubtitle.setName("artilleryReadySubtitle Subtitle");
        artilleryReadySubtitle.setText("Artillery Ready");
        artilleryReadySubtitle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        activateTimer.setTarget(artilleryReadySubtitle.getIndex());
        subTitleList.add(artilleryReadySubtitle);
        
        McuSubtitle artilleryCancelledSubtitle = new McuSubtitle();
        artilleryCancelledSubtitle.setName("artilleryCancelledSubtitle Subtitle");
        artilleryCancelledSubtitle.setText("Artillery Cancelled");
        artilleryCancelledSubtitle.setPosition(pwcgGroundUnitInformation.getPosition().copy());
        deactivateTimer.setTarget(artilleryCancelledSubtitle.getIndex());
        subTitleList.add(artilleryCancelledSubtitle);

        MissionStringHandler subtitleHandler = MissionStringHandler.getInstance();
        subtitleHandler.registerMissionText(artilleryReadySubtitle.getLcText(), artilleryReadySubtitle.getText()); 
        subtitleHandler.registerMissionText(artilleryCancelledSubtitle.getLcText(), artilleryCancelledSubtitle.getText()); 
    }

	private Coordinate getRandomPosition() throws PWCGException 
	{
		Coordinate position = new Coordinate();
		
		int xoffset = RandomNumberGenerator.getRandom(1000);
		int zoffset = RandomNumberGenerator.getRandom(1000);
		
		position.setXPos(pwcgGroundUnitInformation.getPosition().copy().getXPos() + xoffset - 500);
		position.setZPos(pwcgGroundUnitInformation.getPosition().copy().getZPos() + zoffset - 500);
		
		return position;
	}

	@Override
	protected void createGroundTargetAssociations()
	{
	    pwcgGroundUnitInformation.getMissionBeginUnit().linkToMissionBegin(activateTimer.getIndex());
		activateTimer.setTarget(activate.getIndex());
		
		for (Artillery gun : arty)
		{
			activate.setObject(gun.getEntity().getIndex());
			deactivate.setObject(gun.getEntity().getIndex());
		}
		
		deactivateTimer.setTarget(deactivate.getIndex());
	}

	@Override
	final public void write(BufferedWriter writer) throws PWCGIOException 
	{	
	    try
	    {
	        pwcgGroundUnitInformation.getMissionBeginUnit().write(writer);
	        
    		activateTimer.write(writer);
    		activate.write(writer);
    		
    		deactivateTimer.write(writer);
    		deactivate.write(writer);
    		
    		for (Artillery gun: arty)
    		{
    			gun.write(writer);
    		}
    
    		for (int i = 0; i < subTitleList.size(); ++i)
            {
                McuSubtitle subtitle = subTitleList.get(i);
                subtitle.write(writer);
                writer.newLine();
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
            throw new PWCGIOException(e.getMessage());
        }
	}

	public McuTimer getActivateTimer() 
	{
		return activateTimer;
	}

	public McuTimer getDeactivateTimer() 
	{
		return deactivateTimer;
	}

}	

