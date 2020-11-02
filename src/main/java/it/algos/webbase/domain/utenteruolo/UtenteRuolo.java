package it.algos.webbase.domain.utenteruolo;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.vaadin.data.Container.Filter;

import it.algos.webbase.domain.ruolo.Ruolo;
import it.algos.webbase.domain.utente.Utente;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import it.algos.webbase.web.query.FilterFactory;

import java.util.ArrayList;
import java.util.Vector;

@Entity
public class UtenteRuolo extends BaseEntity {

	private static final long serialVersionUID = 7452557596156158519L;

	@ManyToOne
	@NotNull
	private Utente utente;

	@ManyToOne
	@NotNull
	private Ruolo ruolo;

	public UtenteRuolo() {
		this((Utente) null, (Ruolo) null);
	}// end of constructor

	public UtenteRuolo(String nomeUtente, String nomeRuolo) {
		this(Utente.read(nomeUtente), Ruolo.read(nomeRuolo));
	}// end of constructor

	public UtenteRuolo(Utente utente, Ruolo ruolo) {
		super();
		this.setUtente(utente);
		this.setRuolo(ruolo);
	}// end of constructor

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Ruolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(Ruolo ruolo) {
		this.ruolo = ruolo;
	}

	public synchronized static ArrayList<UtenteRuolo> findUtente(Utente utente) {
		ArrayList<UtenteRuolo> lista;
		Vector vettore = (Vector) AQuery.queryList(UtenteRuolo.class, UtenteRuolo_.utente,utente);
		lista = new ArrayList<UtenteRuolo>(vettore);
		return lista;
	}// end of method

	/**
	 * Recupera il record usando la query specifica
	 * 
	 * @return utenteruolo, null se non trovato
	 */
	public static UtenteRuolo read(Utente utente, Ruolo ruolo) {
		UtenteRuolo instance = null;
		Filter filtroUtente = FilterFactory.create(UtenteRuolo_.utente, utente);
		Filter filtroRuolo = FilterFactory.create(UtenteRuolo_.ruolo, ruolo);
		BaseEntity entity = AQuery.getEntity(UtenteRuolo.class, filtroUtente, filtroRuolo);

		if (entity != null) {
			if (entity instanceof UtenteRuolo) {
				instance = (UtenteRuolo) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method

}// end of entity class
