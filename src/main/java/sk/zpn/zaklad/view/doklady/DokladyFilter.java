package sk.zpn.zaklad.view.doklady;

import com.vaadin.data.provider.ListDataProvider;
import sk.zpn.domena.Doklad;

public class DokladyFilter {
    private ListDataProvider<Doklad> dataProvider;
    private String cisloDokladu;
    private String poberatel;

    public DokladyFilter(ListDataProvider<Doklad> dataProvider) {
        this.dataProvider=dataProvider;
        this.dataProvider.addFilter(this::test);
    }

    public void setCisloDokladu(String cisloDokladu) {
        this.cisloDokladu = cisloDokladu;
        this.dataProvider.refreshAll();
    }

    public void setPoberatel(String poberatel) {
        this.poberatel = poberatel;
        this.dataProvider.refreshAll();
    }
    public boolean test(Doklad doklad) {
        boolean matchesCisloDokladu = matches(doklad.getCisloDokladu(), cisloDokladu);
        boolean matchesPoberatel = matches(doklad.getPoberatelMenoAdresa(), poberatel);

        return matchesCisloDokladu && matchesPoberatel ;
    }

    private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty() || value
                .toLowerCase().contains(searchTerm.toLowerCase());
    }
}

