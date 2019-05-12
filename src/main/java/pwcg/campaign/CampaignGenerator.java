package pwcg.campaign;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGUserException;
import pwcg.core.utils.DateUtils;

public class CampaignGenerator 
{
    private CampaignGeneratorModel generatorModel;
    private Campaign campaign;
    
    public CampaignGenerator(CampaignGeneratorModel generatorModel)
    {
        this.generatorModel = generatorModel;
    }

    public Campaign generate() throws PWCGException
    {
        validateCampaignInputs();
        createCampaignBasis();
        staffSquadrons();
        staffReplacements();
        
        return campaign;
    }

    public void writeCampaign() throws PWCGException
    {
        campaign.write();
        PWCGContextManager.getInstance().setCampaign(campaign);
    }

    private void createCampaignBasis() throws PWCGException
    {
        setMapForNewCampaign();
        createCampaign();
        setCampaignAces();
    }

    private void createCampaign() throws PWCGException
	{
		campaign = new Campaign();
        campaign.initializeCampaignConfigs();
        campaign.setDate(generatorModel.getCampaignDate());
        campaign.getCampaignData().setName(generatorModel.getCampaignName());
        campaign.getCampaignData().setCoop(generatorModel.isCoop());
	}

    private void staffSquadrons() throws PWCGException
    {
        List<Squadron> activeSquadronsOnCampaignStartDate = PWCGContextManager.getInstance().getSquadronManager().getActiveSquadrons(generatorModel.getCampaignDate());
        for (Squadron squadron : activeSquadronsOnCampaignStartDate)
        {
            CampaignSquadronGenerator squadronGenerator = new CampaignSquadronGenerator(campaign, squadron);
            squadronGenerator.createSquadron(generatorModel);
        }
    }

    private void staffReplacements() throws PWCGException
    {
        List<ArmedService> armedServices = ArmedServiceFactory.createServiceManager().getAllActiveArmedServices(campaign.getDate());
        for (ArmedService armedService : armedServices)
        {
            CampaignEquipmentGenerator equipmentGenerator = new CampaignEquipmentGenerator(campaign, armedService);
            equipmentGenerator.createReplacements();
        }
    }

    private void setMapForNewCampaign() throws PWCGException
    {
        Squadron squad = PWCGContextManager.getInstance().getSquadronManager().getSquadronByName(generatorModel.getSquadronName(), generatorModel.getCampaignDate());
        IAirfield airfield = squad.determineCurrentAirfieldAnyMap(generatorModel.getCampaignDate());
        List<FrontMapIdentifier> airfieldMaps = PWCGContextManager.getInstance().getMapForAirfield(airfield.getName());
        FrontMapIdentifier initialAirfieldMap = airfieldMaps.get(0);

        PWCGContextManager.getInstance().changeContext(initialAirfieldMap);
    }

	private void validateCampaignInputs()
	        throws PWCGUserException, PWCGException
	{
		if (generatorModel.getCampaignDate() == null)
        {
            throw new PWCGUserException ("Invalid date - no campaign start date provided");
        }


        Date earliest = DateUtils.getBeginningOfGame();
        Date latest = DateUtils.getEndOfWar();

        if (generatorModel.getCampaignDate().before(earliest) || generatorModel.getCampaignDate().after(latest))
        {
            throw new PWCGUserException ("Invalid date - must be between start and end of war");
        }

        if (generatorModel.getCampaignName() == null || generatorModel.getCampaignName().length() == 0)
        {
            throw new PWCGUserException ("Invalid name - no campaign name provided");
        }

        if (generatorModel.getPlayerName() == null || generatorModel.getPlayerName().length() == 0)
        {
            throw new PWCGUserException ("Invalid name - no pilot name provided");
        }

        if (generatorModel.getPlayerRank() == null || generatorModel.getPlayerRank().length() == 0)
        {
            throw new PWCGUserException ("Invalid rank - no rank provided");
        }

        if (generatorModel.getSquadronName() == null || generatorModel.getSquadronName().length() == 0)
        {
            throw new PWCGUserException ("Invalid squaron - no squadron provided");
        }
        
        if (generatorModel.getPlayerRegion() == null)
        {
            generatorModel.setPlayerRegion("");
        }
	}

	private void setCampaignAces() throws PWCGException
	{
		CampaignAces aces =  PWCGContextManager.getInstance().getAceManager().loadFromHistoricalAces(generatorModel.getCampaignDate());
        campaign.getPersonnelManager().setCampaignAces(aces);
	}

}
