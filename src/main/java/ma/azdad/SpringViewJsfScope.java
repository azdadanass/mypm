package ma.azdad;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SpringViewJsfScope implements Scope {

	@Override
	public Object get(String name, ObjectFactory objectFactory) {

		Map viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();

		if (viewMap.containsKey(name)) {
			return viewMap.get(name);
		} else {
			Object object = objectFactory.getObject();
			viewMap.put(name, object);
			return object;
		}
	}

	@Override
	public Object remove(String name) {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(name);
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		// Not supported
	}

	@Override
	public Object resolveContextualObject(String key) {
		return null;
	}
}