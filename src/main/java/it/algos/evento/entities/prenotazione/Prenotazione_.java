package it.algos.evento.entities.prenotazione;

import it.algos.evento.entities.insegnante.Insegnante;
import it.algos.evento.entities.modopagamento.ModoPagamento;
import it.algos.evento.entities.rappresentazione.Rappresentazione;
import it.algos.evento.entities.scuola.Scuola;
import it.algos.evento.entities.tiporicevuta.TipoRicevuta;
import it.algos.webbase.multiazienda.CompanyEntity_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.util.Date;

@StaticMetamodel(Prenotazione.class)
public class Prenotazione_ extends CompanyEntity_ {

	public static volatile SingularAttribute<Prenotazione, Integer> numPrenotazione;
	public static volatile SingularAttribute<Prenotazione, String> uuid;
	public static volatile SingularAttribute<Prenotazione, Rappresentazione> rappresentazione;
	public static volatile SingularAttribute<Prenotazione, Date> dataPrenotazione;
	public static volatile SingularAttribute<Prenotazione, Scuola> scuola;
	public static volatile SingularAttribute<Prenotazione, Insegnante> insegnante;
	public static volatile SingularAttribute<Prenotazione, String> classe;
	public static volatile SingularAttribute<Prenotazione, String> telRiferimento;
	public static volatile SingularAttribute<Prenotazione, String> emailRiferimento;
	
	public static volatile SingularAttribute<Prenotazione, Integer> numInteri;
	public static volatile SingularAttribute<Prenotazione, Integer> numRidotti;
	public static volatile SingularAttribute<Prenotazione, Integer> numDisabili;
	public static volatile SingularAttribute<Prenotazione, Integer> numAccomp;
	public static volatile SingularAttribute<Prenotazione, Integer> numTotali;
	
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoIntero;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoRidotto;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoDisabili;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoAccomp;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoGruppo;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoDaPagare;
	
	public static volatile SingularAttribute<Prenotazione, ModoPagamento> modoPagamento;
	public static volatile SingularAttribute<Prenotazione, Date> scadenzaPagamento;
	public static volatile SingularAttribute<Prenotazione, Integer> livelloSollecitoPagamento;
	public static volatile SingularAttribute<Prenotazione, Boolean> pagamentoConfermato;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoPagato;
	public static volatile SingularAttribute<Prenotazione, Date> dataPagamentoConfermato;
	public static volatile SingularAttribute<Prenotazione, TipoRicevuta> tipoRicevuta;

	public static volatile SingularAttribute<Prenotazione, String> note;
	public static volatile SingularAttribute<Prenotazione, Date> scadenzaConferma;
	public static volatile SingularAttribute<Prenotazione, Integer> livelloSollecitoConferma;
	public static volatile SingularAttribute<Prenotazione, Boolean> congelata;
	public static volatile SingularAttribute<Prenotazione, Boolean> confermata;
	public static volatile SingularAttribute<Prenotazione, Date> dataConferma;
	public static volatile SingularAttribute<Prenotazione, Boolean> pagamentoRicevuto;
	public static volatile SingularAttribute<Prenotazione, Date> dataPagamentoRicevuto;
	public static volatile SingularAttribute<Prenotazione, Boolean> privato;

	public static volatile SingularAttribute<Prenotazione, Boolean> richiestoBus;
	public static volatile SingularAttribute<Prenotazione, String> dettagliBus;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoBus;
	public static volatile SingularAttribute<Prenotazione, Boolean> pagatoBus;

	public static volatile SingularAttribute<Prenotazione, Boolean> richiestoLab;
	public static volatile SingularAttribute<Prenotazione, String> dettagliLab;
	public static volatile SingularAttribute<Prenotazione, BigDecimal> importoLab;
	public static volatile SingularAttribute<Prenotazione, Boolean> pagatoLab;


}// end of entity class
