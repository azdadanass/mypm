package ma.azdad.model;

	import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

	@ManagedBean
	@SessionScoped
	public class SwitchBean {
	    private boolean switchState;

	    public boolean isSwitchState() {
	        return switchState;
	    }

	    public void setSwitchState(boolean switchState) {
	        this.switchState = switchState;
	    }
	    
	}
	


