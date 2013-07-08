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
import org.activiti.engine.budget.BudgetService;
import org.activiti.engine.budget.Program;
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
  
  protected boolean createDemoUsersAndGroups;
  protected boolean createDemoProcessDefinitions;
  protected boolean createDemoModels;
  protected boolean generateReportData;
  
  public void init() {
    this.identityService = processEngine.getIdentityService();
    this.repositoryService = processEngine.getRepositoryService();
    this.budgetService = processEngine.getBudgetService();
    
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
    
    if (createDemoModels) {
      LOGGER.info("Initializing demo models");
      initModelData();
    }
    
    if (generateReportData) {
      LOGGER.info("Initializing demo report data");
      generateReportData();
    }
    
    initDemoSources();
    initDemoPrograms();
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
    String[] assignmentGroups = new String[] {"management", "sales", "marketing", "engineering", "sag", "agi", "app", "cds", "sir", "net","spf","pst","rea","stt","ric","cge","doc","dir" };
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
      newGroup.setName(groupId.substring(0, 1).toUpperCase() + groupId.substring(1));
      newGroup.setType(type);
      identityService.saveGroup(newGroup);
    }
  }
  
  protected void initDemoSources(){
	  createSource("fonte1", "Fonte 1", new Double(10000));
	  createSource("fonte2", "Fonte 2", new Double(20000));
	  createSource("fonte3", "Fonte 3", new Double(30000));
	  createSource("fonte4", "Fonte 4", new Double(40000));
	  createSource("fonte5", "Fonte 5", new Double(50000));
	  createSource("fonte6", "Fonte 6", new Double(60000));
	  createSource("fonte7", "Fonte 7", new Double(70000));
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
	  createProgram("programma1", "Programma 1", new Double(1000));
	  createProgram("programma2", "Programma 2", new Double(2000));
	  createProgram("programma3", "Programma 3", new Double(3000));
	  createProgram("programma4", "Programma 4", new Double(4000));
	  createProgram("programma5", "Programma 5", new Double(5000));
	  createProgram("programma6", "Programma 6", new Double(6000));
	  createProgram("programma7", "Programma 7", new Double(7000));
  }
  
  protected void createProgram(String programId,String name, Double total){
	  if(budgetService.createProgramQuery().programId(programId).count() == 0){
		  Program newProgram = budgetService.newProgram(programId);
		  newProgram.setName(name);
		  newProgram.setTotal(total);
		  budgetService.saveProgram(newProgram);
	  }
  }

  protected void initDemoUsers() {
    createUser("kermit", "Kermit", "The Frog", "kermit", "kermit@activiti.org", 
            "org/activiti/explorer/images/kermit.jpg",
            Arrays.asList("management", "sales", "marketing", "engineering","sag", "agi", "app", "cds", "sir", "net","spf","pst","rea","stt","ric","cge","doc","dir", "user", "admin"),
            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
                          "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));
    
    createUser("gonzo", "Gonzo", "The Great", "gonzo", "gonzo@activiti.org", 
            "org/activiti/explorer/images/gonzo.jpg",
            Arrays.asList("management", "sales", "marketing", "user"),
            null);
    createUser("fozzie", "Fozzie", "Bear", "fozzie", "fozzie@activiti.org", 
            "org/activiti/explorer/images/fozzie.jpg",
            Arrays.asList("marketing", "engineering", "user"),
            null);
    // new  SR default users
    createUser("sbaghino", "Sebastiano", "Baghino", "baghino", "labopensource+PST-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("pst","user"),
            Arrays.asList("birthDate", "", "jobTitle", "Membro", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("gserra", "Giuseppe", "Serra", "serra", "labopensource+PST-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("pst","user"),
            Arrays.asList("birthDate", "", "jobTitle", "Responsabile", "location", "",
                    "phone", "", "twitterName", "", "skype", ""));
    createUser("emulas", "Enrico", "Mulas", "mulas", "labopensource+SPF-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("spf", "app", "user"),
            null);
    createUser("smaxia", "Susanna", "Maxia", "maxia", "labopensource+AGI-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("agi", "user"),
            null);
    createUser("acorda", "Alessandra", "corda", "corda", "labopensource+SAG-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("sag", "user"),
            null);
    createUser("vsongini", "Valter", "Songini", "songini", "labopensource+CDS-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("cds", "user"),
            null);
    createUser("lsagheddu", "Lucia", "Sagheddu", "sagheddu", "labopensource+SIR-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("sir", "user"),
            null);
    createUser("cmou", "Cesare", "Mou", "mou", "labopensource+REA-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("net","rea", "user"),
            null);
    createUser("sennas", "Sandra", "Ennas", "ennas", "labopensource+STT-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("stt", "user"),
            null);
    createUser("lcontini", "Luca", "Contini", "contini", "labopensource+RIC-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("ric", "user"),
            null);
    createUser("agugliotta", "Alessandra", "Gugliotta", "gugliotta", "labopensource+CGE-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("cge", "user"),
            null);
    createUser("eangioni", "Elena", "Angioni", "angioni", "labopensource+DOC-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("doc", "user"),
            null);
    createUser("aatzeni", "Alessandra", "Atzeni", "atzeni", "labopensource+DIR-responsabile@gmail.com", 
            "org/activiti/explorer/images/user-blue-icon.png",
            Arrays.asList("dir", "user"),
            null);
    
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
    
    String deploymentName = "Demo processes";
    List<Deployment> deploymentList = repositoryService.createDeploymentQuery().deploymentName(deploymentName).list();
    
    if (deploymentList == null || deploymentList.size() == 0) {
      repositoryService.createDeployment()
        .name(deploymentName)
        .addClasspathResource("org/activiti/explorer/demo/process/createTimersProcess.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/VacationRequest.png")
        .addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/FixSystemFailureProcess.png")
        .addClasspathResource("org/activiti/explorer/demo/process/simple-approval.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/Helpdesk.png")
        .addClasspathResource("org/activiti/explorer/demo/process/reviewSalesLead.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/processo-autorizzazione-spesa.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/processo-autorizzazione-spesa-DG.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/processo-abs.bpmn")
        .deploy();
    }
    
    String reportDeploymentName = "Demo reports";
    deploymentList = repositoryService.createDeploymentQuery().deploymentName(reportDeploymentName).list();
    if (deploymentList == null || deploymentList.size() == 0) {
      repositoryService.createDeployment()
        .name(reportDeploymentName)
        .addClasspathResource("org/activiti/explorer/demo/process/reports/taskDurationForProcessDefinition.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/reports/processInstanceOverview.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/reports/helpdeskFirstLineVsEscalated.bpmn20.xml")
        .addClasspathResource("org/activiti/explorer/demo/process/reports/employeeProductivity.bpmn20.xml")
        .deploy();
    }
    
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
