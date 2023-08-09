package ma.azdad.view;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;


@ManagedBean
@Component
@Scope("view")
@Secured("ROLE_IT_MANAGER")
public class TestView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());


	private String input;

	public void getFullNames() {
		Pattern pattern = Pattern.compile("\\[(.*?)\\]");
		Matcher matcher = pattern.matcher(input);
		while (matcher.find())
			System.out.println(matcher.group(1));
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

}