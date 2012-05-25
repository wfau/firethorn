package asov.adql;

import adql.db.DBColumn;

import adql.db.DBTable;

import adql.query.ADQLObject;

import adql.query.operand.ADQLColumn;

import adql.query.operand.function.geometry.PointFunction;

import adql.search.SimpleReplaceHandler;

/**
 * Replace all POINT('...', ra, dec) by coord.
 */
public class ReplacePointHandler extends SimpleReplaceHandler {

	@Override
	protected boolean match(ADQLObject obj) {
		if (obj instanceof PointFunction){
			PointFunction point = (PointFunction)obj;
			if (point.getCoord1() instanceof ADQLColumn && point.getCoord2() instanceof ADQLColumn){
				ADQLColumn coord1 = (ADQLColumn)point.getCoord1(), coord2 = (ADQLColumn)point.getCoord2();
				if (coord1.getDBLink() == null)
					return coord1.getColumnName().equalsIgnoreCase("ra") && coord2.getColumnName().equalsIgnoreCase("dec");
				else
					return coord1.getDBLink().getDBName().equalsIgnoreCase("ra") && coord2.getDBLink().getDBName().equalsIgnoreCase("dec");
			}
		}
		return false;
	}

	@Override
	protected ADQLObject getReplacer(ADQLObject obj) throws UnsupportedOperationException {
		PointFunction point = (PointFunction)obj;
		if (((ADQLColumn)point.getCoord1()).getDBLink() == null)
			return new ADQLColumn("coord");
		else{
			DBTable t = ((ADQLColumn)point.getCoord1()).getDBLink().getTable();
			DBColumn coordColumn = t.getColumn("coord", false);
			ADQLColumn col = new ADQLColumn("coord");
			col.setDBLink(coordColumn);
			return col;
		}
	}

}
