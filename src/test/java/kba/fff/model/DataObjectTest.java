package kba.fff.model;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DataObjectTest {
	
	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	@Test
	public void fooTest() {
		DataObject dObj = new DataObject();		
		dObj.setLabel("fnork");
		log.debug(dObj.getTerseTurtle());
	}
	
//	@Test
//	public void alibabaTest() throws RepositoryException, RepositoryConfigException, RDFHandlerException {
//		// create a repository
//		Repository store = new SailRepository(new MemoryStore());
//		store.initialize();
//
//		// wrap in an object repository
//		ObjectRepositoryFactory factory = new ObjectRepositoryFactory();
//		ObjectRepository repository = factory.createRepository(store);
//		
//		DataObject dOb = new DataObject();
//		dOb.setSize(10);
//		
//
//		// add a Document to the repository
//		ObjectConnection con = repository.getConnection();
//		ValueFactory vf = con.getValueFactory();
//		URI id = vf.createURI("http://example.com/data/2012/getting-started");
//		con.addObject(id, dOb);
//		
//		PrintWriter out = new PrintWriter(System.out);
//		RDFWriter wr = Rio.createWriter(RDFFormat.TURTLE, out);
//		wr.startRDF();
//
//	}

}
