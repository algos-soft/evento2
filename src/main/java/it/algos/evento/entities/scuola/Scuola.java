package it.algos.evento.entities.scuola;

import it.algos.evento.entities.comune.Comune;
import it.algos.evento.entities.ordinescuola.OrdineScuola;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.entity.DefaultSort;
import it.algos.webbase.web.query.AQuery;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@DefaultSort({"sigla"})
public class Scuola extends CompanyEntity {

	private static final long serialVersionUID = 5063889291780182288L;

	@NotEmpty
	private String sigla;

	@NotEmpty
	private String nome;

	@ManyToOne
	@NotNull
	private OrdineScuola ordine;

	private String tipo;

	@ManyToOne
	private Comune comune;

	private String indirizzo;
	private String cap;
	private String telefono;
	private String fax;
	private String email;

	@Lob
	private String note;

	public Scuola() {
		this("");
	}// end of constructor

	public Scuola(String sigla) {
		this(sigla, "", null, null);
	}// end of constructor

//	public Scuola(String sigla, String nome) {
//		this(sigla, nome, (Comune) null);
//	}// end of constructor

//	public Scuola(String sigla, String nome, Comune comune) {
//		this(sigla, nome, comune, OrdineScuolaEnumOld.superiore.getId());
//	}// end of constructor

	public Scuola(String sigla, String nome, OrdineScuola ordine) {
		this(sigla, nome, (Comune) null, ordine);
	}// end of constructor

	public Scuola(String sigla, String nome, Comune comune, OrdineScuola ordine) {
		super();
		this.setSigla(sigla);
		this.setNome(nome);
		this.setOrdine(ordine);
		this.setComune(comune);
	}// end of constructor

	@Override
	public String toString() {
		String stringa = getSigla();
		if (nome != null) {
			stringa += " - " + getNome();
		}
		if (comune != null) {
			stringa += " - " + comune.toString();
		}
		return stringa;
	}// end of method

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public OrdineScuola getOrdine() {
		return ordine;
	}

	public void setOrdine(OrdineScuola ordine) {
		this.ordine = ordine;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public Comune getComune() {
		return comune;
	}

	public void setComune(Comune comune) {
		this.comune = comune;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public static Scuola read(long id) {
		Scuola instance = null;
		BaseEntity entity = AQuery.find(Scuola.class, id);

		if (entity != null) {
			if (entity instanceof Scuola) {
				instance = (Scuola) entity;
			}// end of if cycle
		}// end of if cycle

		return instance;
	}// end of method


}// end of entity class
