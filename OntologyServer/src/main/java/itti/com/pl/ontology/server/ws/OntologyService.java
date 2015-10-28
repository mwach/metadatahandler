package itti.com.pl.ontology.server.ws;

import itti.com.pl.ontology.server.ws.bean.DependenciesList;
import itti.com.pl.ontology.server.ws.bean.MetadataObject;
import itti.com.pl.ontology.server.ws.bean.TSINodeType;
import itti.com.pl.ontology.server.ws.bean.TypeOfObject;

import javax.jws.WebService;

import org.springframework.stereotype.Service;

@WebService(endpointInterface = "itti.com.pl.ontology.server.ws.OntologyWS")
@Service
public class OntologyService implements OntologyWS {

	@Override
	public boolean setTSINodeType(TSINodeType tsiNodeType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registerMetadataObject(TypeOfObject typeOfObject,
			MetadataObject metadataObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateMetadataObject(TypeOfObject typeOfObject,
			MetadataObject metadataObject) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MetadataObject searchMetadata(TypeOfObject typeOfObject, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetadataObject getMetadata(String objectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MetadataObject readMetadata(String objectId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependenciesList getDependencies(String objectId) {
		// TODO Auto-generated method stub
		return null;
	}

}