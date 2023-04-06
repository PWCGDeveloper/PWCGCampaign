package pwcg.campaign.context;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public enum Country {
    NEUTRAL(""),
    FRANCE("France"),
    BRITAIN("Britain"),
    CANADA(nationality("Britain", 90), nationality("France", 10)),
    USA("USA"),
    RUSSIA("Russia"),
    BELGIUM(nationality("France", 50), nationality("Flemish", 50)),
    GERMANY("Germany"),
    AUSTRIA(nationality("Germany", 60), nationality("Hungary", 40)),
    ITALY("Italy"),
    ;

    private final List<NationalityDistribution> nationalities;
    private final int cumulativeOdds;

    Country(NationalityDistribution... odds) {
        this.nationalities = Arrays.asList(odds);
        this.cumulativeOdds = nationalities.stream().mapToInt(d -> d.odds).sum();
    }

    Country(String singleNationality) {
        this.nationalities = Collections.singletonList(nationality(singleNationality, 100));
        this.cumulativeOdds = 100;
    }


    private static NationalityDistribution nationality(String nationality, int odds) {
        return new NationalityDistribution(nationality, odds);
    }

    public String pickNationality() {
        if (nationalities.size() == 1)
            return nationalities.get(0).nationality;

        int random = ThreadLocalRandom.current().nextInt(cumulativeOdds);

        int runningTotal = 0;
        for (NationalityDistribution n : nationalities) {
            runningTotal += n.odds;
            if (random < runningTotal)
                return n.nationality;
        }
        return nationalities.get(nationalities.size() - 1).nationality;
    }

    private static class NationalityDistribution {
        final String nationality;
        final int odds;

        private NationalityDistribution(String nationality, int odds) {
            this.nationality = nationality;
            this.odds = odds;
        }
    }
}
