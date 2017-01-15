package muller.martin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Esta classe e responsavel por executar
 * uma url da API REST do Jenkins via POST 
 * 
 * @author Martin Muller <mmuller.web@gmail.com>
 *
 */
public class execute {
	
	
	private static InputStream enviarPost(String servidor, String complemento, String method) throws Exception{
		return enviarPost(servidor, complemento, null,method,null);
	}
	
	private static InputStream enviarPost(String servidor, String complemento, String params, String method, String crumb) throws Exception{

		String strURL = servidor + complemento;
		
		URL url = new URL(strURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod(method);
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if(crumb != null){
			String[] spl = crumb.split(":");
			if(spl.length > 1)
				con.setRequestProperty(spl[0], spl[1]);
		}
		if(params != null){
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(params);
			wr.flush();
			wr.close();
		}
		return con.getInputStream();
	}
	
	/**
	 * ARGS 0 http://localhost:8080/
	 * ARGS 1 teste/um
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if(args.length < 2)
			throw new Exception("Erro de parametro favor informar url...");

		try{
			
			// Pegar CRUMB JENKINS PARA EXECUTAR
			
			BufferedReader in = new BufferedReader(new InputStreamReader(enviarPost(args[0], "crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)'","GET")));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// FIM<MMMMM Pegar CRUMB JENKINS PARA EXECUTAR

			// Executando stop JENKINS!!!!
			in = new BufferedReader(new InputStreamReader(enviarPost(args[0], args[1],null,"POST",response.toString())));
			
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response);
			System.out.println("$##$#$#$#$#$#$# Toco no jenkins");
			
		}catch(Exception e){
			throw new Exception(e);
		}
		
	}
}
