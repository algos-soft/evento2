package it.algos.evento.entities.lettera;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.Compare;
import it.algos.evento.entities.lettera.allegati.Allegato;
import it.algos.evento.entities.lettera.allegati.Allegato_;
import it.algos.webbase.domain.company.BaseCompany;
import it.algos.webbase.web.entity.BaseEntity;
import it.algos.webbase.web.query.AQuery;
import org.apache.commons.mail.DataSourceResolver;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageResolver implements DataSourceResolver {

	private BaseCompany company;

	public ImageResolver(BaseCompany company) {
		this.company = company;
	}

	@Override
	public DataSource resolve(String resourceLocation) throws IOException {
		ByteArrayDataSource bds=null;
		Container.Filter f1 = new Compare.Equal(Allegato_.name.getName(), resourceLocation);
		Container.Filter f2 = new Compare.Equal(Allegato_.company.getName(), company);
		Container.Filter filter = new And(f1, f2);
		List<? extends BaseEntity> listAllegati = AQuery.getList(Allegato.class, filter);
		if(listAllegati.size()==1){
			BaseEntity entity=listAllegati.get(0);
			Allegato allegato = (Allegato) entity;
			bds = new ByteArrayDataSource(allegato.getContent(), allegato.getMimeType());
			bds.setName(allegato.getName());
		}
		return bds;
	}

	@Override
	public DataSource resolve(String resourceLocation, boolean isLenient) throws IOException {
		return resolve(resourceLocation);
	}

}
