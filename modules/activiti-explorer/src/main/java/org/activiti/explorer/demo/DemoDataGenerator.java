/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.activiti.explorer.demo;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.attachment.AttachmentCategory;
import org.activiti.engine.attachment.AttachmentService;
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.CostEntry;
import org.activiti.engine.budget.Program;
import org.activiti.engine.budget.Project;
import org.activiti.engine.budget.ProjectCostItem;
import org.activiti.engine.budget.ProjectSourceItem;
import org.activiti.engine.budget.Source;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.util.ClockUtil;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Lab Open Source
 */
public class DemoDataGenerator implements ModelDataJsonConstants {
  
  protected static final Logger LOGGER = LoggerFactory.getLogger(DemoDataGenerator.class);

  protected transient ProcessEngine processEngine;
  protected transient IdentityService identityService;
  protected transient RepositoryService repositoryService;
  protected transient BudgetService budgetService;
  protected transient AttachmentService attachmentService;
  
  protected boolean createDemoUsersAndGroups;
  protected boolean createDemoProcessDefinitions;
  protected boolean createDemoModels;
  protected boolean generateReportData;
  
  public void init() {
    this.identityService = processEngine.getIdentityService();
    this.repositoryService = processEngine.getRepositoryService();
    this.budgetService = processEngine.getBudgetService();
    this.attachmentService = processEngine.getAttachmentService();
    
    if (createDemoUsersAndGroups) {
      LOGGER.info("Initializing demo groups");
      initDemoGroups();
      LOGGER.info("Initializing demo users");
      initDemoUsers();
    }
    
    if (createDemoProcessDefinitions) {
      LOGGER.info("Initializing demo process definitions");
      initProcessDefinitions();
    }
    
//    if (createDemoModels) {
//      LOGGER.info("Initializing demo models");
//      initModelData();
//    }
    
//    if (generateReportData) {
//      LOGGER.info("Initializing demo report data");
//      generateReportData();
//    }
    
    initDemoSources();
    initDemoPrograms();
    initDemoProjects();
    initDemoCostEntries();
    initDemoProjectCostItems();
    initDemoProjectSourceItems();
    initDemoAttachmentCategory();
  }
  
  public void setProcessEngine(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }
  
  public void setCreateDemoUsersAndGroups(boolean createDemoUsersAndGroups) {
    this.createDemoUsersAndGroups = createDemoUsersAndGroups;
  }

  public void setCreateDemoProcessDefinitions(boolean createDemoProcessDefinitions) {
    this.createDemoProcessDefinitions = createDemoProcessDefinitions;
  }

  public void setCreateDemoModels(boolean createDemoModels) {
    this.createDemoModels = createDemoModels;
  }
  
  public void setGenerateReportData(boolean generateReportData) {
    this.generateReportData = generateReportData;
  }

  protected void initDemoGroups() {
    String[] assignmentGroups = new String[] {"dirigenti", "responsabili", "sag", "agi", "app", "cds", "sir", "net","spf","pst","rea","stt","ric","cge","doc","dir" };
    for (String groupId : assignmentGroups) {
      createGroup(groupId, "assignment");
    }
    
    String[] securityGroups = new String[] {"user", "admin"}; 
    for (String groupId : securityGroups) {
      createGroup(groupId, "security-role");
    }
  }
  
  protected void createGroup(String groupId, String type) {
    if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
      Group newGroup = identityService.newGroup(groupId);
      if (groupId.equals("responsabili") || groupId.equals("dirigenti") || type.equals("security-role")) {
    	  newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
      }
      else {
		newGroup.setName(groupId.toUpperCase());
	}
      newGroup.setType(type);
      identityService.saveGroup(newGroup);
    }
  }
  
  protected void initDemoSources(){
	  createSource("000", "F. Cons. ap. 2009", 		new Double(100000));
	  createSource("002", "POR 2007/2013", 			new Double(200000));
	  createSource("003", "Residui Mis. 3.13", 		new Double(300000));
	  createSource("009", "PDL 2009", 				new Double(400000));
	  createSource("010", "PDL 2010", 				new Double(500000));
	  createSource("011", "PDL 2011", 				new Double(600000));
	  createSource("012", "PDL 2012", 				new Double(700000));
	  createSource("014", "Risorse UE", 			new Double(700000));
	  createSource("711", "L.R. 7/07 Disc. 2011", 	new Double(700000));
  }
  
  protected void createSource(String sourceId,String name, Double total){
	  if(budgetService.createSourceQuery().sourceId(sourceId).count() == 0){
		  Source newSource = budgetService.newSource(sourceId);
		  newSource.setName(name);
		  newSource.setTotal(total);
		  budgetService.saveSource(newSource);
	  }
  }
  
  protected void initDemoPrograms(){
	  createProgram("AMMICT", "Ammodernamento infrastrutture ICT (AMMICT) 1", 											new Double(10000));
	  createProgram("AMMIMP", "Ammodernamento impianti PST (AMMIMP)", 													new Double(20000));
	  createProgram("ASSIMP", "Assistenza alle Imprese (ASSIMP)", 														new Double(30000));
	  createProgram("BIOMED", "Distretto Biomed (BIOMED)", 																new Double(40000));
	  createProgram("CLUSER", "Cluster Materiali (CLUSMT)", 															new Double(50000));
	  createProgram("CLUSMT", "Cluster Energie Rinnovabili (CLUSER)", 													new Double(60000));
	  createProgram("DISICT", "Sardegna District (DISICT)", 															new Double(70000));
	  createProgram("DIVULG", "Divulgazione (DIVULG)", 																	new Double(70000));
	  createProgram("LI311A", "Aiuti alle imprese per la produzione di energia da FER (LI311A)", 						new Double(70000));
	  createProgram("LI311C", "Impianti sperimentali di solare termodinamico (LI311C)", 								new Double(70000));
	  createProgram("LI312B", "Smart City (LI312B)", 																	new Double(70000));
	  createProgram("LI312C", "Promozione utilizzo, da parte delle imprese, di tecnologie ad alta efficienza (LI312C)", new Double(70000));
	  createProgram("LI611A", "Promozione e sostegno all'attività di R&S dei poli di innovazione (LI611A)", 			new Double(70000));
	  createProgram("LI621A", "INNOVA.RE (LI621A)", 																	new Double(70000));
	  createProgram("LI621B", "Sostegno alla creazione e sviluppo di nuove imprese innovative (LI621B)", 				new Double(70000));
  }
  
  protected void createProgram(String programId,String name, Double total){
	  if(budgetService.createProgramQuery().programId(programId).count() == 0){
		  Program newProgram = budgetService.newProgram(programId);
		  newProgram.setName(name);
		  newProgram.setTotal(total);
		  budgetService.saveProgram(newProgram);
	  }
  }
  
  protected void initDemoProjects(){
	  createProject("1nii", 		"1° call bando aiuti Nuove Imprese Innovative (1NII)");
	  createProject("1sup", 		"1° call bando aiuti Start Up Innovative (1SUP)");
	  createProject("2nii", 		"2° call bando aiuti Nuove Imprese Innovative (2NII)");
	  createProject("2sup", 		"2° call bando aiuti Start Up Innovative (2SUP)");
	  createProject("aras", 		"Assistenza tecnica RAS per bandi aiuti imprese (ARAS)");
	  createProject("atec", 		"Assistenza tecnica RAS per tecnologie ad alta efficienza (ATEC)");
	  createProject("bapi", 		"Bando Aiuti Premi e Incentivi (BAPI)");
	  createProject("bibl", 		"Biblioteca (BIBL)");
	  createProject("bioe", 		"Centro Bioetica del Mediterraneo (BIOE)");
	  createProject("bisr", 		"Biblioteca Scientifica Regionale (BISR)");
	  createProject("bott", 		"Azioni cluster Bottom Up (BOTT)");
	  createProject("bres", 		"Bando aiuti per progetti R&S (BRES)");
	  createProject("cine", 		"Progetto CINEMA (CINE)");
	  createProject("city", 		"Servizi Smart City (CITY)");
	  createProject("cpro", 		"Centro Prototipazione (CPRO)");
	  createProject("cult", 		"Divulgazione Cultura scientifica (CULT)");
	  createProject("disa", 		"Progetto ICT Disabilità (DISA)");
	  createProject("down", 		"Azioni cluster Top Down (DOWN)");
	  createProject("farm", 		"Cluster R&S Farmaco (FARM)");
	  createProject("form", 		"Borse Formazione imprese (FORM)");
	  createProject("gest_cluser", 	"Gestione piattaforme E.R. (GEST)");
	  createProject("gest_disict", 	"Gestione e sviluppo Distretto (GEST)");
	  createProject("inno", 		"Innovazione e T.T. (INNO)");
	  createProject("irpo", 		"Incentivo Ricerca Polaris (IRPO)");
	  createProject("itli", 		"Impianti Tecnologici Laboratorio Idrogeno (ITLI)");
	  createProject("lcab", 		"Laboratorio Camera Bianca (LCAB)");
	  createProject("lnmr", 		"Laboratorio NMR (LNMR)");
	  createProject("mate", 		"Attività piattaforme Materiali (MATE)");
	  createProject("nano_biomed", 	"Laboratorio Nanobiotecnologie (NANO)");
	  createProject("nano_divulg", 	"Workshop Nanobiotecnologie (NANO)");
	  createProject("parc", 		"Rinnovo impianti PST (PARC)");
	  createProject("pear", 		"Supporto redazione PEAR (PEAR)");
	  createProject("quru", 		"Qualificazione Risorse Umane (QURU)");
	  createProject("reti", 		"Bando Aiuti Innovaz. Reti PMI (RETI)");
	  createProject("rict", 		"Rinnovo infrastrutture ICT (RICT)");
	  createProject("sbre", 		"Sportello Brevetti (SBRE)");
	  createProject("semi", 		"Seminari scientifici (SEMI)");
	  createProject("sinn", 		"Servizi per l'Innovazione (SINN)");
	  createProject("smmo", 		"Smart Mobility (SMMO)");
	  createProject("spap", 		"Sportello Appalti (SPAP)");
	  createProject("spen", 		"Sportello Energia (SPEN)");
	  createProject("spre", 		"Sportello Ricerca Europea (SPRE)");
	  createProject("stab", 		"Laboratorio Stabulazione (STAB)");
	  createProject("svis", 		"Servizio Valutazione  Impianti Sperimentali solari/termodinamici (SVIS)");
  }
  
  protected void createProject(String projectId,String name){
	  if(budgetService.createProjectQuery().projectId(projectId).count() == 0){
		  Project newProject = budgetService.newProject(projectId);
		  newProject.setName(name);
		  budgetService.saveProject(newProject);
	  }
  }
  
  protected void initDemoCostEntries(){
	  createCostEntry("collaboratori", "Collaboratori");
	  createCostEntry("attrezzature", "Attrezzature");
	  createCostEntry("altro", "Altro");
  }
  
  protected void createCostEntry(String costEntryId,String name){
	  if(budgetService.createCostEntryQuery().costEntryId(costEntryId).count() == 0){
		  CostEntry newCostEntry = budgetService.newCostEntry(costEntryId);
		  newCostEntry.setName(name);
		  budgetService.saveCostEntry(newCostEntry);
	  }
  }
  
  protected void initDemoProjectCostItems(){
	  createProjectCostItem("1", "down", "collaboratori", new Double(600), new Double(600));
	  createProjectCostItem("2", "down", "attrezzature", new Double(300), new Double(300));
	  createProjectCostItem("3", "down", "altro", new Double(100), new Double(100));
	  createProjectCostItem("4", "bott", "collaboratori", new Double(1400), new Double(1400));
	  createProjectCostItem("5", "bott", "attrezzature", new Double(600), new Double(600));
  }
  
  protected void createProjectCostItem(String id, String idProject, String idCostEntry, Double total, Double actual){
	  if(budgetService.createProjectCostItemQuery().projectCostItemId(id).count() == 0){
		  ProjectCostItem newProjectCostItem = budgetService.newProjectCostItem(id);
		  newProjectCostItem.setId(id);
		  newProjectCostItem.setIdCostEntry(idCostEntry);
		  newProjectCostItem.setIdProject(idProject);
		  newProjectCostItem.setTotal(total);
		  newProjectCostItem.setActual(actual);
		  budgetService.saveProjectCostItem(newProjectCostItem);
	  }
  }
  
  protected void initDemoProjectSourceItems(){
	  createProjectSourceItem("1", "down", 				"002", 	new Double(600),	new Double(600));
	  createProjectSourceItem("2", "bott", 				"002", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("3", "reti", 				"002", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("5", "inno", 				"002", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("6", "1sup", 				"002", 	new Double(600),	new Double(600));
	  createProjectSourceItem("7", "2sup", 				"002", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("8", "bres", 				"002", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("9", "1nii", 				"002", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("10", "2nii", 			"002", 	new Double(600),	new Double(600));
	  createProjectSourceItem("11", "aras", 			"002", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("12", "svis", 			"002", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("13", "city", 			"002", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("14", "atec", 			"002", 	new Double(600),	new Double(600));
	  createProjectSourceItem("15", "gest_disict",		"003", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("16", "disa", 			"009", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("17", "cpro", 			"009", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("18", "cpro", 			"011", 	new Double(600),	new Double(600));
	  createProjectSourceItem("19", "cpro", 			"012", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("20", "gest_cluser", 		"003", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("21", "gest_cluser", 		"012", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("22", "itli", 			"009", 	new Double(600),	new Double(600));
	  createProjectSourceItem("23", "spen", 			"012", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("24", "smmo", 			"012", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("25", "pear", 			"002", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("26", "mate", 			"003", 	new Double(600),	new Double(600));
	  createProjectSourceItem("27", "nano_biomed", 		"003", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("28", "nano_biomed", 		"012", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("29", "stab", 			"003", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("30", "stab", 			"012", 	new Double(600),	new Double(600));
	  createProjectSourceItem("31", "lnmr", 			"010", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("32", "lnmr", 			"012", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("33", "lcab", 			"003", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("34", "lcab", 			"012", 	new Double(600),	new Double(600));
	  createProjectSourceItem("35", "semi", 			"009", 	new Double(300), 	new Double(300));
	  createProjectSourceItem("36", "bioe", 			"003", 	new Double(100), 	new Double(100));
	  createProjectSourceItem("37", "farm", 			"011", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("38", "irpo", 			"011", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("39", "irpo", 			"012", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("40", "form", 			"010", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("41", "quru", 			"009", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("42", "sinn", 			"010", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("43", "sinn", 			"011", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("44", "sinn", 			"012", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("45", "spre", 			"010", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("46", "spre", 			"011", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("47", "bisr", 			"711", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("48", "bapi", 			"012", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("49", "sbre", 			"010", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("50", "sbre", 			"011", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("51", "sbre", 			"012", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("52", "bibl", 			"012", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("53", "spap", 			"010", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("54", "spap", 			"011", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("55", "cine", 			"014", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("56", "nano_divulg", 		"711", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("57", "cult", 			"711", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("58", "rict", 			"000", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("59", "rict", 			"009", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("60", "rict", 			"003", 	new Double(1400), 	new Double(1400));
	  createProjectSourceItem("61", "parc", 			"003", 	new Double(1400), 	new Double(1400));
	  
  }
  
  protected void createProjectSourceItem(String id, String idProject, String idSource, Double total, Double actual){
	  if(budgetService.createProjectSourceItemQuery().projectSourceItemId(id).count() == 0){
		  ProjectSourceItem newProjectSourceItem = budgetService.newProjectSourceItem(id);
		  newProjectSourceItem.setId(id);
		  newProjectSourceItem.setIdSource(idSource);
		  newProjectSourceItem.setIdProject(idProject);
		  newProjectSourceItem.setTotal(total);
		  newProjectSourceItem.setActual(actual);
		  budgetService.saveProjectSourceItem(newProjectSourceItem);
	  }
  }
  
  protected void initDemoAttachmentCategory(){
	  createAttachmentCategory("generico", "Generico");
	  createAttachmentCategory("determinazione", "Determinazione");
	  createAttachmentCategory("fattura", "Fattura");
	  createAttachmentCategory("letterainvito", "Lettera Invito");
	  createAttachmentCategory("contratto", "Contratto");
  }
  
  protected void createAttachmentCategory(String attachmentCategoryId,String name){
	  if(attachmentService.createAttachmentCategoryQuery().attachmentCategoryId(attachmentCategoryId).count()== 0){
		  AttachmentCategory newAttachmentCategory = attachmentService.newAttachmentCategory(attachmentCategoryId);
		  newAttachmentCategory.setName(name);
		  attachmentService.saveAttachmentCategory(newAttachmentCategory);
	  }
  }

  protected void initDemoUsers() {
    createUser("kermit", "Kermit", "The Frog", "kermit", "kermit@activiti.org", 
            "org/activiti/explorer/images/kermit.jpg",
            Arrays.asList("dirigenti", "responsabili", "sag", "agi", "app", "cds", "sir", "net", "spf", "pst", "rea", "stt", "ric", "cge", "doc", "dir", "user", "admin"),
            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
                          "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));
    // new  SR default users
    createUser("gpisanu", "Giorgio", "Pisanu", "pisanu", "labopensource+DIR-direttore@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("dir", "dirigenti", "admin"),
            Arrays.asList("birthDate", "", "jobTitle", "Dirigente Area Servizi alle Imprese", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("sbaghino", "Sebastiano", "Baghino", "baghino", "labopensource+PST-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("pst","user"),
            Arrays.asList("birthDate", "", "jobTitle", "Membro", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("gserra", "Giuseppe", "Serra", "serra", "labopensource+PST-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("pst", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("emulas", "Enrico", "Mulas", "mulas", "labopensource+SPF-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("spf", "app", "dirigenti", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Dirigente Responsabile Aree Giuridico-Amministrativa ed Economico-Finanziaria", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("smaxia", "Susanna", "Maxia", "maxia", "labopensource+AGI-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("agi", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("acorda", "Alessandra", "Corda", "corda", "labopensource+SAG-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("sag", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("vsongini", "Valter", "Songini", "songini", "labopensource+CDS-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("cds", "dirigenti", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Dirigente Responsabile Area Ricerca e Parco Tecnologico", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("lsagheddu", "Lucia", "Sagheddu", "sagheddu", "labopensource+SIR-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("sir", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("cmou", "Cesare", "Mou", "mou", "labopensource+REA-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("net","rea", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("sennas", "Sandra", "Ennas", "ennas", "labopensource+STT-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("stt", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("lcontini", "Luca", "Contini", "contini", "labopensource+RIC-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("ric", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("agugliotta", "Alessandra", "Gugliotta", "gugliotta", "labopensource+CGE-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("cge", "responsabili", "admin"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("eangioni", "Elena", "Angioni", "angioni", "labopensource+DOC-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("doc", "responsabili", "admin"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("aatzeni", "Alessandra", "Atzeni", "atzeni", "labopensource+DIR-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("dir", "responsabili", "user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    
  }
  
  protected void createUser(String userId, String firstName, String lastName, String password, 
          String email, String imageResource, List<String> groups, List<String> userInfo) {
    
    if (identityService.createUserQuery().userId(userId).count() == 0) {
      
      // Following data can already be set by demo setup script
      
      User user = identityService.newUser(userId);
      user.setFirstName(firstName);
      user.setLastName(lastName);
      user.setPassword(password);
      user.setEmail(email);
      identityService.saveUser(user);
      
      if (groups != null) {
        for (String group : groups) {
          identityService.createMembership(userId, group);
        }
      }
    }
    
    // Following data is not set by demo setup script
      
    // image
    if (imageResource != null) {
      byte[] pictureBytes = IoUtil.readInputStream(this.getClass().getClassLoader().getResourceAsStream(imageResource), null);
      Picture picture = new Picture(pictureBytes, "image/jpeg");
      identityService.setUserPicture(userId, picture);
    }
      
    // user info
    if (userInfo != null) {
      for(int i=0; i<userInfo.size(); i+=2) {
        identityService.setUserInfo(userId, userInfo.get(i), userInfo.get(i+1));
      }
    }
    
  }
  
  protected void initProcessDefinitions() {
    
    String deploymentName = "Procedure avviabili";
    List<Deployment> deploymentList = repositoryService.createDeploymentQuery().deploymentName(deploymentName).list();
    
    if (deploymentList == null || deploymentList.size() == 0) {
      repositoryService.createDeployment()
        .name(deploymentName)
        .addClasspathResource("org/activiti/explorer/demo/process/nuovo-progetto.bpmn")
        .deploy();
    }
    
    deploymentName = "Sottoprocedure";
    deploymentList = repositoryService.createDeploymentQuery().deploymentName(deploymentName).list();
    
    if (deploymentList == null || deploymentList.size() == 0) {
      repositoryService.createDeployment()
        .name(deploymentName)
        .addClasspathResource("org/activiti/explorer/demo/process/determinazione-DG.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/determinazione-DG-noProposta.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/pagamento-ABS.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/ABS.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/pubblicazione.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/documentazione-aiuti.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/valutazione-aiuti.bpmn")   
        .addClasspathResource("org/activiti/explorer/demo/process/aiuti.bpmn")
        .addClasspathResource("org/activiti/explorer/demo/process/collaborazioni.bpmn")
        .deploy();
    }
    
//    String reportDeploymentName = "Demo reports";
//    deploymentList = repositoryService.createDeploymentQuery().deploymentName(reportDeploymentName).list();
//    if (deploymentList == null || deploymentList.size() == 0) {
//      repositoryService.createDeployment()
//        .name(reportDeploymentName)
////        .addClasspathResource("org/activiti/explorer/demo/process/reports/taskDurationForProcessDefinition.bpmn20.xml")
////        .addClasspathResource("org/activiti/explorer/demo/process/reports/processInstanceOverview.bpmn20.xml")
////        .addClasspathResource("org/activiti/explorer/demo/process/reports/helpdeskFirstLineVsEscalated.bpmn20.xml")
////        .addClasspathResource("org/activiti/explorer/demo/process/reports/employeeProductivity.bpmn20.xml")
//        .deploy();
//    }
    
  }

  protected void generateReportData() {
    if (generateReportData) {
      
      // Report data is generated in background thread
      
      Thread thread = new Thread(new Runnable() {
        
        public void run() {
          
          // We need to temporarily disable the job executor or it would interfere with the process execution
          ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration().getJobExecutor().shutdown();
          
          Random random = new Random();
          
          Date now = new Date(new Date().getTime() - (24 * 60 * 60 * 1000));
          ClockUtil.setCurrentTime(now);
          
          for (int i=0; i<50; i++) {
            
            if (random.nextBoolean()) {
              processEngine.getRuntimeService().startProcessInstanceByKey("fixSystemFailure");
            }
            
            if (random.nextBoolean()) {
              processEngine.getIdentityService().setAuthenticatedUserId("kermit");
              Map<String, Object> variables = new HashMap<String, Object>();
              variables.put("customerName", "testCustomer");
              variables.put("details", "Looks very interesting!");
              variables.put("notEnoughInformation", false);
              processEngine.getRuntimeService().startProcessInstanceByKey("reviewSaledLead", variables);
            }
            
            if (random.nextBoolean()) {
              processEngine.getRuntimeService().startProcessInstanceByKey("escalationExample");
            }
            
            if (random.nextInt(100) < 20) {
              now = new Date(now.getTime() - ((24 * 60 * 60 * 1000) - (60 * 60 * 1000)));
              ClockUtil.setCurrentTime(now);
            }
          }
          
          List<Job> jobs = processEngine.getManagementService().createJobQuery().list();
          for (int i=0; i<jobs.size()/2; i++) {
            ClockUtil.setCurrentTime(jobs.get(i).getDuedate());
            processEngine.getManagementService().executeJob(jobs.get(i).getId());
          }
          
          List<Task> tasks = processEngine.getTaskService().createTaskQuery().list();
          while (tasks.size() > 0) {
            for (Task task : tasks) {
              
              if (task.getAssignee() == null) {
                String assignee = random.nextBoolean() ? "kermit" : "fozzie";
                processEngine.getTaskService().claim(task.getId(), assignee);
              }
              
              ClockUtil.setCurrentTime(new Date(task.getCreateTime().getTime() + random.nextInt(60 * 60 * 1000)));
              
              processEngine.getTaskService().complete(task.getId());
            }
            
            tasks = processEngine.getTaskService().createTaskQuery().list();
          }
          
          ClockUtil.reset();
          
          ((ProcessEngineImpl) processEngine).getProcessEngineConfiguration().getJobExecutor().start();
          LOGGER.info("Demo report data generated");
        }
        
      });
      thread.start();
      
    }
  }
  
  protected void initModelData() {
    createModelData("Demo model", "This is a demo model", "org/activiti/explorer/demo/model/test.model.json");
  }
  
  protected void createModelData(String name, String description, String jsonFile) {
    List<Model> modelList = repositoryService.createModelQuery().modelName("Demo model").list();
    
    if (modelList == null || modelList.size() == 0) {
    
      Model model = repositoryService.newModel();
      model.setName(name);
      
      ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
      modelObjectNode.put(MODEL_NAME, name);
      modelObjectNode.put(MODEL_DESCRIPTION, description);
      model.setMetaInfo(modelObjectNode.toString());
      
      repositoryService.saveModel(model);
      
      try {
        InputStream svgStream = this.getClass().getClassLoader().getResourceAsStream("org/activiti/explorer/demo/model/test.svg");
        repositoryService.addModelEditorSourceExtra(model.getId(), IOUtils.toByteArray(svgStream));
      } catch(Exception e) {
        LOGGER.warn("Failed to read SVG", e);
      }
      
      try {
        InputStream editorJsonStream = this.getClass().getClassLoader().getResourceAsStream(jsonFile);
        repositoryService.addModelEditorSource(model.getId(), IOUtils.toByteArray(editorJsonStream));
      } catch(Exception e) {
        LOGGER.warn("Failed to read editor JSON", e);
      }
    }
  }

}
