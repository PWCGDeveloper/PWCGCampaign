package pwcg.campaign.squadmember;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.api.IArmedServiceManager;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.skin.Skin;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class HistoricalAce extends SquadronMember
{
    protected List<HistoricalAceSquadron> squadrons = new ArrayList<HistoricalAceSquadron>();
    protected List<HistoricalAceRank> ranks = new ArrayList<HistoricalAceRank>();

    public HistoricalAce()
    {
        super();
        aiSkillLevel = AiSkillLevel.ACE;
    }

    public Ace getAtDate(Date date) throws PWCGException
    {
        Ace aceNow = new Ace();

        determineAceName(aceNow, date);
        determineAcePicture(aceNow);
        determineAceVictories(date, aceNow);
        determineAceMissionsFlown(aceNow);
        determineAceMedals(date, aceNow);
        Squadron referenceSquadron = determineHistoricalAceSquadron(date, aceNow);
        determineAceCountry(date, aceNow, referenceSquadron);
        determineHistoricalAceRank(date, aceNow, referenceSquadron);
        determineAceSkins(aceNow);
        
        return aceNow;
    }

    private void determineAceName(Ace aceNow, Date date) throws PWCGException
    {
        aceNow.setName(name);
        aceNow.setSerialNumber(serialNumber);
        aceNow.setPilotActiveStatus(pilotActiveStatus, date, null);
    }

    private void determineAcePicture(Ace aceNow)
    {
        String picName = name + ".jpg";
        aceNow.setPicName(picName);
    }

    private void determineAceVictories(Date date, Ace aceNow)
    {
        for (int i = 0; i < victories.size(); ++i)
        {
            Victory aceVictory = victories.get(i);

            if (date.after(aceVictory.getDate()))
            {
                aceNow.victories.add(aceVictory);
            }
        }
    }

    private void determineAceMissionsFlown(Ace aceNow) throws PWCGException
    {
        int missionsFlown = aceNow.getSquadronMemberVictories().getAirToAirVictoryCount() * 3;
        missionsFlown += RandomNumberGenerator.getRandom(20);
        aceNow.setMissionFlown(missionsFlown);
    }

    private void determineAceMedals(Date date, Ace aceNow)
    {
        for (int i = 0; i < medals.size(); ++i)
        {
            Medal medal = medals.get(i);
            if (date.after(medal.getMedalDate()))
            {
                aceNow.medals.add(medal);
            }
        }
    }

    private Squadron determineHistoricalAceSquadron(Date date, Ace aceNow) throws PWCGException
    {
        aceNow.setSquadronId(-1);

        HistoricalAceSquadron lastHistoricalAceSquadron = null;
        for (int i = 0; i < squadrons.size(); ++i)
        {
            HistoricalAceSquadron aceSquadron = squadrons.get(i);
            if (date.after(aceSquadron.date))
            {
                if (aceSquadron.squadron == SquadronMemberStatus.STATUS_ON_LEAVE)
                {
                    aceNow.setPilotActiveStatus(SquadronMemberStatus.STATUS_ON_LEAVE, null, null);
                }
                else if (aceSquadron.squadron == SquadronMemberStatus.STATUS_KIA)
                {
                    aceNow.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, date, null);
                }
                else
                {
                    aceNow.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
                    lastHistoricalAceSquadron = aceSquadron;
                    aceNow.setSquadronId(lastHistoricalAceSquadron.squadron);
                }
            }
        }

        Squadron squadron = null;
        if (lastHistoricalAceSquadron != null)
        {
            squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(lastHistoricalAceSquadron.squadron);
        }

        return squadron;
    }

    private void determineHistoricalAceRank(Date date, Ace aceNow, Squadron referenceSquadron) throws PWCGException
    {
        if (ranks.size() > 5)
        {
            throw new PWCGException("malformed entry for ace " + name);
        }
        
        
        ArmedService service = null;
        if (referenceSquadron != null)
        {
            service = referenceSquadron.determineServiceForSquadron(date);
        }
        else
        {
            ICountry country = CountryFactory.makeCountryByCountry(aceNow.getCountry());
            IArmedServiceManager armedServiceManager = ArmedServiceFactory.createServiceManager();
            service = armedServiceManager.getPrimaryServiceForNation(country.getCountry(), date);
        }

        if (service != null)
        {
            IRankHelper rankLists = RankFactory.createRankHelper();
            ArrayList<String> rankStrings = rankLists.getRanksByService(service);

            for (int i = 0; i < ranks.size(); ++i)
            {
                HistoricalAceRank aceRank = ranks.get(i);
                if (!date.before(aceRank.date))
                {
                    aceNow.setRank(rankStrings.get(aceRank.rank));
                }
            }
        }
    }

    private void determineAceCountry(Date date, Ace aceNow, Squadron referenceSquadron) throws PWCGException
    {
        if (referenceSquadron != null)
        {
            aceNow.setCountry(referenceSquadron.determineSquadronCountry(date).getCountry());
        }
        else
        {
            aceNow.setCountry(country);
        }
    }

    private void determineAceSkins(Ace aceNow)
    {
        List<Skin> skinsForAce = new ArrayList<Skin>();
        for (Skin skin : skins)
        {
            skinsForAce.add(skin);
        }
        aceNow.setSkins(skinsForAce);
    }

    public int getStatus(Date date)
    {
        for (HistoricalAceSquadron squadron : getSquadrons())
        {
            if (squadron.squadron == SquadronMemberStatus.STATUS_KIA)
            {
                if (date.after(squadron.date))
                {
                    return SquadronMemberStatus.STATUS_KIA;
                }
            }
        }

        return pilotActiveStatus;
    }

    public List<Victory> getVictories(Date date)
    {
        List<Victory> victoriesAtDate = new ArrayList<Victory>();
        for (Victory victory : victories)
        {
            if (!victory.getDate().after(date))
            {
                victoriesAtDate.add(victory);
            }
        }
        return victoriesAtDate;
    }

    public List<Medal> getMedals(Date date)
    {
        List<Medal> medalsAtDate = new ArrayList<Medal>();
        for (Medal medal : medals)
        {
            if (!medal.getMedalDate().after(date))
            {
                medalsAtDate.add(medal);
            }
        }
        return medalsAtDate;
    }

    public int getCurrentSquadron(Date date, boolean useOnLeave)
    {
        int currentSquadron = -1;

        for (int i = 0; i < squadrons.size(); ++i)
        {
            HistoricalAceSquadron squadron = squadrons.get(i);

            if (!squadron.date.after(date))
            {
                if (useOnLeave == true)
                {
                    currentSquadron = squadron.squadron;
                }
                else
                {
                    if (squadron.squadron != SquadronMemberStatus.STATUS_ON_LEAVE)
                    {
                        currentSquadron = squadron.squadron;
                    }
                }
            }
        }

        return currentSquadron;
    }

    public String getCurrentRank(Date date) throws PWCGException
    {
        HistoricalAceRank currentRank = null;

        for (HistoricalAceRank rank : ranks)
        {
            if (!rank.date.after(date))
            {
                currentRank = rank;
            }
        }

        String rankName = "";

        int squadId = getCurrentSquadron(date, false);
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadId);

        if (squadron != null && currentRank != null)
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            rankName = rankObj.getRankByService(currentRank.rank, squadron.determineServiceForSquadron(date));
        }

        return rankName;
    }

    public int getAceNumVictories(Date date)
    {
        int numVictories = 0;

        for (int i = 0; i < victories.size(); ++i)
        {
            Victory victory = victories.get(i);

            if (victory.getDate().before(date))
            {
                ++numVictories;
            }
        }

        return numVictories;
    }

    public void addHistoricalAceSquadron(int squadron, Date date)
    {
        HistoricalAceSquadron as = new HistoricalAceSquadron();
        as.squadron = squadron;
        as.date = date;
        squadrons.add(as);
    }

    public List<HistoricalAceSquadron> getSquadrons()
    {
        return squadrons;
    }

    public void setSquadrons(ArrayList<HistoricalAceSquadron> squadrons)
    {
        this.squadrons = squadrons;
    }

    public List<HistoricalAceRank> getRanks()
    {
        return ranks;
    }

    public void setRanks(ArrayList<HistoricalAceRank> ranks)
    {
        this.ranks = ranks;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setSquadrons(List<HistoricalAceSquadron> squadrons)
    {
        this.squadrons = squadrons;
    }

    public void setRanks(List<HistoricalAceRank> ranks)
    {
        this.ranks = ranks;
    }
}
