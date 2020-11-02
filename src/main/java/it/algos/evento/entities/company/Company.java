package it.algos.evento.entities.company;

import it.algos.evento.demo.DemoDataGenerator;
import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.destinatario.Destinatario;
import it.algos.evento.entities.evento.Evento;
import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.lettera.Lettera;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.mailing.Mailing;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.evento.entities.prenotazione.Prenotazione;
import it.algos.evento.entities.prenotazione.eventi.EventoPren;
import it.algos.evento.entities.progetto.Progetto;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.sala.Sala;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.spedizione.Spedizione;
import it.algos.evento.entities.stagione.Stagione;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.evento.pref.PrefEventoEntity;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.domain.utenteruolo.UtenteRuolo;
import it.algos.webbase.multiazienda.CompanyEntity_;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.EntityQuery;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


@Entity
@DefaultSort({"companyCode"})
public class Company extends BaseCompany {

	private static final long serialVersionUID = 8238775575826490450L;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Comune> comuni;

    @OneToMany(mappedBy = "company", targetEntity=Evento.class)
    @CascadeOnDelete
    private List<Evento> eventi;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Insegnante> insegnanti;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Lettera> lettere;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Allegato> allegati;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<ModoPagamento> modiPagamento;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Progetto> progetti;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Sala> sale;

    @OneToMany(mappedBy = "company")
    @CascadeOnDelete
    private List<Scuola> scuola;

    @OneToMany(mappedBy = "company", targetEntity=PrefEventoEntity.class)
    @CascadeOnDelete
    private List<PrefEventoEntity> prefs;

	public Company() {
		super();
	}// end of constructor

	public static EntityQuery<Company> query = new EntityQuery(Company.class);


	public void createDemoData(){
		DemoDataGenerator.createDemoData(this);
	};
	
	/**
	 * Elimina tutti i dati di questa azienda.
	 * <p>
	 * L'ordine di cancellazione è critico per l'integrità referenziale
	 */
	public void deleteAllData(){

		// elimina le tabelle
		AQuery.delete(Spedizione.class, CompanyEntity_.company, this);
		AQuery.delete(Lettera.class, CompanyEntity_.company, this);
		AQuery.delete(EventoPren.class, CompanyEntity_.company, this);
		AQuery.delete(Prenotazione.class, CompanyEntity_.company, this);
		AQuery.delete(TipoRicevuta.class, CompanyEntity_.company, this);
		AQuery.delete(Rappresentazione.class,  CompanyEntity_.company, this);
		AQuery.delete(Sala.class,  CompanyEntity_.company, this);
		AQuery.delete(Evento.class,  CompanyEntity_.company, this);
		AQuery.delete(Stagione.class,  CompanyEntity_.company, this);
		AQuery.delete(Progetto.class,  CompanyEntity_.company, this);
		AQuery.delete(ModoPagamento.class,  CompanyEntity_.company, this);
		AQuery.delete(Insegnante.class,  CompanyEntity_.company, this);
		AQuery.delete(Scuola.class,  CompanyEntity_.company, this);
		AQuery.delete(OrdineScuola.class,  CompanyEntity_.company, this);
		AQuery.delete(Comune.class,  CompanyEntity_.company, this);
		AQuery.delete(Destinatario.class,  CompanyEntity_.company, this);
		AQuery.delete(Mailing.class,  CompanyEntity_.company, this);
		AQuery.delete(PrefEventoEntity.class, CompanyEntity_.company, this);

		// elimina gli utenti
		AQuery.delete(UtenteRuolo.class, CompanyEntity_.company, this);
		AQuery.delete(Ruolo.class, CompanyEntity_.company, this);
		AQuery.delete(Utente.class, CompanyEntity_.company, this);

		// elimina le preferenze
		AQuery.delete(PrefEventoEntity.class, CompanyEntity_.company, this);

	}


}// end of entity class



