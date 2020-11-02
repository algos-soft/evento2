package it.algos.webbase.multiazienda;

import com.vaadin.data.Container;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.module.ModulePop;
import it.algos.webbase.web.table.ModuleTable;

/**
 * Una ModuleTable con Container già filtrato sulla Company corrente
 */
@SuppressWarnings("serial")
public class ETable extends ModuleTable{

	public ETable(ModulePop module) {
		super(module);

		// if there is a current company set the cookie prefix for the company
		BaseCompany company = CompanySessionLib.getCompany();
		if(company!=null){
			setCookiePrefix(company.getName());
		}
	}
	
//	public ETable(Class<?> entityClass) {
//		super(entityClass);
//	}

	/**
	 * Creates the container
	 * <p>
	 * 
	 * @return un container RW filtrato sulla azienda corrente
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Container createContainer() {
		Class<CompanyEntity> entityClass = (Class<CompanyEntity>)getEntityClass();
		BaseCompany company = CompanySessionLib.getCompany();
		ELazyContainer entityContainer = new ELazyContainer(getEntityManager(), entityClass, getContainerPageSize() , company);
		return entityContainer;
	}// end of method


	/**
	 * Ritorna il numero di record di competenza della azienda corrente
	 * presenti nella domain class di questa tabella
	 */
	@SuppressWarnings("unchecked")
	public long getTotalRows() {
		Class<CompanyEntity> entityClass = (Class<CompanyEntity>)getEntityClass();
		return CompanyQuery.getCount(entityClass);
	}// end of method

}
