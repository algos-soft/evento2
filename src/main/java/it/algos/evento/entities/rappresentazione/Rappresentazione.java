package it.algos.evento.entities.rappresentazione;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.Prenotazione_;
import it.algos.evento.entities.sala.Sala;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.entity.EM;
import it.algos.webbase.web.query.AQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@DefaultSort({"dataRappresentazione"})
public class Rappresentazione extends CompanyEntity {

    public static final String CMD_PRENOTAZIONI_EXPORT = "Esporta prenotazioni...";
    public static final Resource ICON_PRENOTAZIONI_EXPORT = FontAwesome.DOWNLOAD;
    public static final String CMD_PARTECIPANTI_EXPORT = "Esporta partecipanti...";
    public static final Resource ICON_MEMO_EXPORT = FontAwesome.DOWNLOAD;
    public static final String CMD_EXPORT = "Esporta Rappresentazioni...";
    public static final Resource ICON_EXPORT = FontAwesome.DOWNLOAD;

    private static final long serialVersionUID = -3267255652926186175L;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd-MM-yyyy HH:mm");



    @ManyToOne
    @NotNull
    private Evento evento;

    @ManyToOne
    @NotNull
    private Sala sala;

    private int capienza;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date dataRappresentazione;

    @Lob
    private String note;

    @ManyToMany
    // @CascadeOnDelete mette la constraint ON DELETE CASCADE sulla tabella di incrocio
    // - cancellando lo One Record cancella automaticamente i record nella tabella di incrocio
    // - non cancella i record nella tabella di destinazione
    // HA EFFETTO NEL DDL (CREAZIONE DATABASE)
    @CascadeOnDelete
    private List<Insegnante> insegnanti;


    @OneToMany(mappedBy = "rappresentazione")
    @CascadeOnDelete
    private List<Prenotazione> prenotazioni;


    public Rappresentazione() {
        super();
    }// end of constructor


    public Rappresentazione(Evento evento, Sala sala, Date dataRappresentazione) {
        super();
        setEvento(evento);
        setSala(sala);
        setDataRappresentazione(dataRappresentazione);
    }// end of constructor

    /**
     * Ritorna una data nel formato adeguato per una Rappresentazione (nomegiorno-giorno-mese-ammo-ora-minuto)
     */
    public static String getDateAsString(Date date) {
        String stringa = "";
        if (date != null) {
            stringa = dateFormat.format(date);
        }
        return stringa;
    }// end of method

    /**
     * Recupera la rappresentazione usando la query specifica
     *
     * @return la rappresentazione, null se non trovato
     */
    public static Rappresentazione read(Object id) {
        Rappresentazione instance = null;
        BaseEntity entity = AQuery.find(Rappresentazione.class, (long)id);

        if (entity != null) {
            if (entity instanceof Rappresentazione) {
                instance = (Rappresentazione) entity;
            }// end of if cycle
        }// end of if cycle

        return instance;
    }// end of method

    @Override
    public String toString() {
        return getEvento().toString() + " - " + getDateAsString(getDataRappresentazione());
    }// end of method

    /**
     * Ritorna una data nel formato adeguato per una Rappresentazione (nomegiorno-giorno-mese-ammo-ora-minuto)
     */
    public String getDateAsString() {
        return Rappresentazione.getDateAsString(dataRappresentazione);
    }// end of method

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public Date getDataRappresentazione() {
        return dataRappresentazione;
    }

    public void setDataRappresentazione(Date dataRappresentazione) {
        this.dataRappresentazione = dataRappresentazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Insegnante> getInsegnanti() {
        return insegnanti;
    }

    public void setInsegnanti(List<Insegnante> insegnanti) {
        this.insegnanti = insegnanti;
    }


    /**
     * Ritorna una stringa con data e disponibilità
     * da visualizzare in alcuni popup
     */
    public String getDataEtDisponibilita(){

        String sData = getDateAsString();
        Sala sala = getSala();
        String sSala="";
        if (sala!=null){
            sSala=sala.toString();
        }
        EntityManager em = EM.createEntityManager();
        int disponibili=RappresentazioneModulo.countPostiDisponibili(this, em);
        em.close();
        String s = sData+" "+sSala+" - disp: "+disponibili;

        return s;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rappresentazione that = (Rappresentazione) o;

        if (evento != null ? !evento.equals(that.evento) : that.evento != null) return false;
        if (sala != null ? !sala.equals(that.sala) : that.sala != null) return false;
        return !(dataRappresentazione != null ? !dataRappresentazione.equals(that.dataRappresentazione) : that.dataRappresentazione != null);

    }

    @Override
    public int hashCode() {
        int result = evento != null ? evento.hashCode() : 0;
        result = 31 * result + (sala != null ? sala.hashCode() : 0);
        result = 31 * result + (dataRappresentazione != null ? dataRappresentazione.hashCode() : 0);
        return result;
    }


    //	/**
//	 * Ritorna l'importo totale di una prenotazione per una data rappresentazione
//	 * @deprecated
//	 * @see Prenotazione
//	 * <p>
//	 *
//	 * @param rappresentazione
//	 *            la rappresentazione
//	 * @param nInteri
//	 *            il numero di posti interi
//	 * @param nRidotti
//	 *            il numero di posti ridotti
//	 * @return l'importo totale
//	 */
//	public static BigDecimal getTotImporto(Rappresentazione rappresentazione, int nInteri, int nRidotti) {
//		BigDecimal totImporto = new BigDecimal(0);
//		if (rappresentazione != null) {
//			Evento evento = rappresentazione.getEvento();
//			if (evento != null) {
//				BigDecimal iIntero = evento.getImportoIntero();
//				BigDecimal iRidotto = evento.getImportoRidotto();
//
//				BigDecimal totInteri = iIntero.multiply(new BigDecimal(nInteri));
//				BigDecimal totRidotti = iRidotto.multiply(new BigDecimal(nRidotti));
//
//				totImporto = totInteri.add(totRidotti);
//			}
//		}
//		return totImporto;
//	}// end of method

    /**
     * Ritorna tutte le prenotazioni relative a questa rappresentazione
     * @param congelate true ritorna anche quelle congelate, false le esclude
     * @return le prenotazioni
     */
    public Prenotazione[] getPrenotazioni(boolean congelate){

        EntityManager entityManager = EM.createEntityManager();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Prenotazione.class);
        Root<Prenotazione> root = cq.from(Prenotazione.class);

        cq.select(root);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get(Prenotazione_.rappresentazione), this));
        if(!congelate){
            predicates.add(cb.equal(root.get(Prenotazione_.congelata), false));
        }
        cq.where(predicates.toArray(new Predicate[]{}));

        TypedQuery query = entityManager.createQuery(cq);

        final List<Prenotazione> entities = query.getResultList();

        entities.forEach(entityManager::detach);

        return entities.toArray(new Prenotazione[0]);

    }

}// end of entity class
