package it.algos.evento.entities.comune;

import it.algos.webbase.multiazienda.CompanyEntity;
import it.algos.webbase.web.entity.DefaultSort;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
@DefaultSort({"nome"})
public class Comune extends CompanyEntity {

	private static final long serialVersionUID = 2134838403438690707L;

	@NotNull
	private String nome;


	private String siglaProvincia;

	public Comune() {
		this("", "");
	}// end of constructor

	public Comune(String nome, String siglaProvincia) {
		super();
		this.setNome(nome);
		this.setSiglaProvincia(siglaProvincia);

	}// end of constructor

	@Override
	public String toString() {
		String stringa = "";
		stringa = nome;
		if (!siglaProvincia.equals("")) {
			stringa += " (" + this.siglaProvincia + ")";
		}// end of if cycle

		return stringa;
	}// end of method


	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

}// end of entity class
