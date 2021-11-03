package tn.esprit.spring.controller;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Entreprise;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EntrepriseRepository;
import tn.esprit.spring.services.IEntrepriseService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EntrepriseTest {
	@Autowired
	IEntrepriseService ientrepriseservice;
	Entreprise entreprsise;
	Departement departement;
	@Autowired
	DepartementRepository departementRepo;
	@Autowired
	EntrepriseRepository EntrepriseRepository;
	private static final Logger L = LogManager.getLogger(EmployeeTest.class);
	@Before
	public void setUp(){
		
		departementRepo.deleteAll();
		Departement dep = new Departement();
		dep.setName("info");
		this.departement =  this.departementRepo.save(dep);
		Entreprise entreprise = new Entreprise();
		entreprise.setName("jihene khouja");
		entreprise.setRaisonSocial("celeb");
		this.ientrepriseservice.ajouterEntreprise(entreprise);
		this.entreprsise = entreprise;
		java.util.List<String> listdepartement = ientrepriseservice.getAllDepartementsNamesByEntreprise(this.entreprsise.getId());
		if( ientrepriseservice.getAllDepartementsNamesByEntreprise(this.entreprsise.getId()).size() != 0) {
			this.departementRepo.deleteAll();
		};
		
	}
	@Test
	public void testAjouterEntreprise() {
		Entreprise entreprise = new Entreprise();
		entreprise.setName("jihene khouja");
		entreprise.setRaisonSocial("celeb");
		int id = this.ientrepriseservice.ajouterEntreprise(entreprise);
		assertTrue(id > 0); 	
		
	}
	@Test
	public void testAjouterdepartement() {
		Departement departement = new Departement();
		departement.setName("jihene khouja");
		int id = this.ientrepriseservice.ajouterDepartement(departement);
		assertTrue(id > 0); 	
		
	}
	@Test
	public void affecterDepartementAEntreprise() {
		this.ientrepriseservice.affecterDepartementAEntreprise(this.departement.getId(), this.entreprsise.getId());
		Departement  D = this.departementRepo.findById(this.departement.getId()).get();
		assertTrue(D.getEntreprise().getId() == this.entreprsise.getId());
	}
	@Test
	public void getAllDepartementsNamesByEntreprise() {
		java.util.List<String> listdepartement = ientrepriseservice.getAllDepartementsNamesByEntreprise(this.entreprsise.getId());
		assertEquals(0, listdepartement.size());
	}
	@Test
	public void deleteEntrepriseById() {
		this.ientrepriseservice.deleteEntrepriseById(this.entreprsise.getId());
		assertTrue(!this.EntrepriseRepository.findById(this.entreprsise.getId()).isPresent());
		
	}
	@Test
	public void deleteDepartementById() {
		this.ientrepriseservice.deleteDepartementById(this.departement.getId());
		assertTrue(!this.departementRepo.findById(this.departement.getId()).isPresent());
		
	}
	
}
