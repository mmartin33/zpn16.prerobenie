package sk.zpn.domena;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;





    @Entity
    @Table(name = "uzivatelia")
    public class Uzivatel {

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name = "id")
        private int id;

        @Column(name = "meno")
        private String meno;


        @Column(name = "heslo")
        private String heslo;

        public Uzivatel() {

        }

        public Uzivatel(int id, String meno, String heslo) {
            this.setId(id);
            this.setMeno(meno);
            this.setHeslo(heslo);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
        public String getMeno() {
            return meno;
        }

        public void setMeno(String meno) {
            this.meno = meno;
        }

        public String getHeslo() {
            return heslo;
        }

        public void setHeslo(String heslo) {
            this.heslo = heslo;
        }
    }





