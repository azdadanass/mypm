package ma.azdad.view;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.service.ThymeLeafService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.TemplateType;

@ManagedBean
@Component
@Scope("session")

public class HtmlView implements Serializable {

	@Autowired
	ThymeLeafService thymeLeafService;

	@Autowired
	CacheView cacheView;

	public String generateOverlayPanelContentBasic(Date date, String photo, String name, String title, String phone, String email) {
		Map<String, Object> map = new HashMap<>();
		map.put("photo", photo);
		map.put("name", name);
		map.put("title", title);
		map.put("phone", phone);
		map.put("email", email);
		map.put("date", date);
		return thymeLeafService.generate(TemplateType.HTML, "overlay_panel.html", map);
	}

	public String generateOverlayPanelContent(Date date, String... usernameTab) {
		String username = UtilsFunctions.firstNotNullAndNotEmpty(usernameTab);
		return generateOverlayPanelContentBasic(date, cacheView.getPhoto(username), cacheView.getFullName(username), cacheView.getJobTitle(username), cacheView.getMobileNumber(username), cacheView.getEmail(username));
	}

	public String generateOverlayPanelContent(String username) {
		return generateOverlayPanelContent(null, username);
	}

}
