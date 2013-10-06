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

package org.sr.activiti.explorer.ui.budget;

import org.activiti.explorer.ExplorerApp;
import org.activiti.explorer.I18nManager;
import org.activiti.explorer.Messages;
import org.activiti.explorer.ViewManager;
import org.activiti.explorer.ui.custom.ToolBar;
import org.activiti.explorer.ui.custom.ToolbarEntry.ToolbarCommand;

/**
 * @author Lab Open Source
 */
public class BudgetMenuBar extends ToolBar {
  
  private static final long serialVersionUID = 1L;
  
  public static final String ENTRY_PROGRAMS = "programs";
  public static final String ENTRY_PROJECTS = "projects";
  public static final String ENTRY_SOURCES = "sources";
  public static final String ENTRY_COSTENTRIES = "costEntries";

  protected I18nManager i18nManager;
  protected ViewManager viewManager;
  
  public BudgetMenuBar() {
    this.i18nManager = ExplorerApp.get().getI18nManager();
    this.viewManager = ExplorerApp.get().getViewManager();
    
    init();
  }
  
  protected void init() {
    setWidth("100%");

    addToolbarEntry(ENTRY_PROGRAMS, i18nManager.getMessage(Messages.BUDGET_MENU_PROGRAMS), new ToolbarCommand() {
      public void toolBarItemSelected() {
        viewManager.showProgramPage();
      }
    });
    
    addToolbarEntry(ENTRY_PROJECTS, i18nManager.getMessage(Messages.BUDGET_MENU_PROJECTS), new ToolbarCommand() {
      public void toolBarItemSelected() {
        viewManager.showProjectPage();
      }
    });
    
    addToolbarEntry(ENTRY_SOURCES, i18nManager.getMessage(Messages.BUDGET_MENU_SOURCES), new ToolbarCommand() {
        public void toolBarItemSelected() {
          viewManager.showSourcePage();
        }
      });
    
    addToolbarEntry(ENTRY_COSTENTRIES, i18nManager.getMessage(Messages.BUDGET_MENU_COSTENTRIES), new ToolbarCommand() {
        public void toolBarItemSelected() {
          viewManager.showCostEntryPage();
        }
      });
  }
}
