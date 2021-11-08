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
import java.util.List;
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
import tn.esprit.spring.entities.Timesheet;
import tn.esprit.spring.entities.Mission;
import tn.esprit.spring.repository.ContratRepository;
import tn.esprit.spring.repository.DepartementRepository;
import tn.esprit.spring.repository.EmployeRepository;
import tn.esprit.spring.repository.MissionRepository;
import tn.esprit.spring.services.IEmployeService;
import tn.esprit.spring.services.IEntrepriseService;
import tn.esprit.spring.services.ITimesheetService;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimesheetTest {
	@Autowired
	IEmployeService iemployeservice;
	@Autowired
	IEntrepriseService ientrepriseservice;
	@Autowired
	ITimesheetService itimesheetservice;
	@Autowired
	EmployeRepository employeRepository;
	@Autowired
	MissionRepository missionRepository;
	
	@Autowired
	DepartementRepository departementRepo;
	@Autowired
	ContratRepository contratRepo;
	
	
	Departement departement;
	Contrat contrat;
	Employe emplyee;
	Mission mission;
	private static final Logger L = LogManager.getLogger(TimesheetTest.class);
	
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
		employe.setEmail("amine.guesmi@esprit.tn");
		employe.setNom("guesmi");
		employe.setPrenom("amine");
		employe.setRole(Role.ADMINISTRATEUR);
		 this.iemployeservice.ajouterEmploye(employe);
		this.emplyee = employe;
		
		Mission m = new Mission();
		m.setName("mission1");
		m.setDescription("mission1 desc");
		m.setDepartement(dep);
		this.itimesheetservice.ajouterMission(mission);
		this.mission = m;
		
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
	
	/********************************************/
	
	@Test
	public void testAjouterTimesheet() {
		Date db = new Date();
		Date df = new Date();
		this.itimesheetservice.ajouterTimesheet(this.mission.getId() , this.emplyee.getId() , db, df);
	
	}
	/********************************************/
	@Test
	public void testAffecterMissionADepartement() {
		this.itimesheetservice.affecterMissionADepartement(this.mission.getId(), this.departement.getId());
		Mission  m = this.missionRepository.findById(this.mission.getId()).get();
		assertTrue(m.getDepartement().getId() == this.departement.getId());
	}
	/********************************************/
	@Test
	public void testValiderTimesheet() {
		
		int missionId = this.mission.getId();
		int employeId = this.emplyee.getId();
		Date dateDebut = new Date();
		Date dateFin = new Date();
		int validateurId = 1;
		
		 this.itimesheetservice.validerTimesheet(missionId, employeId, dateDebut, dateFin, validateurId);
	}
	/********************************************/

	
}
