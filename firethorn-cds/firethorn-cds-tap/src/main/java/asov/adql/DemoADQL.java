package asov.adql;

import adql.db.DBChecker;

import adql.parser.ADQLParser;
import adql.parser.ParseException;

import adql.query.ADQLQuery;

import adql.search.IReplaceHandler;
import adql.translator.ADQLTranslator;
import adql.translator.PgSphereTranslator;
import adql.translator.TranslationException;

import asov.DemoASOV;

public class DemoADQL {

	public static void main(String[] args) {

		// 1. PARSE ADQL
		ADQLQuery query = null;
		try{
			// Build the ADQL parser:
			ADQLParser parser = new ADQLParser();

			// Check DB consistency:
			parser.setQueryChecker(new DBChecker(DemoASOV.getDBTables()));
			// Restrict coordinate systems:
			parser.setCoordinateSystems(DemoASOV.getCoordinateSystems());

			// Parse:
			query = parser.parseQuery(System.in);
			System.out.println("Correct ADQL !");

		}catch(ParseException pe){
// ZRQ
//			System.err.println("ADQL syntax incorrect between (l."+pe.getBeginLine()+",c."+pe.getBeginColumn()+") and (l."+pe.getEndLine()+",c."+pe.getEndColumn()+"): "+pe.getMessage());
			System.err.println("ADQL syntax incorrect AT [" + pe.getPosition() + "][" + pe.getMessage() + "]");
			System.exit(2);
		}

		// MANIPULATE ADQL (OPTIONAL)
		IReplaceHandler replacer = new ReplacePointHandler();
		replacer.searchAndReplace(query);
		if (replacer.getNbMatch() > 0)
			System.out.println("INFO: "+replacer.getNbReplacement()+"/"+replacer.getNbMatch()+" replacements done");

		// 2. TRANSLATE ADQL INTO SQL
		try{
			// Build the translator (for PostgreSQL+PgSphere):
			ADQLTranslator translator = new PgSphereTranslator();

			// Translate:
			String sql = translator.translate(query);
			System.out.println("*** SQL ***\n"+sql);

		}catch(TranslationException te){
			System.err.println("Translation into SQL failed: "+te.getMessage());
		}

	}

}
