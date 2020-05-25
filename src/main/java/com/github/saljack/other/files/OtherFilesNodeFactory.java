/**
 * Copyright 2020 Saljack
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.saljack.other.files;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ChangeListener;

import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ui.support.NodeFactory;
import org.netbeans.spi.project.ui.support.NodeList;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.Node;
import org.openide.util.ChangeSupport;

@NodeFactory.Registration(projectType = {
    "org-netbeans-modules-ant-freeform",
    "org-netbeans-modules-apisupport-project",
    "org-netbeans-modules-apisupport-project-suite",
    "org-netbeans-modules-apisupport-project-suite-jnlp",
    "org-netbeans-modules-apisupport-project-suite-osgi",
    "org-netbeans-modules-apisupport-project-suite-package",
    "org-netbeans-modules-autoproject",
    "org-netbeans-modules-bpel-project",
    "org-netbeans-modules-j2ee-clientproject",
    "org-netbeans-modules-j2ee-earproject",
    "org-netbeans-modules-j2ee-ejbjarproject",
    "org-netbeans-modules-java-j2seproject",
    "org-netbeans-modules-javacard-project",
    "org-netbeans-modules-javaee-project",
    "org-netbeans-modules-javafx2-project",
    "org-netbeans-modules-maven",
    "org-netbeans-modules-php-project",
    "org-netbeans-modules-ruby-project",
    "org-netbeans-modules-sql-project",
    "org-netbeans-modules-web-project",
    "org-netbeans-modules-xslt-project"
}, position = 9000)
public class OtherFilesNodeFactory implements NodeFactory {

  @Override
  public NodeList<?> createNodes(Project project) {
    return new OtherFileNodeList(project);
  }

  private static final class OtherFileNodeList implements NodeList<Node> {

    private final Project project;
    private final ChangeSupport cs;

    OtherFileNodeList(Project project) {
      super();
      this.project = project;
      this.cs = new ChangeSupport(this);
      // Reload node list on project directory content change
      project.getProjectDirectory().addFileChangeListener(new FileChangeAdapter() {
        @Override
        public void fileDeleted(FileEvent fe) {
          cs.fireChange();
        }

        @Override
        public void fileDataCreated(FileEvent fe) {
          cs.fireChange();
        }

        @Override
        public void fileRenamed(FileRenameEvent fe) {
          cs.fireChange();
        }
      });
    }

    @Override
    public Node node(Node key) {
      return key;
    }

    @Override
    public List<Node> keys() {
      final List<Node> keys = new ArrayList<Node>(0);
      final FileObject directory = project.getProjectDirectory();
      keys.add(getFileNode(directory));
      return keys;
    }

    @Override
    public void addChangeListener(ChangeListener l) {
      cs.addChangeListener(l);
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
      cs.removeChangeListener(l);
    }

    @Override
    public void addNotify() {
    }

    @Override
    public void removeNotify() {
    }

    private static Node getFileNode(FileObject child) {
      try {
        final DataObject find = DataObject.find(child);
        Node node = find.getNodeDelegate().cloneNode();
        node.setDisplayName("Files");
        return node;
      } catch (DataObjectNotFoundException ex) {
        throw new IllegalStateException(ex);
      }
    }
  }

}
