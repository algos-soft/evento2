package it.algos.evento.demo;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.EventoApp;
import it.algos.evento.EventoBootStrap;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.comune.ComuneImport;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.evento.Evento_;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.LetteraModulo;
import it.algos.evento.entities.lettera.Lettera_;
import it.algos.evento.entities.lettera.ModelliLettere;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.lettera.allegati.AllegatoModulo;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.ordinescuola.OrdineScuola_;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.rappresentazione.Rappresentazione_;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.stagione.Stagione_;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.pref.CompanyPrefs;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.AlgosApp;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.CQuery;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

//import it.algos.evento.multiazienda.EROContainer;

public class DemoDataGenerator {


    /**
     * Codice della demo company
     */
    private static final String DEMO_COMPANY_CODE = "demo";

    /**
     * Creazione dei dati iniziali per una data azienda.
     * <p>
     *
     * @param company - l'azienda di riferimento Attenzione! <br>
     *                L'ordine di creazione delle varie tavole deve rispettare le
     *                relazioni che esistono tra di esse. <br>
     * @return true se la company è stata creata correttamente
     */
    public static boolean createDemoData(BaseCompany company) {
        boolean success=false;

        EntityManager manager = EM.createEntityManager();
        manager.getTransaction().begin();

        try {


            // In questa classe devo sempre registrare la company nei record prima di salvare
            // perché questo codice può essere eseguito dal server prima che sia avviata
            // una sessione e quindi non posso assumere di poter prendere
            // la company dalla sessione.
            // In questa classe non posso quindi usare classi come EQuery o EContainer
            // che sono internamente filtrate in base alla Company che si
            // troverebbe nella sessione.
            // Anche nell'uso delle preferenze la company va sempre esplicitata

            if (getCount(Allegato.class, company) == 0) {
                creaAllegati(company, manager);
            }
            if (getCount(Lettera.class, company) == 0) {
                creaLettere(company, manager);
            }
            if (getCount(Sala.class, company) == 0) {
                creaSale(company, manager);
            }
            if (getCount(Progetto.class, company) == 0) {
                creaProgetti(company, manager);
            }
            if (getCount(ModoPagamento.class, company) == 0) {
                creaPagamenti(company, manager);
            }
            if (getCount(TipoRicevuta.class, company) == 0) {
                creaTipiRicevuta(company, manager);
            }
            if (getCount(OrdineScuola.class, company) == 0) {
                creaOrdiniScuola(company, manager);
            }
            if (getCount(Insegnante.class, company) == 0) {
                creaInsegnanti(company, manager);
            }
            if (getCount(Comune.class, company) == 0) {
                creaComuni(company, manager);
            }
            if (getCount(Scuola.class, company) == 0) {
                creaScuole(company, manager);
            }
            if (getCount(Stagione.class, company) == 0) {
                creaStagioni(company, manager);
            }
            if (getCount(Evento.class, company) == 0) {
                creaEventi(company, manager);
            }
            if (getCount(Rappresentazione.class, company) == 0) {
                creaRappresentazioni(company, manager);
            }
            if (getCount(Prenotazione.class, company) == 0) {
                creaPrenotazioni(company, manager);
            }
            // personalizza le preferenze per questa azienda
            creaPreferenze(company, manager);

            manager.getTransaction().commit();


            // cose eseguite per ogni company ad ogi avvio del server - le devo fare anche qui
            // dopo la creazione della nuova company.
            EventoBootStrap.doForCompany(company);

            success=true;

        } catch (Exception e) {
            manager.getTransaction().rollback();
            e.printStackTrace();
        }

        manager.close();

        return success;

    }// end of method


    /**
     * Create some demo data only if the table is empty
     */
    private static void creaSale(BaseCompany company, EntityManager manager) {
        Sala sala;

        sala = new Sala("Auditorium", 430);
        sala.setCompany(company);
        manager.persist(sala);

        sala = new Sala("Odeon", 220);
        sala.setCompany(company);
        manager.persist(sala);

    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    private static void creaProgetti(BaseCompany company, EntityManager manager) {
        Progetto p;

        p = new Progetto("La scienza della vita");
        p.setCompany(company);
        manager.persist(p);

        p = new Progetto("Storia e memoria");
        p.setCompany(company);
        manager.persist(p);

        p = new Progetto("Popoli nel tempo");
        p.setCompany(company);
        manager.persist(p);

    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaPagamenti(BaseCompany company, EntityManager manager) {
        ModoPagamento m;

        m = new ModoPagamento("BB", "Bonifico bancario");
        m.setCompany(company);
        manager.persist(m);

        m = new ModoPagamento("VP", "Vaglia postale");
        m.setCompany(company);
        manager.persist(m);

        m = new ModoPagamento("CONT", "Contanti");
        m.setCompany(company);
        manager.persist(m);

    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaTipiRicevuta(BaseCompany company, EntityManager manager) {
        save(new TipoRicevuta("RIC", "Ricevuta"), company, manager);
        save(new TipoRicevuta("FATT", "Fattura"), company, manager);
        save(new TipoRicevuta("FE", "Fattura Elettronica"), company, manager);
    }// end of method


    /**
     * Create some demo data only if the table is empty
     */
    public static void creaOrdiniScuola(BaseCompany company, EntityManager manager) {
        save(new OrdineScuola("INF", "Scuola dell'Infanzia"), company, manager);
        save(new OrdineScuola("PRI", "Primaria"), company, manager);
        save(new OrdineScuola("MED", "Secondaria I grado (medie)"), company, manager);
        save(new OrdineScuola("SUP", "Secondaria II grado (superiori)"), company, manager);
        save(new OrdineScuola("UNI", "Università"), company, manager);
    }// end of method


    /**
     * Create some demo data only if the table is empty
     */
    public static void creaInsegnanti(BaseCompany company, EntityManager manager) {
        Insegnante ins;

        ins = new Insegnante("Lovecchio", "Luigi", "Prof.",
                "lovecchio.luigi@gmail.com", "matematica, scienze");
        ins.setOrdineScuola(getOrdineScuolaRandom(company, manager));
        ins.setTelefono("348-784565");
        ins.setIndirizzo1("Via dei Gelsomini, 8");
        ins.setIndirizzo2("20154 Ferrara");
        ins.setCompany(company);
        manager.persist(ins);

        ins = new Insegnante("Ferrari", "Sara", "Prof.ssa",
                "ferrari.sara@gmail.com", "lettere");
        ins.setOrdineScuola(getOrdineScuolaRandom(company, manager));
        ins.setTelefono("885-4455778");
        ins.setIndirizzo1("Via Garibaldi, 26");
        ins.setIndirizzo2("50145 Rovigo");
        ins.setCompany(company);
        manager.persist(ins);

        ins = new Insegnante("Sarfatti", "Lucia", "Prof.ssa",
                "lsarfatti@ymail.com", "disegno");
        ins.setOrdineScuola(getOrdineScuolaRandom(company, manager));
        ins.setTelefono("999-5462335");
        ins.setIndirizzo1("Piazza Po, 12");
        ins.setIndirizzo2("56445 Mantova");
        ins.setCompany(company);
        manager.persist(ins);

        ins = new Insegnante("Gasparotti", "Antonella", "Prof.ssa",
                "agasparotti@tin.it", "storia, filosofia");
        ins.setOrdineScuola(getOrdineScuolaRandom(company, manager));
        ins.setTelefono("884-6589998");
        ins.setIndirizzo1("Largo Brasilia, 22");
        ins.setIndirizzo2("20100 Milano");
        ins.setCompany(company);
        manager.persist(ins);

        ins = new Insegnante("Marinelli", "Laura", "Prof.ssa",
                "lmarinelli@hotmail.it", "lettere, storia");
        ins.setOrdineScuola(getOrdineScuolaRandom(company, manager));
        ins.setTelefono("556-4456658");
        ins.setIndirizzo1("Via Vasco de Gama, 22");
        ins.setIndirizzo2("25556 Castelnuovo Val Tidone (PC)");
        ins.setCompany(company);
        manager.persist(ins);

    }// end of method

    /**
     * Crea i comuni
     */
    public static void creaComuni(BaseCompany company, EntityManager manager) {
        ServletContext svlContext = EventoApp.getServletContext();
        String path = "/" + AlgosApp.DEMODATA_FOLDER_NAME + "comuni/comuni.xls";
        String fullPath = svlContext.getRealPath(path);
        if (fullPath != null) {
            ComuneImport.doImport(fullPath, company, manager);
        }
    }// end of method

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaScuole(BaseCompany company, EntityManager manager) {
        Scuola scuola;

//        // cerca l'ordine "SUP"
//        Class clazz = OrdineScuola.class;
//        CriteriaBuilder cb = manager.getCriteriaBuilder();
//        CriteriaQuery<OrdineScuola> cq = cb.createQuery(clazz);
//        Root<OrdineScuola> root = (Root<OrdineScuola>) cq.from(clazz);
//        Predicate[] preds = new Predicate[2];
//        preds[0] = cb.equal(root.get(EventoEntity_.company), company);
//        preds[1] = cb.equal(root.get(OrdineScuola_.sigla), "SUP");
//        cq.where(preds);
//        TypedQuery<OrdineScuola> query = manager.createQuery(cq);
//        List<OrdineScuola> entities = query.getResultList();

        // cerca l'ordine "SUP"
        CQuery<OrdineScuola> q = new CQuery<>(manager, OrdineScuola.class);
        q.addFilter(CompanyEntity_.company, company);
        q.addFilter(OrdineScuola_.sigla, "SUP");
        List<OrdineScuola> entities = q.getResultList();

        OrdineScuola ordine = null;
        if (entities.size() > 0) {
            ordine = entities.get(0);
        }

        scuola = new Scuola("Beccaria", "Liceo Classico Beccaria",
                getComuneRandom(company, manager), ordine);
        scuola.setIndirizzo("Via Carlo Linneo,5");
        scuola.setCap("20145");
        scuola.setTelefono("025-365487");
        scuola.setEmail("liceobeccaria@yahoo.com");
        scuola.setCompany(company);
        manager.persist(scuola);

        scuola = new Scuola("Rampaldi", "Istituto Tecnico Rampaldi",
                getComuneRandom(company, manager), ordine);
        scuola.setIndirizzo("Via Varzi N. 16");
        scuola.setCap("56554");
        scuola.setTelefono("125-2356487");
        scuola.setEmail("istrampaldi@yahoo.com");
        scuola.setCompany(company);
        manager.persist(scuola);

        scuola = new Scuola("Leonardo", "Liceo Scientifico Leonardo da Vinci",
                getComuneRandom(company, manager), ordine);
        scuola.setIndirizzo("Via Stazione 1");
        scuola.setCap("36665");
        scuola.setTelefono("035-564789");
        scuola.setEmail("liceoleonardo@yahoo.com");
        scuola.setCompany(company);
        manager.persist(scuola);

        scuola = new Scuola("Falcone", "Istituto Magistrale Giovanni Falcone", ordine);
        scuola.setIndirizzo("Via Dunant, 1");
        scuola.setCap("24128");
        scuola.setTelefono("035-6598745");
        scuola.setEmail("istfalcone@yahoo.com");
        scuola.setCompany(company);
        manager.persist(scuola);

        scuola = new Scuola("Rota", "Istituto Superiore Lorenzo Rota",
                getComuneRandom(company, manager), ordine);
        scuola.setIndirizzo("Via Lavello, 17");
        scuola.setCap("55664");
        scuola.setTelefono("023-564789");
        scuola.setEmail("istitutorota@yahoo.com");
        scuola.setCompany(company);
        manager.persist(scuola);

    }// end of method

    /**
     * Assegna la company e salva
     */
    private static void save(CompanyEntity entity, BaseCompany company, EntityManager manager) {
        entity.setCompany(company);
        manager.persist(entity);
    }


    private static Comune getComuneRandom(BaseCompany company, EntityManager manager) {
        return (Comune) getEntityRandom(Comune.class, company, manager);
    }

    private static OrdineScuola getOrdineScuolaRandom(BaseCompany company, EntityManager manager) {
        return (OrdineScuola) getEntityRandom(OrdineScuola.class, company, manager);
    }


    private static Progetto getProgettoRandom(BaseCompany company, EntityManager manager) {
        return (Progetto) getEntityRandom(Progetto.class, company, manager);
    }

    private static BaseEntity getEntityRandom(Class clazz, BaseCompany company, EntityManager manager) {

//        CriteriaBuilder cb = manager.getCriteriaBuilder();
//        CriteriaQuery<? extends BaseEntity> cq = cb.createQuery(clazz);
//        Root<EventoEntity> root = (Root<EventoEntity>) cq.from(clazz);
//        Predicate pred;
//        pred = cb.equal(root.get(EventoEntity_.company), company);
//        cq.where(pred);
//        TypedQuery<? extends BaseEntity> query = manager.createQuery(cq);
//        List<EventoEntity> entities = (List<EventoEntity>) query.getResultList();

        CQuery q = new CQuery(manager, clazz);
        q.addFilter(CompanyEntity_.company, company);
        List entities = q.getResultList();

        BaseEntity entity=null;
        if(entities.size()>0){
            int min = 0;
            int max = entities.size() - 1;
            int randomNum = new Random().nextInt((max - min) + 1) + min;
            entity=(BaseEntity)entities.get(randomNum);
        }
        return entity;
    }

    /**
     * @return random da 1 a max
     */
    private static int getRandom(int max) {
        Random rand = new Random();
        int min = 1;
        return rand.nextInt((max - min) + 1) + min;
    }


    /**
     * Crea stagioni demo
     * Crea la stagione corrente
     */
    public static void creaStagioni(BaseCompany company, EntityManager manager) {

        // fino a fine maggio crea la stagione iniziata l'anno precedente
        // da giugno crea la stagione che inizia quest'anno
        DateTime dt = new DateTime();
        int year = dt.getYear();
        int month = dt.getMonthOfYear();  // where January is 1 and December is 12
        int yearStart;
        DateTime dStart, dEnd;
        if (month < 6) {
            yearStart = year - 1;
        } else {
            yearStart = year;
        }
        dStart = new DateTime(yearStart, 6, 1, 0, 0, 0);
        dEnd = dStart.plusYears(1).minusDays(1);
        String sigla = "" + dStart.getYear() + "-" + dEnd.getYear();

        Stagione stagione = new Stagione();
        stagione.setSigla(sigla);
        stagione.setDatainizio(dStart.toDate());
        stagione.setDatafine(dEnd.toDate());
        stagione.setCorrente(true);
        stagione.setCompany(company);
        manager.persist(stagione);

    }

    /**
     * Create some demo data only if the table is empty
     */
    public static void creaEventi(BaseCompany company, EntityManager manager) {

        Evento evento;
        evento = new Evento("Vivarelli", "Un ricordo di Roberto Vivarelli", 16,
                9, 0, 5);
        saveEvento(evento, company, manager);

        evento = new Evento("Accademia", "Accademia Bizantina", 16, 9, 0, 5);
        saveEvento(evento, company, manager);

        evento = new Evento("Gentile", "L'assassinio di Giovanni Gentile", 12,
                7, 0, 5);
        saveEvento(evento, company, manager);

        evento = new Evento("Chimica", "Le Frontiere della Chimica", 12, 7, 0, 5);
        saveEvento(evento, company, manager);

        evento = new Evento("Big Bang",
                "Big Bang: l'inizio e la fine nelle stelle", 18, 10, 0, 8);
        saveEvento(evento, company, manager);

        evento = new Evento("Auschwitz", "Auschwitz - parla un testimone", 15,
                8, 0, 5);
        saveEvento(evento, company, manager);

        evento = new Evento("Mafia",
                "Cercando la verità nel labirinto della mafia", 10, 8, 0, 5);
        saveEvento(evento, company, manager);

        evento = new Evento("Shackleton", "Sulle orme di Ernest Shackleton",
                16, 8, 0, 5);
        saveEvento(evento, company, manager);

    }// end of method

    private static void saveEvento(Evento evento, BaseCompany company, EntityManager manager) {

        // recupera la stagione corrente
        CQuery<Stagione> q = new CQuery<>(manager, Stagione.class);
        q.addFilter(CompanyEntity_.company, company);
        q.addFilter(Stagione_.corrente, true);
        List<Stagione> entities = q.getResultList();

        Stagione stagione = null;
        if (entities.size() > 0) {
            stagione = entities.get(0);
        }

        evento.setProgetto(getProgettoRandom(company, manager));
        evento.setStagione(stagione);
        evento.setCompany(company);
        manager.persist(evento);
    }


    private static int getCount(Class<? extends BaseEntity> clazz, BaseCompany company) {
        long num = AQuery.count(clazz, Evento_.company, company);
        return (int) num;
    }

    private static void creaRappresentazioni(BaseCompany company, EntityManager manager) {
        Rappresentazione rapp;

        // cominciamo a creare rappresentazioni dal 1 novembre dell'anno corrente
        int year = Year.now().getValue();
        MutableDateTime dt = new MutableDateTime(year, 11, 1, 0, 0, 0, 0);
        new MutableDateTime();

        for (int i = 0; i < 40; i++) {

            dt.addDays(getRandom(5));
            if (getRandom(2) == 1) {
                dt.setTime(10, 30, 0, 0);
            } else {
                dt.setTime(15, 0, 0, 0);
            }
            Evento evento = (Evento) getEntityRandom(Evento.class, company, manager);
            Sala sala = (Sala) getEntityRandom(Sala.class, company, manager);

            rapp = new Rappresentazione();
            rapp.setEvento(evento);
            rapp.setDataRappresentazione(dt.toDate());
            rapp.setSala(sala);
            rapp.setCapienza(sala.getCapienza());
            rapp.setCompany(company);
            manager.persist(rapp);

        }
    }

    /**
     * Crea un certo numero di prenotazioni.
     * <p>
     *
     * @return una lista delle prenotazioni create
     */
    public static ArrayList<Prenotazione> creaPrenotazioni(BaseCompany company, EntityManager manager) {
        ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
        int quante = 50;
        Prenotazione pren;

        // cominciamo a prenotare dal 1 settembre dell'anno corrente
        int year = Year.now().getValue();
        MutableDateTime dt = new MutableDateTime(year, 9, 1, 0, 0, 0, 0);

        for (int i = 0; i < quante; i++) {

            dt.addDays(getRandom(3) - 1); // da 0 a 2 gg

            // una rappresentazione che sia almeno 1 mese più avanti delle
            // prenotazione
            Rappresentazione rapp = getRappresentazionePost(dt.toDate(), company, manager);

            if (rapp != null) {

                int nInteri = getRandom(80);
                int nRidotti = getRandom(3);
                int nDisabili = getRandom(4);
                int nAccomp = getRandom(4);


                pren = new Prenotazione();
                int numpren = CompanyPrefs.nextNumPren.getInt(company);
                pren.setNumPrenotazione(numpren);
                pren.setDataPrenotazione(dt.toDate());
                pren.setRappresentazione(rapp);
                pren.setScuola((Scuola) getEntityRandom(Scuola.class, company, manager));
                pren.setInsegnante((Insegnante) getEntityRandom(Insegnante.class, company, manager));
                pren.setEmailRiferimento(pren.getInsegnante().getEmail());
                pren.setTelRiferimento(pren.getInsegnante().getTelefono());
                pren.setClasse("4D");

                pren.setNumInteri(nInteri);
                pren.setNumRidotti(nRidotti);
                pren.setNumDisabili(nDisabili);
                pren.setNumAccomp(nAccomp);

                pren.setModoPagamento((ModoPagamento) getEntityRandom(ModoPagamento.class, company, manager));

                DateTime scadConf = new DateTime(pren.getDataPrenotazione())
                        .plusDays(CompanyPrefs.ggScadConfermaPrenotazione.getInt(company));
                pren.setScadenzaConferma(scadConf.toDate());

                DateTime dataRapp = new DateTime(pren.getRappresentazione()
                        .getDataRappresentazione());
                DateTime scadPaga = dataRapp
                        .minusDays(CompanyPrefs.ggScadConfermaPagamento.getInt(company));
                pren.setScadenzaPagamento(scadPaga.toDate());

                // importi
                Evento e = pren.getRappresentazione().getEvento();
                BigDecimal iIntero = e.getImportoIntero();
                BigDecimal iRidotto = e.getImportoRidotto();
                BigDecimal iDisabili = e.getImportoDisabili();
                BigDecimal iAccomp = e.getImportoAccomp();
                pren.setImportoIntero(iIntero);
                pren.setImportoRidotto(iRidotto);
                pren.setImportoDisabili(iDisabili);
                pren.setImportoAccomp(iAccomp);

                // copertura dati obbligatori mancanti
                if ((pren.getEmailRiferimento() == null)
                        || (pren.getEmailRiferimento().equals(""))) {
                    pren.setEmailRiferimento("unamail@test.it");
                }
                if ((pren.getTelRiferimento() == null)
                        || (pren.getTelRiferimento().equals(""))) {
                    pren.setTelRiferimento("99999999");
                }

                pren.setCompany(company);

                manager.persist(pren);

                prenotazioni.add(pren);

                System.out.println("create " + i + " -> " + pren);

                int nextnum = CompanyPrefs.nextNumPren.getInt(company) + 1;
                CompanyPrefs.nextNumPren.put(company, nextnum);

            }

        }

        return prenotazioni;

    }

    // Recupera una rappresentazione a caso tra quelle che sono
    // almeno 1 mese più avanti della data fornita
    private static Rappresentazione getRappresentazionePost(Date date, BaseCompany company, EntityManager manager) {
        Rappresentazione rapp = null;
        DateTime dt = new DateTime(date).plusDays(30);

        // tutte le rappresentazioni dopo la data spedificata
        CQuery<Rappresentazione> q = new CQuery<>(manager, Rappresentazione.class);
        q.addFilter(CompanyEntity_.company, company);
        Predicate p = q.getCB().greaterThan(q.getPath(Rappresentazione_.dataRappresentazione), dt.toDate());
        q.addFilter(p);
        List<Rappresentazione> entities=q.getResultList();

        int max = entities.size();
        if (max > 0) {
            int random = getRandom(max) - 1;
            rapp = entities.get(random);
        }
        return rapp;
    }

    /**
     * Crea gli allegati
     */
    public static void creaAllegati(BaseCompany company, EntityManager manager) {
        ArrayList<Allegato> lista = AllegatoModulo.getDemoData();
        for (Allegato allegato : lista) {
            allegato.setCompany(company);
            manager.persist(allegato);
        }
    }// end of method


    /**
     * Crea le lettere mancanti
     */
    public static void creaLettere(BaseCompany company, EntityManager manager) {
        Lettera lettera;

        // controlla che esistano tutti i modelli previsti, se non esistono li crea
        for (ModelliLettere modello : ModelliLettere.values()) {
            String code = modello.getDbCode();
            Filter f1 = new Compare.Equal(Evento_.company.getName(), company);
            Filter f2 = new Compare.Equal(Lettera_.sigla.getName(), code);
            Filter f3 = new And(f1, f2);
            List<? extends BaseEntity> entities = AQuery.getList(Lettera.class, f3);
            if (entities.size() == 0) {
                lettera = LetteraModulo.getLetteraDemo(modello);
                lettera.setCompany(company);
                manager.persist(lettera);
            }
        }

    }// end of method

    /**
     * Crea le preferenze
     */
    private static void creaPreferenze(BaseCompany company, EntityManager manager) {
        // registra l'indirizzo della company come mittente delle email
        CompanyPrefs.senderEmailAddress.put(company, company.getEmail(), manager);
    }


}// end of class
