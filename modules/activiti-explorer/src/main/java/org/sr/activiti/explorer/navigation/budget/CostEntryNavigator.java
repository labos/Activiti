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
public class CostEntryNavigator implements Navigator {

  public static final String COSTENTRY_URI_PART = "costEntry";
  
  public String getTrigger() {
    return COSTENTRY_URI_PART;
  }

  public void handleNavigation(UriFragment uriFragment) {
    String costEntryId = uriFragment.getUriPart(1);
    
    if(costEntryId != null) {
      ExplorerApp.get().getViewManager().showCostEntryPage(costEntryId);
    } else {
      ExplorerApp.get().getViewManager().showCostEntryPage();
    }
  }

}
