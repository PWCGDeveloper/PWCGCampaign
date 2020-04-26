package pwcg.core.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pwcg.campaign.context.Country;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public enum Callsign {
	NONE(Country.GERMANY, 0, Country.RUSSIA, 0, Country.BRITAIN, 0, Country.USA, 0, Country.ITALY, 0),

	STORK(Country.GERMANY, 1, Country.RUSSIA, 1),
	GANNET(Country.GERMANY, 2, Country.RUSSIA, 2),
	RAVEN(Country.GERMANY, 3, Country.RUSSIA, 3),
	ROOK(Country.GERMANY, 4, Country.RUSSIA, 4),
	THRUSH(Country.GERMANY, 5, Country.RUSSIA, 5),
	CRANE(Country.GERMANY, 6, Country.RUSSIA, 6),
	FINCH(Country.GERMANY, 7, Country.RUSSIA, 7),
	ORIOLE(Country.GERMANY, 8, Country.RUSSIA, 8),
	CANARY(Country.GERMANY, 9, Country.RUSSIA, 9, Country.ITALY, 9),
	SWAN(Country.GERMANY, 10, Country.RUSSIA, 10),
	KITTIWAKE(Country.GERMANY, 11, Country.RUSSIA, 11),
	EAGLE(Country.GERMANY, 12, Country.RUSSIA, 12),
	PELICAN(Country.GERMANY, 13, Country.RUSSIA, 13),
	SWIFT(Country.GERMANY, 14, Country.RUSSIA, 14),
	REDSHANK(Country.GERMANY, 15, Country.RUSSIA, 15),
	DUCK(Country.GERMANY, 16, Country.RUSSIA, 16),
	PHEASANT(Country.GERMANY, 17, Country.RUSSIA, 17),
	SEAGULL(Country.GERMANY, 18, Country.RUSSIA, 18),
	BRAMBLING(Country.GERMANY, 19, Country.RUSSIA, 19),
	HAWK(Country.GERMANY, 20, Country.RUSSIA, 20),

	ACACIA(Country.GERMANY, 21, Country.RUSSIA, 21),
	BEECH(Country.GERMANY, 22, Country.RUSSIA, 22),
	ELM(Country.GERMANY, 23, Country.RUSSIA, 23),
	OAK(Country.GERMANY, 24, Country.RUSSIA, 24),
	HORNBEAM(Country.GERMANY, 25, Country.RUSSIA, 25),
	SPRUCE(Country.GERMANY, 26, Country.RUSSIA, 26),
	JASMINE(Country.GERMANY, 27, Country.RUSSIA, 27),
	WILLOW(Country.GERMANY, 28, Country.RUSSIA, 28),
	MAPLE(Country.GERMANY, 29, Country.RUSSIA, 29),
	LINDEN(Country.GERMANY, 30, Country.RUSSIA, 30),
	MAGNOLIA(Country.GERMANY, 31, Country.RUSSIA, 31),
	ALDER(Country.GERMANY, 32, Country.RUSSIA, 32),
	FIR(Country.GERMANY, 33, Country.RUSSIA, 33),
	ROWAN(Country.GERMANY, 34, Country.RUSSIA, 34),
	PINE(Country.GERMANY, 35, Country.RUSSIA, 35),
	THUJA(Country.GERMANY, 36, Country.RUSSIA, 36),
	PISTACHIO(Country.GERMANY, 37, Country.RUSSIA, 37),
	MULBERRY(Country.GERMANY, 38, Country.RUSSIA, 38),
	EUCALYPTUS(Country.GERMANY, 39, Country.RUSSIA, 39),
	ASH(Country.GERMANY, 40, Country.RUSSIA, 40),

	STORM(Country.GERMANY, 41, Country.RUSSIA, 41),
	TYPHOON(Country.GERMANY, 42, Country.RUSSIA, 42),
	HURRICANE(Country.GERMANY, 43, Country.RUSSIA, 43),
	CYCLONE(Country.GERMANY, 44, Country.RUSSIA, 44),
	VOLCANO(Country.GERMANY, 45, Country.RUSSIA, 45),

    ACORN(Country.BRITAIN, 46),
    CHARLIE(Country.BRITAIN, 47),
    BEARSKIN(Country.BRITAIN, 48),
    LUTON(Country.BRITAIN, 49),
    ALERT(Country.BRITAIN, 50),
    MITOR(Country.BRITAIN, 51),
    ANGEL(Country.BRITAIN, 52),
    RABBIT(Country.BRITAIN, 53),
    BAFFIN(Country.BRITAIN, 54),
    FILMSTAR(Country.BRITAIN, 55),
    TURKEY(Country.BRITAIN, 56),
    TENNIS(Country.BRITAIN, 57),
    VICEROY(Country.BRITAIN, 58),
    PANSY(Country.BRITAIN, 59),
    HYDRO(Country.BRITAIN, 60),
    SUNCUP(Country.BRITAIN, 61),
    TALLYHO(Country.BRITAIN, 62),
    WAGON(Country.BRITAIN, 63),
    DOGROSE(Country.BRITAIN, 64),
    GANNIC(Country.BRITAIN, 65),

    ROUGHMAN(Country.USA, 66),
    BLUE_BIRD(Country.USA, 67),
    PLASTIC(Country.USA, 68),
    DITTO(Country.USA, 69),
    BULLRING(Country.USA, 70),
    WOODBINE(Country.USA, 71),
    WARCRAFT(Country.USA, 72),
    RIPPER(Country.USA, 73),
    COBWEB(Country.USA, 74),
    ROCKET(Country.USA, 75),
    ANGUS(Country.USA, 76),
    CLEVELAND(Country.USA, 77),
    GRANITE(Country.USA, 78),
    SLAPJACK(Country.USA, 79),
    PINTAIL(Country.USA, 80),
    TURQOISE(Country.USA, 81),
    BISON(Country.USA, 82),
    NEPTUNE(Country.USA, 83),
    CHIEFTAIN(Country.USA, 84),
    ELWOOD(Country.USA, 85),

    WATERBURY(Country.BRITAIN, 86),
    SOUTHBURY(Country.BRITAIN, 87),
    ALDERWOOD(Country.BRITAIN, 88, Country.USA, 108),
    LOCKERLY(Country.BRITAIN, 89),
    NEWBURY(Country.BRITAIN, 90),
    DURRINGTON(Country.BRITAIN, 91),
    ASHBURY(Country.BRITAIN, 92),
    MANCHESTER(Country.BRITAIN, 93),
    MORNINGSTAR(Country.BRITAIN, 94),
    HOLYBOURNE(Country.BRITAIN, 95),
    WALSINGHAM(Country.BRITAIN, 96),
    OTTERHOUND(Country.BRITAIN, 97),
    WESTMORELAND(Country.BRITAIN, 98),
    COVENTRY(Country.BRITAIN, 99),
    HARRINGTON(Country.BRITAIN, 100),
    CUMBERLAND(Country.BRITAIN, 101),
    WELLINGTON(Country.BRITAIN, 102),
    BELLINGHAM(Country.BRITAIN, 103),
    DUNSTERVILLE(Country.BRITAIN, 104),
    SULLINGTON(Country.BRITAIN, 105),

    MAYFLOWER(Country.USA, 106),
    HIGHTOWER(Country.USA, 107),
    // ALDERWOOD already defined for Britain
    HALLOWAY(Country.USA, 109),
    RAMONA(Country.USA, 110),
    MADELINE(Country.USA, 111),
    ROSEMARY(Country.USA, 112),
    JUNIPER(Country.USA, 113),
    DELAWARE(Country.USA, 114),
    BURLINGTON(Country.USA, 115),
    OVERSTREET(Country.USA, 116),
    CLARABELLE(Country.USA, 117),
    MARIGOLD(Country.USA, 118),
    GOLDENROD(Country.USA, 119),
    EDISON(Country.USA, 120),
    FARMINGTON(Country.USA, 121),
    BEAVERDAM(Country.USA, 122),
    ARLINGTON(Country.USA, 123),
    EVERGREEN(Country.USA, 124),
    SUNFLOWER(Country.USA, 125),

    LONGBOW(Country.BRITAIN, 126),
    CROSSBOW(Country.BRITAIN, 127),
    BROADSWORD(Country.BRITAIN, 128),
    JAVELIN(Country.BRITAIN, 129),
    KENWAY(Country.BRITAIN, 130),

    SWEEPSTAKES(Country.USA, 131),
    MARMITE(Country.USA, 132),
    MUDGUARD(Country.USA, 133),
    GROUNDHOG(Country.USA, 134),
    SPOTLIGHT(Country.USA, 135);

	private Map<Country, Integer> mappings = new HashMap<>();

	private Callsign(final Object... args)
	{
	    assert(args.length % 2 == 0);
	    for (int i = 0; i < args.length; i += 2)
	    {
	        Country country = (Country) args[i];
	        Integer value = (Integer) args[i + 1];
	        assert(country != null && value != null);
	        mappings.put(country, value);
	    }

	}

	public Integer getNum(Country country)
	{
	    if (country == Country.NEUTRAL)
	    {
            return 0;
	    }
	    else if (!mappings.containsKey(country))
	    {
	        PWCGLogger.log(LogLevel.ERROR, "Callsign " + toString() + " is not valid for country " + country.toString());
	        return 0;
	    }
		return mappings.get(country);
	}
	
	public Set<Country> getCountries()
	{
	    return mappings.keySet();
	}

	public String toString()
	{
		return this.name().substring(0,1) + this.name().substring(1).toLowerCase().replace("_", " ");
	}
}
