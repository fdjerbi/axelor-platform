/**
 * Copyright (c) 2012-2013 Axelor. All Rights Reserved.
 *
 * The contents of this file are subject to the Common Public
 * Attribution License Version 1.0 (the “License”); you may not use
 * this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://license.axelor.com/.
 *
 * The License is based on the Mozilla Public License Version 1.1 but
 * Sections 14 and 15 have been added to cover use of software over a
 * computer network and provide for limited attribution for the
 * Original Developer. In addition, Exhibit A has been modified to be
 * consistent with Exhibit B.
 *
 * Software distributed under the License is distributed on an “AS IS”
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is part of "Axelor Business Suite", developed by
 * Axelor exclusively.
 *
 * The Original Developer is the Initial Developer. The Initial Developer of
 * the Original Code is Axelor.
 *
 * All portions of the code written by Axelor are
 * Copyright (c) 2012-2013 Axelor. All Rights Reserved.
 */
package com.axelor.wkf.data;

import java.util.ArrayList;

import com.axelor.meta.db.MetaAction;
import com.axelor.meta.service.MetaModelService;
import com.axelor.wkf.db.Node;
import com.axelor.wkf.db.Transition;
import com.axelor.wkf.db.Workflow;

public class CreateData {

	public static Workflow createWorkflow() {
		
		Transition startTransition = new Transition();
		startTransition.setName("startTransition");
		startTransition.setSequence(0);
		
		Transition intermediaryTransition = new Transition();
		intermediaryTransition.setName("intermediaryTransition");
		intermediaryTransition.setSequence(0);
		
		Transition endTransition = new Transition();
		endTransition.setName("endTransition");
		endTransition.setSequence(0);
		
		MetaAction metaAction1 = new MetaAction();
		metaAction1.setName("action-test-1");
		metaAction1.setXml("<action-record name=\"action-test-1\" model=\"com.axelor.wkf.db.Workflow\"><field name=\"archived\" expr=\"eval: true\"/></action-record>");
		metaAction1.setType("action-record");
		metaAction1.save();
		
		MetaAction metaAction2 = new MetaAction();
		metaAction2.setName("action-test-2");
		metaAction2.setXml("<action-record name=\"action-test-2\" model=\"com.axelor.wkf.db.Workflow\"><field name=\"active\" expr=\"eval: !archived\"/></action-record>");
		metaAction2.setType("action-record");
		metaAction2.save();
		
		MetaAction metaAction3 = new MetaAction();
		metaAction3.setName("action-test-3");
		metaAction3.setXml("<action-attrs name=\"action-test-3\" ><attribute name=\"hidden\" for=\"archived\" expr=\"eval: true\"/></action-attrs>");
		metaAction3.setType("action-attrs");
		metaAction3.save();
		
		MetaAction metaAction4 = new MetaAction();
		metaAction4.setName("action-test-4");
		metaAction4.setXml("<action-attrs name=\"action-test-4\" ><attribute name=\"readonly\" for=\"archived\" expr=\"eval: true\"/></action-attrs>");
		metaAction4.setType("action-attrs");
		metaAction4.save();

		MetaAction metaAction5 = new MetaAction();
		metaAction5.setName("action-test-5");
		metaAction5.setXml("<action-group name=\"action-test-5\" ><action name=\"action-test-1\"/><action name=\"action-test-2\"/></action-group>");
		metaAction5.setType("action-group");
		
		MetaAction metaAction6 = new MetaAction();
		metaAction6.setName("action-test-6");
		metaAction6.setXml("<action-group name=\"action-test-6\" ><action name=\"action-test-3\"/><action name=\"action-test-4\"/><action name=\"save\"/></action-group>");
		metaAction6.setType("action-group");
		
		Node node1 = new Node();
		node1.setName("Node 1");
		node1.setType("start");
		node1.setStartTransitions(new ArrayList<Transition>());
		node1.addEndTransition(startTransition);
		
		Node node2 = new Node();
		node2.setName("Node 2");
		node2.setType("intermediary");
		node2.setAction( metaAction5 );
		node2.addStartTransition(startTransition);
		node2.addEndTransition(intermediaryTransition);
		
		Node node3 = new Node();
		node3.setName("Node 3");
		node3.setType("intermediary");
		node3.setAction( metaAction6 );
		node3.addStartTransition(intermediaryTransition);
		node3.addEndTransition(endTransition);
		
		Node node4 = new Node();
		node4.setName("Node 2");
		node4.setType("stop");
		node4.setLogicOperator("and");
		node4.addStartTransition(endTransition);
		node4.setEndTransitions(new ArrayList<Transition>());
		
		startTransition.setStartNode(node1);
		startTransition.setNextNode(node2);
		
		intermediaryTransition.setStartNode(node2);
		intermediaryTransition.setNextNode(node3);
		
		endTransition.setStartNode(node3);
		endTransition.setNextNode(node4);
		
		Workflow workflow = new Workflow();
		
		workflow.setName("Wkf");
		workflow.setMetaModel( MetaModelService.getMetaModel(Workflow.class) );
		workflow.setNode(node1);
		workflow.setMaxNodeCounter(1);
		
		return workflow.save();
	}
	

}
