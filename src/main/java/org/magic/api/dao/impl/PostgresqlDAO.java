package org.magic.api.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jooq.SQLDialect;
import org.magic.api.beans.MTGCard;
import org.magic.api.beans.MTGCollection;
import org.magic.api.beans.MTGEdition;
import org.magic.api.beans.MTGGrading;
import org.magic.api.beans.technical.MTGProperty;
import org.magic.api.interfaces.MTGStockItem;
import org.magic.api.interfaces.abstracts.extra.AbstractMagicSQLDAO;
import org.postgresql.util.PGobject;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PostgresqlDAO extends AbstractMagicSQLDAO {


	@Override
	protected String getjdbcnamedb() {
		return "postgresql";
	}

	@Override
	protected SQLDialect getDialect() {
		return SQLDialect.POSTGRES;
	}

	@Override
	protected void storeCard(PreparedStatement pst, int position, MTGCard mc) throws SQLException {
		var jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(serialiser.toJsonElement(mc).toString());
		pst.setObject(position, jsonObject);
	}


	@Override
	protected void storeTransactionItems(PreparedStatement pst, int position, List<MTGStockItem> grd) throws SQLException {
		var jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(serialiser.toJsonElement(grd).toString());
		pst.setObject(position,jsonObject );

	}

	@Override
	protected void storeGrade(PreparedStatement pst, int position, MTGGrading grd) throws SQLException {
		var jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(serialiser.toJsonElement(grd).toString());
		pst.setObject(position, jsonObject);
	}

	
	@Override
	public List<MTGCard> listCardsFromCollection(MTGCollection collection, MTGEdition me) throws SQLException {

		var ret = new ArrayList<MTGCard>();
		var sql = "SELECT distinct(idmc) FROM stocks WHERE qte > 0 AND collection= ?";

		if (me != null)
			sql = "SELECT distinct(idmc) FROM stocks WHERE qte > 0 AND collection= ? and idMe = ?";

		try (var c = pool.getConnection(); PreparedStatement pst = c.prepareStatement(sql)) {
			pst.setString(1, collection.getName());
			if (me != null)
				pst.setString(2, me.getId());
			try (ResultSet rs = executeQuery(pst)) {
					while (rs.next()) 
					{
						try (var c2 = pool.getConnection(); var pst2 = c2.prepareStatement("SELECT mcard FROM stocks WHERE idmc = ? AND collection= ?")) {
							pst2.setString(1,rs.getString(1) );
							pst2.setString(2, collection.getName());
							var rs2 = executeQuery(pst2);
							
							if(rs2.next()) {
							var mc = readCard(rs2, MCARD);
							notify(mc);
							ret.add(mc);
							}
					}
				}
			}
		}
		return ret;	
	}

	@Override
	protected void storeTiersApps(PreparedStatement pst, int i, Map<String, String> tiersAppIds) throws SQLException {

		var jsonObject = new PGobject();
		jsonObject.setType("json");
		jsonObject.setValue(serialiser.toJsonElement(tiersAppIds).toString());
		pst.setObject(i, jsonObject);
	}

	@Override
	protected void storeDeckBoard(PreparedStatement pst, int i, Map<MTGCard, Integer> board) throws SQLException {
		var jsonObject = new PGobject();
		jsonObject.setType("json");
		var arr = new JsonArray();
		board.entrySet().forEach(e->{
			var obj = new JsonObject();
			obj.addProperty("qty", e.getValue());
			obj.add("card", serialiser.toJsonElement(e.getKey()));
			arr.add(obj);
		});
		jsonObject.setValue(arr.toString());

		pst.setObject(i, jsonObject);
	}



	@Override
	protected String getdbSizeQuery() {
		return "select table_name, pg_relation_size(quote_ident(table_name)) from information_schema.tables where table_schema = '"+getString(DB_NAME)+"'order by 2";
	}

	@Override
	public Map<String,Long> getDBSize() {

		var map= new HashMap<String,Long>();

		try (var c = pool.getConnection(); PreparedStatement pst = c.prepareStatement(getdbSizeQuery(),ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY,ResultSet.HOLD_CURSORS_OVER_COMMIT); ResultSet rs = executeQuery(pst);) {
			while(rs.next())
				map.put(rs.getString(1), rs.getLong(2));


		} catch (SQLException e) {
			logger.error(e);
		}
		return map;
	}

	@Override
	public String getName() {
		return "PostGreSQL";
	}

	@Override
	public Map<String, MTGProperty> getDefaultAttributes() {

		var m = super.getDefaultAttributes();
		m.get(SERVERPORT).setDefaultValue("5432");
		return m;
	}


}
