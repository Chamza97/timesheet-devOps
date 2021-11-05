package tn.esprit.spring.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import org.aspectj.weaver.Iterators;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import tn.esprit.spring.entities.Contrat;
import tn.esprit.spring.entities.Departement;
import tn.esprit.spring.entities.Employe;
import tn.esprit.spring.entities.Role;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IEntrepriseService;
import tn.esprit.spring.services.ITimesheetService;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EmployeeTest {
	@Autowired
	IEmployeService iemployeservice;
	@Autowired
	IEntrepriseService ientrepriseservice;
	@Autowired
	ITimesheetService itimesheetservice;
	@Autowired
	EmployeRepository employeRepository;
	
	@Autowired
	DepartementRepository departementRepo;
	@Autowired
	ContratRepository contratRepo;
	
	
	Departement departement;
	Contrat contrat;
	Employe emplyee;
	private static final Logger L = LogManager.getLogger(EmployeeTest.class);
	
	@Before
	public void setUp(){
		java.util.List<Employe> listEmployees = iemployeservice.getAllEmployes();
		if( iemployeservice.getAllEmployes().size() != 0) {
			this.employeRepository.deleteAll();
		};
		departementRepo.deleteAll();
		Departement dep = new Departement();
		dep.setName("info");
		this.departement =  this.departementRepo.save(dep);
		Employe employe = new Employe();
		employe.setActif(true);
		employe.setEmail("chamza9@gmail.com");
		employe.setNom("hamza");
		employe.setPrenom("chaouachi");
		employe.setRole(Role.ADMINISTRATEUR);
		 this.iemployeservice.ajouterEmploye(employe);
		this.emplyee = employe;
		
		try {
			Contrat contrat  = new Contrat();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date d = dateFormat.parse("2015-03-23");
			contrat.setDateDebut(d);
			contrat.setSalaire(2000);
			contrat.setTypeContrat("CDI");
			this.contrat = this.contratRepo.save(contrat);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	@Test
	public void testGetAllEmployes() {
		java.util.List<Employe> listEmployees = iemployeservice.getAllEmployes();
		assertEquals(1, listEmployees.size());
	}
	@Test
	public void testAjouterEmploye() {
		Employe employe = new Employe();
		employe.setActif(true);
		employe.setEmail("chamza97@gmail.com");
		employe.setNom("hamza");
		employe.setPrenom("chaouachi");
		employe.setRole(Role.ADMINISTRATEUR);
		int id = this.iemployeservice.ajouterEmploye(employe);
		assertTrue(id > 0); 
		
		
	}
	@Test
	public void testMettreAjourEmailByEmployeId() {
		
		this.iemployeservice.mettreAjourEmailByEmployeId("hamza.chaouachi@esprit.tn", this.emplyee.getId());
		Employe employe =  iemployeservice.getAllEmployes().get(0);
		assertEquals(employe.getEmail(),"hamza.chaouachi@esprit.tn" ); 
	}
	@Test
	public void testGetEmployePrenomById() {
		
		String prenom = this.iemployeservice.getEmployePrenomById(this.emplyee.getId());
		assertEquals(prenom,"chaouachi" );
	}
	
	@Test
	public void testAjouterContrat() {
		
		try {
			Contrat contrat  = new Contrat();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date d = dateFormat.parse("2015-03-23");
			contrat.setDateDebut(d);
			contrat.setSalaire(2500);
			contrat.setTypeContrat("CDD");
			this.contratRepo.save(contrat);
			Contrat c  = this.contratRepo.save(contrat);
			assertTrue(c.getReference()> 0);
			assertNotNull(this.contrat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void affecterContratEmploye() {
		this.iemployeservice.affecterContratAEmploye(this.contrat.getReference(), this.emplyee.getId());
		Contrat  c = this.contratRepo.findById(this.contrat.getReference()).get();
		assertTrue(c.getEmploye().getId() == this.emplyee.getId());
	}
	@Test
	public void affecterEmployeADepartement() {
		this.iemployeservice.affecterEmployeADepartement(this.emplyee.getId(), this.departement.getId());
		Employe employe = this.employeRepository.findById(this.emplyee.getId()).get();
		boolean employeeAffecte =false;
		for(Departement dep : employe.getDepartements()) {
			if(dep.getId() == this.departement.getId()) {
				employeeAffecte = true;
			}
		}
		assertTrue(employeeAffecte);
	}
	@Test
	public void deAffecterEmployeADepartement() {
		this.iemployeservice.desaffecterEmployeDuDepartement(this.emplyee.getId(),this.departement.getId());
		Employe employe = this.employeRepository.findById(this.emplyee.getId()).get();
		boolean employeeAffecte =false;
		for(Departement dep : employe.getDepartements()) {
			if(dep.getId() == this.departement.getId()) {
				employeeAffecte = true;
			}
		}
		assertFalse(employeeAffecte);
	}
	@Test
	public void deleteContratById() {
		this.iemployeservice.deleteContratById(this.contrat.getReference());
		assertTrue(this.contratRepo.findById(this.contrat.getReference()).isEmpty());
		
	}
	@Test
	public void deleteEmployeById()
	{
		this.iemployeservice.deleteEmployeById(this.emplyee.getId());
		assertTrue(this.employeRepository.findById(this.emplyee.getId()).isEmpty());
		
	}

	
	
	
	
	
}
