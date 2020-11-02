package it.algos.evento.entities.spedizione;

import it.algos.evento.entities.lettera.Lettera;
import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.DefaultSort;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@DefaultSort({"dataSpedizione"})
public class Spedizione extends CompanyEntity {

	private static final long serialVersionUID = 5331901372570434340L;

	@ManyToOne
	private Lettera lettera;

	private String destinatario = "";

	private String operatore = "";

	private boolean spedita = false;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataSpedizione;

	private String errore;

	public Spedizione() {
		this(null, null);
	}// end of constructor

	public Spedizione(Lettera lettera, String destinatario) {
		this(lettera, destinatario, null);
	}// end of constructor

	public Spedizione(Lettera lettera, String destinatario, String operatore) {
		super();
		this.setLettera(lettera);
		this.setDestinatario(destinatario);
		this.setOperatore(operatore);
		this.setDataSpedizione(new Date());
	}// end of constructor

	@Override
	public String toString() {
		return getDestinatario();
	}// end of method

	/**
	 * @return the lettera
	 */
	public Lettera getLettera() {
		return lettera;
	}

	/**
	 * @param lettera
	 *            the lettera to set
	 */
	public void setLettera(Lettera lettera) {
		this.lettera = lettera;
	}

	/**
	 * @return the destinatario
	 */
	public String getDestinatario() {
		return destinatario;
	}

	/**
	 * @param destinatario
	 *            the destinatario to set
	 */
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	/**
	 * @return the operatore
	 */
	public String getOperatore() {
		return operatore;
	}

	/**
	 * @param operatore
	 *            the operatore to set
	 */
	public void setOperatore(String operatore) {
		this.operatore = operatore;
	}

	/**
	 * @return the spedita
	 */
	public boolean isSpedita() {
		return spedita;
	}

	/**
	 * @param spedita
	 *            the spedita to set
	 */
	public void setSpedita(boolean spedita) {
		this.spedita = spedita;
	}

	/**
	 * @return the dataSpedizione
	 */
	public Date getDataSpedizione() {
		return dataSpedizione;
	}

	/**
	 * @param dataSpedizione
	 *            the dataSpedizione to set
	 */
	public void setDataSpedizione(Date dataSpedizione) {
		this.dataSpedizione = dataSpedizione;
	}

	/**
	 * @return the errore
	 */
	public String getErrore() {
		return errore;
	}

	/**
	 * @param errore
	 *            the errore to set
	 */
	public void setErrore(String errore) {
		this.errore = errore;
	}

}// end of entity class
