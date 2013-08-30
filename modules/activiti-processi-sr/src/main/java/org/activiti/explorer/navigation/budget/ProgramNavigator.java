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

package org.activiti.explorer.navigation.budget;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.navigation.Navigator;
import org.activiti.explorer.navigation.UriFragment;


/**
 * @author Lab Open Source
 */
public class ProgramNavigator implements Navigator {

  public static final String PROGRAM_URI_PART = "program";
  
  public String getTrigger() {
    return PROGRAM_URI_PART;
  }

  public void handleNavigation(UriFragment uriFragment) {
    String programId = uriFragment.getUriPart(1);
    
    if(programId != null) {
      ExplorerApp.get().getViewManager().showProgramPage(programId);
    } else {
      ExplorerApp.get().getViewManager().showProgramPage();
    }
  }

}
