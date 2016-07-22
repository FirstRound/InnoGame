package com.brackeen.scared.controllers;

import com.brackeen.scared.action.Action;
import com.brackeen.scared.action.EmergencyAction;
import com.brackeen.scared.action.OrdinaryAction;
import com.brackeen.scared.action.SpecialAction;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by pisatel on 21.07.16.
 */
public class ActionController {
    Queue<OrdinaryAction> ordinaryActions = new LinkedList<OrdinaryAction>();
    Queue<EmergencyAction> emergencyActions = new LinkedList<EmergencyAction>();
    Queue<SpecialAction> specialActions = new LinkedList<SpecialAction>();

    public void addOrdinaryAction(OrdinaryAction action) {
        ordinaryActions.add(action);
    }

    public void addEmergencyAction(EmergencyAction action) {
        emergencyActions.add(action);
    }

    public void addSpecialAction(SpecialAction action) {
        specialActions.add(action);
    }

    public Action getNextAction() {
        return null;
    }

}
