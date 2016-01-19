package itti.com.pl.ontology.server.ws;

import java.util.UUID;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import itti.com.pl.ontology.common.dto.Device;
import itti.com.pl.ontology.common.dto.Laptop;
import itti.com.pl.ontology.common.dto.MetadataObject;
import itti.com.pl.ontology.common.dto.Network;
import itti.com.pl.ontology.common.dto.Service;
import itti.com.pl.ontology.common.dto.TSINodeType;
import itti.com.pl.ontology.common.dto.TypeOfObject;
import itti.com.pl.ontology.common.exception.OntologyException;
import itti.com.pl.ontology.common.exception.OntologyRuntimeException;
import itti.com.pl.ontology.core.exception.ErrorMessages;
import itti.com.pl.ontology.core.ontology.LocalOntologyRepository;
import itti.com.pl.ontology.core.ontology.OntologyManager;
import itti.com.pl.ontology.server.Constants.Property;

public class MetadataHandlerTest {

	private static MetadataHandler metadataHandler;

	@BeforeClass
	public static void beforeClass() throws OntologyException {
		metadataHandler = new MetadataHandler();
		OntologyManager ontology = (OntologyManager) new LocalOntologyRepository("src/test/resources/")
				.loadOntology("aao_service.owl");
		ontology.setOntologyNamespace("http://afro.wil.waw.pl/AAO.owl#");
		metadataHandler.setOntology(ontology);
	}

	@Test
	public void testTsiNodeType() {

		TSINodeType nodeType = TSINodeType.TN_D;

		Service service = new Service();
		service.setName("tsiNodeTypeService");
		metadataHandler.registerMetadataObject(TypeOfObject.Service, service);
		MetadataObject metadata = metadataHandler.getMetadata(service.getName());
		assertEquals(service, metadata);
		assertNull(metadata.getProperties().get(Property.TypeOfNode.getOntologyName()));

		metadataHandler.setTSINodeType(nodeType);

		MetadataObject updatedMetadata = metadataHandler.getMetadata(service.getName());
		assertEquals(nodeType.getDescription(), updatedMetadata.getProperties().get(Property.TypeOfNode.getOntologyName()));
	}

	@Test
	public void registerDeviceMetadataSuccess() {

		String deviceName = "rdms_1";

		Device device = new Device();
		device.setType("Laptop");
		device.setDeviceID(UUID.randomUUID().toString());
		device.setName(deviceName);
		device.setHasDisplayProperties("Full_20");
		device.setHasHWProperties("LowCPU_17");
		metadataHandler.registerMetadataObject(TypeOfObject.Device, device);

		MetadataObject metadata = metadataHandler.getMetadata(deviceName);
		assertEquals(device, metadata);
	}

	@Test
	public void registerDeviceMetadataFailure() {

		String deviceName = "rdms_2";
		String propertyValue = "someInvalidValue";
		Device device = new Laptop();
		device.setName(deviceName);
		device.setHasDisplayProperties(propertyValue);
		Exception exc = null;
		try {
			metadataHandler.registerDeviceMetadata(device);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(OntologyRuntimeException.class, exc.getClass());
		assertEquals(
				ErrorMessages.ONTOLOGY_PROPERTY_NOT_FOUND_FOR_INSTANCE.getMessage("hasDisplayProperties", deviceName),
				exc.getMessage());
		exc = null;
		try {
			metadataHandler.getMetadata(deviceName);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(OntologyRuntimeException.class, exc.getClass());
		assertEquals(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(deviceName), exc.getMessage());
	}

	@Test
	public void registerNetworkMetadataSuccess() {

		String NetworkName = "network_A";

		Network network = new Network();
		network.setNetworkAddress("127.0.0.1");
		network.setName(NetworkName);
		network.setNetworkCapacity("1gb");
		network.setNetworkRange("LAN");

		metadataHandler.registerMetadataObject(TypeOfObject.Network, network);

		MetadataObject metadata = metadataHandler.getMetadata(NetworkName);
		assertEquals(network, metadata);
	}

	@Test
	public void registerNetworkMetadataFailure() {

		String networkName = "network_B";

		Network network = new Network();
		network.setNetworkAddress("127.0.0.1");
		network.setName(networkName);
		network.setNetworkCapacity("1gb");
		network.setNetworkRange("MyLan");

		Exception exc = null;
		try {
			metadataHandler.registerMetadataObject(TypeOfObject.Network, network);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(OntologyRuntimeException.class, exc.getClass());
		assertEquals(
				ErrorMessages.ONTOLOGY_PROPERTY_NOT_FOUND_FOR_INSTANCE.getMessage("networkRange", networkName),
				exc.getMessage());

		exc = null;
		try {
			metadataHandler.getMetadata(networkName);
		} catch (Exception e) {
			exc = e;
		}
		assertEquals(OntologyRuntimeException.class, exc.getClass());
		assertEquals(ErrorMessages.ONTOLOGY_INSTANCE_NOT_FOUND.getMessage(networkName), exc.getMessage());
	}

	@Test
	public void searchMetadata(){
		metadataHandler.searchMetadata(TypeOfObject.Device, "hasEncodingProperties=SupportsFI_38");
	}
}
