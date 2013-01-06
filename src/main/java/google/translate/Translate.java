package google.translate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.JavaScriptPage;
import com.gargoylesoftware.htmlunit.WebClient;


public class Translate {
	
	public static String translate(String input, String inputLanguageCode, String outputLanguageCode) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
		final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0);
	    final JavaScriptPage page = (JavaScriptPage) webClient.getPage("http://translate.google.com/translate_a/t?client=t&text="+input+"&hl="+inputLanguageCode+"&sl=auto&tl="+outputLanguageCode+"&ie=UTF-8&oe=UTF-8&multires=1&prev=conf&psl=auto&ptl="+inputLanguageCode+"&otf=1&it=sel.57505&ssel=0&tsel=3&uptl="+outputLanguageCode+"&sc=1");
	    final String pageAsText = page.getContent();
	    JsonFactory factory = new JsonFactory();
	    JsonParser parser  = factory.createJsonParser(pageAsText);
	    parser.nextToken();
	    parser.nextToken();
	    int openBrackets = 1;
	    int closedBrackets = 0;
	    List<List<String>> mainList = new ArrayList<List<String>>();
	    List<String> innerList = null;
	    while(openBrackets != closedBrackets){
	    	JsonToken token = parser.nextToken();
		    if(token == JsonToken.START_ARRAY){
		    	openBrackets++;
		    	innerList = new ArrayList<String>();
		    }
		    else if(token == JsonToken.END_ARRAY){
		    	closedBrackets ++;
		    	if(innerList != null){
		    		mainList.add(innerList);
			    	innerList = null;
		    	}
		    }
		    else{
		    	String value = parser.getText();
		    	if(! value.equals(",")){
		    		innerList.add(value);
		    	}
		    }
	    }
	    StringBuffer buffer = new StringBuffer();
	    for(List<String> list : mainList){
	    	buffer.append(list.get(0));
	    }
	    return buffer.toString();
	  
	}

}
