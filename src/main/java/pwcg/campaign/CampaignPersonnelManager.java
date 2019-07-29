package pwcg.campaign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;

public class CampaignPersonnelManager 
{
	private Campaign campaign = null;
    private Map<Integer, SquadronPersonnel> squadronPersonnelAllSquadrons = new HashMap<>();
    private Map<Integer, PersonnelReplacementsService> personnelReplacementsServices = new HashMap<>();
	private CampaignAces campaignAces = new CampaignAces();
	

	public CampaignPersonnelManager(Campaign campaign) 
	{
		this.campaign = campaign;
	}

	public Map<Integer, SquadronPersonnel> getCampaignPersonnel()
	{
		return new HashMap<Integer, SquadronPersonnel>(squadronPersonnelAllSquadrons);
	}

    public SquadronPersonnel getSquadronPersonnel(Integer squadronId)
    {
        return squadronPersonnelAllSquadrons.get(squadronId);
    }

	public void addPersonnelForSquadron(SquadronPersonnel campaignPersonnel)
	{
	    squadronPersonnelAllSquadrons.put(campaignPersonnel.getSquadron().getSquadronId(), campaignPersonnel);
	}

	public CampaignAces getCampaignAces()
	{
		return campaignAces;
	}

	public void setCampaignAces(CampaignAces campaignAces) throws PWCGException
	{
	    for (Ace campaignAce : campaignAces.getCampaignAces().values())
	    {
            campaignAce.mergeWithHistorical(campaign);
	    }
	    
		this.campaignAces = campaignAces;
	}

    public List<SquadronPersonnel> getAllSquadronPersonnel()
    {
        return new ArrayList<SquadronPersonnel>(squadronPersonnelAllSquadrons.values());
    }    

    public Map<Integer, SquadronMember> getAllCampaignMembers() throws PWCGException
    {
        Map<Integer, SquadronMember> allSquadronMembers = getAllNonAceCampaignMembers();
        allSquadronMembers.putAll(campaignAces.getCampaignAces());
        return allSquadronMembers;
    }

    public SquadronMembers getAllActivePlayers() throws PWCGException
    {
        return getPlayersForStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
    }

    public SquadronMembers getFlyingPlayers() throws PWCGException
    {
        return getPlayersForStatus(SquadronMemberStatus.STATUS_ACTIVE);
    }
    
    private SquadronMembers getPlayersForStatus(int status) throws PWCGException
    {
    	SquadronMembers allPlayers =  new SquadronMembers();
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
        	SquadronMembers playersInSquadron = squadronPersonnel.getPlayersByStatus(status);
        	allPlayers.addSquadronMembers(playersInSquadron);
        }
        return allPlayers;
    }    

    public SquadronMember getSinglePlayer() throws PWCGException
    {
        SquadronMembers allPlayers =  getAllActivePlayers();
        return allPlayers.getSquadronMemberList().get(0);
    }    

    private Map<Integer, SquadronMember> getAllNonAceCampaignMembers() throws PWCGException
    {
        Map<Integer, SquadronMember> allCampaignMembers =  new HashMap<>();
        for (SquadronPersonnel squadronPersonnel : campaign.getPersonnelManager().getAllSquadronPersonnel())
        {
            allCampaignMembers.putAll(squadronPersonnel.getSquadronMembersWithAces().getSquadronMemberCollection());
        }
        return allCampaignMembers;
    }    

    public SquadronMember getAnyCampaignMember(Integer serialNumber) throws PWCGException
    {
        SquadronMember squadronMember = campaignAces.retrieveAceBySerialNumber(serialNumber);
        if (squadronMember != null)
        {
            return squadronMember;
        }        

        for (SquadronPersonnel squadronPersonnel : squadronPersonnelAllSquadrons.values())
        {
            squadronMember = squadronPersonnel.getSquadronMember(serialNumber);
            if (squadronMember != null)
            {
                return squadronMember;
            }        
        }

        throw new PWCGException ("Unable to locate squadron member for serial number " + serialNumber);
    }

    public List<PersonnelReplacementsService> getAllPersonnelReplacementsServices()
    {
        return new ArrayList<PersonnelReplacementsService>(personnelReplacementsServices.values());
    }
    
    public PersonnelReplacementsService getPersonnelReplacementsService(Integer serviceId)
    {
        return personnelReplacementsServices.get(serviceId);
    }

    public void addPersonnelReplacementsService(Integer serviceId, PersonnelReplacementsService personnelReplacementsService)
    {
        this.personnelReplacementsServices.put(serviceId, personnelReplacementsService);
    }

    public List<PersonnelReplacementsService> getAllPersonnelReplacements()
    {
        return new ArrayList<PersonnelReplacementsService>(personnelReplacementsServices.values());
    }

    public int getReplacementCount() throws PWCGException
    {
    	int replacementCount = 0;
    	for (PersonnelReplacementsService replacementService : personnelReplacementsServices.values())
    	{
    		replacementCount += replacementService.getReplacements().getActiveCount(campaign.getDate());
    	}
    	
        return replacementCount;
    }
}
