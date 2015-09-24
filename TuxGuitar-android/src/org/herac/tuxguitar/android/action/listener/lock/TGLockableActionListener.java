package org.herac.tuxguitar.android.action.listener.lock;

import java.util.ArrayList;
import java.util.List;

import org.herac.tuxguitar.action.TGActionErrorEvent;
import org.herac.tuxguitar.action.TGActionEvent;
import org.herac.tuxguitar.action.TGActionPostExecutionEvent;
import org.herac.tuxguitar.action.TGActionPreExecutionEvent;
import org.herac.tuxguitar.android.editor.TGEditorManager;
import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.event.TGEventListener;
import org.herac.tuxguitar.util.TGContext;

public class TGLockableActionListener implements TGEventListener {
	
	private TGContext context;
	private List<String> actionIds;
	
	public TGLockableActionListener(TGContext context){
		this.context = context;
		this.actionIds = new ArrayList<String>();
	}
	
	public boolean isLockableAction(String id) {
		return this.actionIds.contains(id);
	}
	
	public void addLockableAction(String id) {
		this.actionIds.add(id);
	}
	
	public void removeLockableAction(String id) {
		this.actionIds.remove(id);
	}
	
	public void checkForLock(String actionId) {
		if( this.isLockableAction(actionId) ) {
			TGEditorManager.getInstance(this.context).lock();
		}
	}
	
	public void checkForUnlock(String actionId) {
		if( this.isLockableAction(actionId) ) {
			TGEditorManager.getInstance(this.context).unlock(false);
		}
	}
	
	public void processEvent(TGEvent event) {
		if( TGActionPreExecutionEvent.EVENT_TYPE.equals(event.getEventType()) ) {
			this.checkForLock((String) event.getAttribute(TGActionEvent.ATTRIBUTE_ACTION_ID));
		}
		else if( TGActionPostExecutionEvent.EVENT_TYPE.equals(event.getEventType()) ) {
			this.checkForUnlock((String) event.getAttribute(TGActionEvent.ATTRIBUTE_ACTION_ID));
		}
		else if( TGActionErrorEvent.EVENT_TYPE.equals(event.getEventType()) ) {
			this.checkForUnlock((String) event.getAttribute(TGActionEvent.ATTRIBUTE_ACTION_ID));
		}
	}
}