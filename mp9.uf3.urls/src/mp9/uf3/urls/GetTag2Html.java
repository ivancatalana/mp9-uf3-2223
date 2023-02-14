package mp9.uf3.urls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GetTag2Html {

	public static void main (String... args) throws IOException {
		String tag;
		
		//comprobar que hi hagi dos paràmetres a l'entrada
		if(args.length !=2 ) {
			System.out.println("Error en els argumennts: getTag2Html URL tag");
			System.exit(0);
		}
		else {
			//capturar els paràmetres
			URL url = new URL(args[0]);
			tag = args[1];

			// patró de cerca regexp
			String pattern = "<" + tag + ".*\\/?>";
			Pattern p = Pattern.compile(pattern);

			System.out.println("busquem a " + url.toString() + " l'etiqueta " + tag);
			//Iniciem la connexió
			HttpURLConnection con = (HttpURLConnection) url.openConnection();


			BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream()) );
			String res;
			while( (res = in.readLine()) != null) {
				Matcher m = p.matcher(res);
				//buscar el tag dins de la línia i mostrar la línia
				if(m.find()) {
					int inici = m.start();
					System.out.println(res.substring(inici));
				}
			}
			in.close();
			
		}
	}
}
