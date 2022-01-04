package pwcg.campaign;

import pwcg.aar.ui.events.model.TransferEvent;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.SquadronTransferFinder;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;


public class TransferHandler 
{
    private Campaign campaign = null;
    private CrewMember player = null;
    
    public TransferHandler (Campaign campaign, CrewMember player)
    {
        this.campaign = campaign;
        this.player = player;
    }

	public TransferEvent transferPlayer(Company oldSquadron, Company newSquadron) throws PWCGException 
	{
	    int leaveTimeForTransfer = transferleaveTime();	
        changeInRankForServiceTransfer(oldSquadron.determineServiceForSquadron(campaign.getDate()), newSquadron.determineServiceForSquadron(campaign.getDate()));
        movePlayerToNewSquadron(newSquadron);
        TransferEvent transferEvent = createTransferEvent(leaveTimeForTransfer, oldSquadron, newSquadron);
		
        return transferEvent;
	}

	public void transferAI(CrewMember crewMember) throws PWCGException 
    {
        SquadronTransferFinder squadronTransferFinder = new SquadronTransferFinder(campaign, crewMember);
        int newSquadronId = squadronTransferFinder.chooseSquadronForTransfer();
		CompanyPersonnel oldSquadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(crewMember.getCompanyId());
		CompanyPersonnel newSquadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(newSquadronId);

		oldSquadronPersonnel.removeCrewMember(crewMember);
        crewMember.setSquadronId(newSquadronId);
		newSquadronPersonnel.addCrewMember(crewMember);
    }

	private TransferEvent createTransferEvent(int leaveTimeForTransfer, Company oldSquad, Company newSquad) throws PWCGException
	{
        boolean isNewsWorthy = true;
		TransferEvent transferEvent = new TransferEvent(campaign, oldSquad.getCompanyId(), newSquad.getCompanyId(), leaveTimeForTransfer, player.getSerialNumber(), campaign.getDate(), isNewsWorthy);
		return transferEvent;
	}

	private void movePlayerToNewSquadron(Company newSquadron) throws PWCGException
	{        
		CompanyPersonnel oldSquadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(player.getCompanyId());
		CompanyPersonnel newSquadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(newSquadron.getCompanyId());

		oldSquadronPersonnel.removeCrewMember(player);
		player.setSquadronId(newSquadron.getCompanyId());
		newSquadronPersonnel.addCrewMember(player);
	}

	private int transferleaveTime() throws PWCGException 
	{
        int leaveTimeForTransfer = 5 + RandomNumberGenerator.getRandom(10);
        return leaveTimeForTransfer;
	}

    private void changeInRankForServiceTransfer(ArmedService oldService, ArmedService newService) throws PWCGException 
    {
        IRankHelper iRank = RankFactory.createRankHelper();        
        int rankPos = iRank.getRankPosByService(player.getRank(), oldService);
        
        int lowestRankPos = iRank.getLowestRankPosForService(newService);
        if (rankPos > lowestRankPos)
        {
            rankPos = lowestRankPos;
        }
        
        String newRank = iRank.getRankByService(rankPos, newService);
        player.setRank(newRank);
    }
}
