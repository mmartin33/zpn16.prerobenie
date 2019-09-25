package sk.zpn.domena;

import org.apache.commons.lang.StringUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public final class FirmySlovenskoDigital {

        public List<FirmaRegistra> nacitanieDat(String nazovFirmy, String ico, boolean firmaObsahuje)  {

            if(!((nazovFirmy!=null&&nazovFirmy.length()>2)||ico!=null))

                System.out.println("Nie sú zadané dostatočné vyhľadávacie kritériá. Zadajte IČO alebo časť názvu firmy");

            List<FirmaRegistra> firmy=new ArrayList<FirmaRegistra>();
            Connection connection = null;
            try {
                connection =openConnection();


                String queryString;
                queryString = "select distinct " +
                        " org.name" +
                        " ,ica.ipo as ico " +
                        " ,adr.street "+
                        " ,adr.building_number "+
                        " ,adr.reg_number " +
                        " ,adr.postal_code "+
                        " ,adr.municipality "+
                        " ,adr.country "+
                        " ,rpo.source_register "+

//              " ,rpo.established_on" +
//              " ,adr.district,adr.formatted_address, adr.effective_from,adr.effective_to" +

                        " from rpo.organizations as rpo" +
                        " left join rpo.organization_name_entries as org on rpo.id=org.organization_id and org.effective_to is null " +
                        " left JOIN rpo.organization_address_entries as adr on rpo.id=adr.organization_id and adr.effective_to is null " +
                        " left join rpo.organization_identifier_entries as ica on rpo.id=ica.organization_id and ica.effective_to is null " +
                        " where " +
                        "  rpo.id is not null "+
                        " and ica.ipo is not null ";
                if (!StringUtils.isEmpty(nazovFirmy))
                    queryString = queryString +" and upper(org.name) like ? " ;
                if (!StringUtils.isEmpty(ico))
                    queryString = queryString + " and ica.ipo=? ";

                queryString=queryString+" order by org.name";

                PreparedStatement statement = connection.prepareStatement(queryString);
                int parameter=1;
                if(!StringUtils.isEmpty(nazovFirmy)) {
                    if (firmaObsahuje)
                        statement.setString(parameter++, "%" + nazovFirmy.toUpperCase() + "%");
                    else
                        statement.setString(parameter++, nazovFirmy.toUpperCase() + "%");
                }
                if (!StringUtils.isEmpty(ico))
                    statement.setInt(parameter++, Integer.parseInt(ico));

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()){
                    FirmaRegistra firma=new FirmaRegistra();
                    String nazov = (String) resultSet.getObject(1);
                    Long icoFirmy = (Long) resultSet.getObject(2);
                    String ulica = (String) resultSet.getObject(3);
                    String cisloDomu = (String) resultSet.getObject(4);
                    Integer popisneCislo = (Integer) resultSet.getObject(5);
                    if (resultSet.wasNull()||(popisneCislo!=null&&popisneCislo.intValue()==0))
                        popisneCislo=null;
                    String psc = (String) resultSet.getObject(6);
                    String obec = (String) resultSet.getObject(7);
                    String krajina = (String) resultSet.getObject(8);
                    String zdrojDat = (String) resultSet.getObject(9);

                    firma.setNazov(nazov);
                    if(icoFirmy!=null)
                        firma.setIco(icoFirmy.toString());
                    firma.setUlica(ulica);
                    String cd="";
                    if(!StringUtils.isEmpty(cisloDomu) && popisneCislo != null)
                        cd = popisneCislo.intValue()+ "/" + cisloDomu;
                    if(StringUtils.isEmpty(cisloDomu)&& popisneCislo!=null)
                        cd= popisneCislo.intValue()+"";
                    if (!StringUtils.isEmpty(cisloDomu) && popisneCislo == null)
                        cd=cisloDomu;
                    firma.setCisloDomu(cd);
                    if(StringUtils.isEmpty(firma.getUlica()))
                        firma.setUlica(cd);
                    firma.setPsc(psc);
                    if(StringUtils.isEmpty(firma.getPsc()))
                        firma.setPsc(cd);

                    firma.setObec(obec);
                    if(StringUtils.isEmpty(firma.getObec()))
                            firma.setObec(cd);

                    firma.setKrajina(krajina);
                    if(StringUtils.isEmpty(firma.getKrajina()))
                        firma.setKrajina(cd);


                    if(zdrojDat!=null&&zdrojDat.equals("Obchodný register"))
                        firma.setPravnickaOsoba(true);

                    firmy.add(firma);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("err_NepodariloSaNacitatData");
            }
            finally{
                //SQLUtil.close(connection);
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return firmy;
        }

        public Connection openConnection() throws SQLException {
//    String driver="net.sourceforge.jtds.jdbc.Driver";
//    if (driver != null) {
//      try {
//        Class.forName(driver);
//      } catch (ClassNotFoundException e) {
//        final SQLException ex = new SQLException("Driver load failed");
//        ex.initCause(e);
//        throw ex;
//      }
//    }

            //noinspection CallToDriverManagerGetConnection
            return DriverManager.getConnection("jdbc:postgresql://sql.ekosystem.slovensko.digital/datahub?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
                    "nni0wd3io11wqtbo0e8rmsa", "1h9BqMwE42tHcKNHXzbWiftfTd8950MyciiV3V2l");
        }



    }



