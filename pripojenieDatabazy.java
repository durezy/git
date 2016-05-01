package dbs_app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class pripojenieDatabazy {
	static Connection c = null;
    static Statement stmt = null;
    static String pravda = "t";
    static Client client;
    static SearchResponse sResponse;
    static List<Map<String, Object>> result;
    
    public pripojenieDatabazy(){
    try {
    Class.forName("org.postgresql.Driver");
      c = DriverManager
         .getConnection("jdbc:postgresql://localhost:5432/postgres",
         "postgres", "somkubo");
      c.setAutoCommit(false);
      //System.out.println("! Databaza otvorena !");
    }
    catch ( Exception e ) {
        //System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        //System.exit(0);
    	//Main.vypisArea.append("Pripojenie k databaze nie je mozne naviazat!");
        
      }
    
    try {
		client = TransportClient.builder().build()
		        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
    }
    
    public static void naplnenieComboboxov() throws SQLException
    {
    	// Naplnanie ElasticSearch databazy z PostgreSQL
    	// ---------------------------------------------------------------------------------------------------------
    	
    	//CreateIndexResponse createResponse = client.admin().indices().create(Requests.createIndexRequest("nemocnica")).actionGet();
    	
    	/*stmt = c.createStatement();
    	ResultSet result = stmt.executeQuery("SELECT meno as pmeno, id as pid FROM pacient;");
        while ( result.next() )
        {
        	String pmeno = result.getString("pmeno");
        	String pid = result.getString("pid");
        	try {
				IndexResponse iResponse = client.prepareIndex("nemocnica", "data")
						   .setSource(jsonBuilder()								
						          .startObject()
						          .field("id", ""+pid+"")								
						          .field("meno", ""+pmeno+"") 						                        			
						          .endObject()
						          )
						    .get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
    	
    	
    	// ---------------------------------------------------------------------------------------------------------
    	
    	String datum = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    	String cas = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
    	int a = 0;
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nazov FROM diagnoza;");
        while ( rs.next() )
        {
        	String nazov = rs.getString("nazov");
        	Main.diagnozaComboboxHodnoty[a] = nazov;
        	a++;
        }
        
        int b = 0;
    	stmt = c.createStatement();
        ResultSet rss = stmt.executeQuery("SELECT meno FROM lekar;");
        while ( rss.next() )
        {
        	String meno = rss.getString("meno");
        	Main.lekarComboboxHodnoty[b] = meno;
        	b++;
        }
        
        int d = 0;
    	stmt = c.createStatement();
        ResultSet r = stmt.executeQuery("SELECT druh FROM vykon;");
        while ( r.next() )
        {
        	String meno = r.getString("druh");
        	Main.vykonComboboxHodnoty[d] = meno;
        	d++;
        }
    }
    
    static void vypisPacientovPodlaId(String pacientId) throws SQLException
    {
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT pacient.meno AS pacientmeno, bydlisko, telefon, pohlavie, druh, datum, cas, popis, pacient_hospitalizovany, pacient_vylieceny, navsteva.id, lekar.meno AS lekarmeno, diagnoza.nazov FROM pacient, navsteva,"
        		+ " vykon, lekar, diagnoza WHERE navsteva.pacient_id = '"+pacientId+"' AND pacient.id='"+pacientId+"' AND vykon.id = navsteva.vykon_id AND lekar.id = navsteva.lekar_id AND diagnoza.id = navsteva.diagnoza_id;");
        
        int a=0;
        while ( rs.next() ) {
        	String meno = rs.getString("pacientmeno");
        	String bydlisko = rs.getString("bydlisko");
        	String telefon = rs.getString("telefon");
        	String pohlavie = rs.getString("pohlavie");
        	String hospitalizovany = rs.getString("pacient_hospitalizovany");
        	if (hospitalizovany.equals(pravda))
        		hospitalizovany = "ano";
        	else hospitalizovany = "nie";
    
        	String vylieceny = rs.getString("pacient_vylieceny");
        	if (vylieceny.equals(pravda))
        		vylieceny = "ano";
        	else
        		vylieceny = "nie";
        	if (a == 0)
        	{
        		Main.vypisArea.setText("");
        		Main.vypisArea.append("Meno pacienta: "+meno+"\nbydlisko: "+bydlisko+"\ntelefon: "+telefon+"\npohlavie:"+pohlavie+"\n"
        								+ "Hospitalizovany: "+hospitalizovany+", vylieceny: "+vylieceny+"\n");
        		a++;
        	}
        	String lekar = rs.getString("lekarmeno");
        	String id = rs.getString("id");
        	String diagnoza = rs.getString("nazov");
        	String druh = rs.getString("druh");
        	String datum = rs.getString("datum");
        	String cas = rs.getString("cas");
        	String popis = rs.getString("popis");
        	if (popis.length() > 15)
        		popis = popis.substring(0, 15);
        	Main.vypisArea.append("\nZaznam ("+id+"):\nLekar: "+lekar+"\ndiagnoza: "+diagnoza+"\n"+druh+", "+datum+", "+cas+"\nNahlad: "+popis+"...\n");
        }
        if (a == 0)
        {
        	ResultSet rss = stmt.executeQuery( "SELECT meno, bydlisko, telefon, pohlavie, pacient_hospitalizovany, pacient_vylieceny FROM pacient WHERE pacient.id='"+pacientId+"';");
        	rss.next();
        	Main.vypisArea.setText("");
        	String meno = rss.getString("meno");
        	String bydlisko = rss.getString("bydlisko");
        	String telefon = rss.getString("telefon");
        	String pohlavie = rss.getString("pohlavie");
        	String hospitalizovany = rss.getString("pacient_hospitalizovany");
        	System.out.println(hospitalizovany);
        	if (hospitalizovany.equals(pravda))
        		hospitalizovany = "ano";
        	else 
        		hospitalizovany = "nie";
    
        	String vylieceny = rss.getString("pacient_vylieceny");
        	if (vylieceny.equals(pravda))
        		vylieceny = "ano";
        	else
        		vylieceny = "nie";
        	
        	Main.vypisArea.append("Meno pacienta: "+meno+"\nbydlisko: "+bydlisko+"\ntelefon: "+telefon+"\npohlavie:"+pohlavie+"\n"
					+ "Hospitalizovany: "+hospitalizovany+", vylieceny: "+vylieceny+"\n");
        	Main.vypisArea.append("\nPacient nema ziadne zaznamy.");
        }
    }
    
    static void pridajPacienta(String pacientMeno, String pacientAdresa, String pacientPohlavie, String pacientTelefon, boolean pacientHospit, boolean pacientVylieceny, String lekar) throws SQLException
    {
    	stmt = c.createStatement();
    	String sql = ("BEGIN;"
    			+ "insert into public.pacient (meno,bydlisko,telefon,pohlavie, pacient_hospitalizovany, pacient_vylieceny) "
    			+ "values ('"+pacientMeno+"', '"+pacientAdresa+"', '"+pacientTelefon+"', '"+pacientPohlavie+"', "+pacientHospit+", "+pacientVylieceny+"); "
    			+ "insert into public.lieci "
    			+ "select lekar.id, pacient.id from lekar, pacient where lekar.meno='"+lekar+"' and pacient.meno='"+pacientMeno+"'; "
    			+ "COMMIT;");
        stmt.executeUpdate(sql);
    	Main.vypisArea.append("\n\nPacient bol pridany");
    	c.commit();
    	Connection con = c; // zapamatenie toho pridania SQL
    	
    	
    	stmt = c.createStatement();
    	ResultSet result = stmt.executeQuery("SELECT id FROM pacient WHERE meno = '"+pacientMeno+"';");
        while ( result.next() )
        {
        	String pid = result.getString("id");
    	try {
			IndexResponse iResponse = client.prepareIndex("nemocnica", "data")
					   .setSource(jsonBuilder()								
					          .startObject()
					          .field("id", ""+pid+"")								
					          .field("meno", ""+pacientMeno+"") 						                        			
					          .endObject()
					          )
					    .get();
		} catch (IOException e) {
			con.rollback(); // ak padne ElasticSearch, rollback SQL
			Main.vypisArea.setText("\nPridanie sa nepodarilo, rolling back changes");
			e.printStackTrace();
		}
        }
        
    }
    
    public static void aktualizujPacienta(String pacientId) throws SQLException
    {
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM pacient WHERE id="+pacientId+";");
        while ( rs.next() ) {
        	String meno = rs.getString("meno");
        	String bydlisko = rs.getString("bydlisko");
        	String telefon = rs.getString("telefon");
        	String pohlavie = rs.getString("pohlavie");
        	boolean hospit = rs.getBoolean("pacient_hospitalizovany");
        	boolean vyliec = rs.getBoolean("pacient_vylieceny");
        	Main.pMenoField.setText(meno);
        	Main.pAdresaField.setText(bydlisko);
        	Main.telefonField.setText(telefon);
        	Main.pohlavieField.setText(pohlavie);
        	Main.pMenoField.setText(meno);
        	Main.pacientHospitChbx.setSelected(hospit);
        	Main.pacientVyliecenyChbx.setSelected(vyliec);
        }
    }
    public static void updatePacienta(String pacientId, String pacientMeno, String pacientAdresa, String pacientPohlavie, String pacientTelefon, boolean pacientHospit, boolean pacientVylieceny) throws SQLException
    {
        stmt = c.createStatement();
        String sql = ("UPDATE public.pacient SET meno='"+pacientMeno+"', bydlisko='"+pacientAdresa+"', telefon='"+pacientTelefon+"', pohlavie='"+pacientPohlavie+"', pacient_hospitalizovany="+pacientHospit+", pacient_vylieceny="+pacientVylieceny+" where id="+pacientId+";");
        stmt.executeUpdate(sql);
        c.commit();
        Main.vypisArea.append("\nPacient bol aktualizovany\n");
        Connection con = c;
        
        
        sResponse = client.prepareSearch("nemocnica")							
		        .setTypes("data")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.setQuery(QueryBuilders.matchQuery("id", ""+pacientId+""))               	    		
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
				
        result = new ArrayList<Map<String, Object>>();
	
        String id = "";
        
        for (SearchHit hit : sResponse.getHits()) {             					       
        		id = hit.getId();
        		//System.out.println(id);
        DeleteResponse dResponse = client.prepareDelete("nemocnica", "data", id).get();
        }
        
        try {
			IndexResponse iResponse = client.prepareIndex("nemocnica", "data")
					   .setSource(jsonBuilder()								
					          .startObject()
					          .field("id", ""+pacientId+"")								
					          .field("meno", ""+pacientMeno+"") 						                        			
					          .endObject()
					          )
					    .get();
		} catch (IOException e) {
			con.rollback();
			Main.vypisArea.setText("Aktualizácia sa nepodarila, rolling back changes");
			e.printStackTrace();
		}
        
        
    }
    
    public static void vymazPacienta(String pacientId) throws SQLException
    {
    	Main.vypisArea.setText("");
    	stmt = c.createStatement();
        String sql = "DELETE FROM pacient WHERE id="+pacientId+";";
        stmt.executeUpdate(sql);
        c.commit();
        Main.vypisArea.append("Pacient a vsetky jeho polozky boli vymazane\n");
        
        
        
        
        sResponse = client.prepareSearch("nemocnica")							
		        .setTypes("data")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.setQuery(QueryBuilders.matchQuery("id", ""+pacientId+""))               	  		
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
				
        result = new ArrayList<Map<String, Object>>();
	
        String id = "";
        int i = 0;
        for (SearchHit hit : sResponse.getHits()) {             					      
        		id = hit.getId();
        		//System.out.println(id);
        DeleteResponse dResponse = client.prepareDelete("nemocnica", "data", id).get();
        i++;		
        }
        if (i == 0)
        {
        	c.rollback(); // ked nezbehol for, tak rollback, lebo sa teda nic nevymazalo
        	Main.vypisArea.append("Data neboli vymazane, rolling back changes");
        }
    }
    
    public static void pridajZaznam(String datum, String cas, String pacientId, String vykon, String diagnoza, String lekar, String text) throws SQLException
    {
   
    	ResultSet rss = stmt.executeQuery( "SELECT vykon.id as vykonid, diagnoza.id as diagnozaid, lekar.id as lekarid "
    			+ "FROM vykon, diagnoza, lekar "
    			+ "WHERE vykon.druh='"+vykon+"' AND diagnoza.nazov='"+diagnoza+"' AND lekar.meno='"+lekar+"';");
    	rss.next();
    	String vykonId = rss.getString("vykonid");
    	String diagnozaId = rss.getString("diagnozaid");
    	String lekarId = rss.getString("lekarid");
    	
    	stmt = c.createStatement();
    	String sql = ("insert into public.navsteva (pacient_id, vykon_id, datum, cas, popis, diagnoza_id, lekar_id) "
    				+ "values ('"+pacientId+"', '"+vykonId+"', '"+datum+"', '"+cas+"', '"+text+"', '"+diagnozaId+"', '"+lekarId+"');");
        stmt.executeUpdate(sql);
        
        Main.vypisArea.append("\nZaznam bol pridany\n");
    	c.commit();
    	
    }
    
    public static void vymazZaznam(String id) throws SQLException
    {
    	stmt = c.createStatement();
        String sql = "DELETE FROM navsteva WHERE id="+id+";";
        stmt.executeUpdate(sql);
        c.commit();
        Main.vypisArea.append("\nZaznam bol vymazany\n");
    }
    
    public static void zobrazZaznam(String id) throws SQLException
    {
    	ResultSet rss = stmt.executeQuery( "SELECT datum, cas, popis, druh, nazov, lekar.meno as lekarmeno, pacient.meno as pacientmeno FROM navsteva, vykon, diagnoza, lekar, pacient "
    			+ "WHERE navsteva.id='"+id+"' AND vykon.id = navsteva.vykon_id AND diagnoza.id = navsteva.diagnoza_id AND lekar.id = navsteva.lekar_id AND pacient.id = navsteva.pacient_id;");
    																
    	rss.next();
    	String datum = rss.getString("datum");
    	String cas = rss.getString("cas");
    	String popis = rss.getString("popis");
    	String druh = rss.getString("druh");
    	String nazov = rss.getString("nazov");
    	String lekar = rss.getString("lekarmeno");
    	String pasient = rss.getString("pacientmeno");
    	Main.vypisArea.setText("");
    	Main.vypisArea.append("Podrobny vypis zaznamu "+id+"\n"
    			+ "Meno pacienta: "+pasient+"\n"
    			+ "Cas: "+cas+" Datum: "+datum+"\n"
    			+ "Vykon: "+druh+", Diagnoza: "+nazov+"\n"
    			+ "Lekar: "+lekar+"\n\n"
    			+ popis);
    }
    
    public static void zobrazPacientovLekara(String lekar) throws SQLException
    {
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select pacient.meno, pacient.id from lekar "
        								+ "inner join lieci on lekar.id=lieci.lekar_id "
        								+ "inner join pacient on pacient.id=lieci.pacient_id "
        								+ "where lekar.meno = '"+lekar+"';");
        Main.vypisArea.setText("");
        Main.vypisArea.append("Pacienti registrovani lekarom: "+lekar+"\n");
        int a=0;
        while ( rs.next() )
        {
        	String meno = rs.getString("meno");
        	String id = rs.getString("id");
        	Main.vypisArea.append(meno+" ("+id+")\n");
        	a++;
        }
        if (a == 0)
        	Main.vypisArea.append("Lekar nema ziadnych registrovanych pacientov.");
    	
    }
    
    public static void zobrazVsetkychPacientov() throws SQLException
    {
    	Main.vypisArea.setText("");
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, meno FROM pacient;");
        while ( rs.next() )
        {
        	String meno = rs.getString("meno");
        	String id = rs.getString("id");
        	Main.vypisArea.append(meno+": "+id+"\n");
        }
    }
    // v SQL skripte funguje, prezentovane cez skript v PostgreSQL, akceptovane ako zlyhanie Javy
    /*public static void zobrazStatistiku() throws SQLException
    {
    	Main.vypisArea.setText("");
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("drop table if exists temp_table; "
        		+ "create temp table temp_table as "
        		+ "select diagnoza.nazov, pacient.meno from navsteva "
        		+ "join diagnoza on diagnoza.id=navsteva.diagnoza_id "
        		+ "join pacient on pacient.id=navsteva.pacient_id "
        		+ "group by pacient.meno, diagnoza.nazov "
        		+ "order by pacient.meno; "
        		+ "select nazov, count(nazov) as pocet from temp_table "
        		+ "group by nazov; ");
        Main.vypisArea.append("Pocet pacientov danej diagnozy\n\n");
        while ( rs.next() )
        {
        	String nazov = rs.getString("nazov");
        	String pocet = rs.getString("pocet");
        	Main.vypisArea.append(nazov+": "+pocet+"\n");
        }
        Main.vypisArea.append("\nOstatne oddelenia nemaju ziadnych pacientov\n");
    }*/
    
    public static void zobrazStatistiku() throws SQLException
    {
    	Main.vypisArea.setText("");
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select oddelenie.nazov, round(avg(pacient.pacient_vylieceny::int)*100,0) as priemer from oddelenie "
        		+ "inner join lekar on lekar.oddelenie_id=oddelenie.id "
        		+ "inner join lieci on lieci.lekar_id=lekar.id "
        		+ "inner join pacient on lieci.pacient_id=pacient.id "
        		+ "group by oddelenie.nazov;");
        Main.vypisArea.append("Pocet vyliecenych pacientov na danom oddeleni\n\n");
        while ( rs.next() )
        {
        	String nazov = rs.getString("nazov");
        	String priemer = rs.getString("priemer");
        	Main.vypisArea.append(nazov+": "+priemer+"\n");
        }
        Main.vypisArea.append("\nOstatne oddelenia nemaju ziadnych pacientov\n");
    }
    
    // TO DO!!!
    public static void zobrazVysetrenychPacientovLekara(String lekar) throws SQLException
    {
    	stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("select pacient.meno as pacient, pacient.id from navsteva, pacient, lekar "
				+ "where navsteva.lekar_id=lekar.id and navsteva.pacient_id=pacient.id and lekar.meno='"+lekar+"' "
				+ "group by pacient.id;");
        Main.vypisArea.setText("");
        Main.vypisArea.append("Pacienti vysetreni lekarom: "+lekar+"\n");
        int a=0;
        while ( rs.next() )
        {
        	String meno = rs.getString("pacient");
        	String id = rs.getString("id");
        	Main.vypisArea.append(meno+" ("+id+")\n");
        	a++;
        }
        if (a == 0)
        	Main.vypisArea.append("Lekar nema ziadnych pacientov.");
    	
    }
    
    public static void zobrazPacientovPodlaMena(String meno) throws SQLException
    {
    	sResponse = client.prepareSearch("nemocnica")							
		        .setTypes("data")
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
			.setQuery("{"
		        		+ "\"query\":{"
		        			+ "\"query_string\":{"
		        				+ "\"query\":\"*"+meno+"*\""
		        				+ "}"
		        			+ "}"
		        		+ "}")               			
		        .setFrom(0).setSize(60).setExplain(true)
		        .execute()
		        .actionGet();
				
        result = new ArrayList<Map<String, Object>>();
	
        String mena = "";
        String id = "";
        
        for (SearchHit hit : sResponse.getHits()) {             					     
        		mena = hit.getSource().get("meno").toString();
        		id = hit.getSource().get("id").toString();
        		Main.vypisArea.append("Meno: "+mena);
        		Main.vypisArea.append("   ID: "+id+"\n");
        }
    }
    
    
    
}
