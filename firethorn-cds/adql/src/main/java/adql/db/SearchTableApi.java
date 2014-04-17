package adql.db;

import java.util.ArrayList;
import java.util.List;

import adql.query.IdentifierField;
import adql.query.from.ADQLTable;
import cds.utils.TextualSearchList;

public interface SearchTableApi
    {

    /**
     * Searches all {@link DBTable} elements which has the given name (case insensitive).
     * 
     * @param table ADQL name of {@link DBTable} to search for.
     * 
     * @return		The corresponding {@link DBTable} elements.
     * 
     * @see TextualSearchList#get(String)
     */
    public List<DBTable> search(final String table);

    /**
     * Searches all {@link DBTable} elements which have the given catalog, schema, and table name (case insensitive).
     * 
     * @param catalog	Catalog name.
     * @param schema	Schema name.
     * @param table		Table name.
     * 
     * @return			The list of all matching {@link DBTable} elements.
     * 
     * @see #search(String, String, String, byte)
     */
    public List<DBTable> search(final String catalog, final String schema, final String table);

    /**
     * Searches all {@link DBTable} elements corresponding to the given {@link ADQLTable} (case insensitive).
     * 
     * @param table	An {@link ADQLTable}.
     * 
     * @return		The list of all corresponding {@link DBTable} elements.
     * 
     * @see #search(String, String, String, byte)
     */
    public List<DBTable> search(final ADQLTable table);

    /**
     * Searches all {@link DBTable} elements which have the given catalog, schema, and table name, with the specified case sensitivity.
     * 
     * @param catalog			Catalog name.
     * @param schema			Schema name.
     * @param table				Table name.
     * @param caseSensitivity	Case sensitivity for each table parts (one bit by part ; 0=sensitive,1=insensitive ; see {@link IdentifierField} for more details).
     * 
     * @return					The list of all matching {@link DBTable} elements.
     * 
     * @see IdentifierField
     */
    public List<DBTable> search(final String catalog, final String schema, final String table, final byte caseSensitivity);

    }
