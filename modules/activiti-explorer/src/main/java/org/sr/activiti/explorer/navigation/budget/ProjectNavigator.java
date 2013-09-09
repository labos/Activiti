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

package org.sr.activiti.explorer.navigation.budget;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.navigation.Navigator;
import org.activiti.explorer.navigation.UriFragment;

/**
 * @author Lab Open Source
 */
public class ProjectNavigator implements Navigator {

  public static final String PROJECT_URI_PART = "project";
  
  public String getTrigger() {
    return PROJECT_URI_PART;
  }

  public void handleNavigation(UriFragment uriFragment) {
    String projectId = uriFragment.getUriPart(1);
    
    if(projectId != null) {
      ExplorerApp.get().getViewManager().showProjectPage(projectId);
    } else {
      ExplorerApp.get().getViewManager().showProjectPage();
    }
  }

}
