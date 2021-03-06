package com.rav.audtioapp.dao.audio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.rav.audtioapp.dao.DAOUtil;
import com.rav.audtioapp.dto.DownloadAudioFilesDTO;

public class DownloadAudioFilesDAO {

	public DownloadAudioFilesDAO() {

		try {
			Statement stmt = DAOUtil.getInstance().getConnection().createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS \"AudioSubmission_Details\" "
					+ "(id numeric NOT NULL, bag text, cot text, gang text, past text, spa text, band text, "
					+ "deck text, house text, pasta text, test text, boat text, duck text, how text, pool text, "
					+ "tie text, boot text, face text, kiss text, seat text, tight text, caught text, far text, "
					+ "pack text, sharp text, too text, longitude character varying(4000), lattitude character varying(4000), "
					+ "birthyear character varying(4000), gender character varying(4000), mothertoungedata character varying(4000), "
					+ "ratefluency character varying(4000),  atwhatage character varying(4000),  emailaddress character varying(4000),  "
					+ "  bornincanada character varying(4000), ifnotmothertounge character varying(4000),\"approvedBy\" character varying(4000),"
					+ "status character varying(4000), state character varying(4000), city character varying(4000), "
					+ "CONSTRAINT \"AudioSubmission_Details_pkey\" PRIMARY KEY (id) )");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getAllVoices(int id) {
		Map<String, String> voices = null;
		Connection connection = DAOUtil.getInstance().getConnection();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			String sql = "select bag, cot, gang, past, "
					+ "spa, band, deck, house, pasta, test,boat, duck, how, pool, tie, boot, face, kiss, seat, tight, caught,  far, pack, "
					+ "sharp, too FROM \"AudioSubmission_Details\" where id = " + id;
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();

				for (int j = 1; j < rsmd.getColumnCount() + 1; j++) {
					String voice = rs.getString(rsmd.getColumnName(j));
					if (voice != null) {
						if (voices == null)
							voices = new HashMap<String, String>();
						voices.put("CVMX-" + id + "_" + rsmd.getColumnName(j) + ".wav",
								voice.substring("data:audio/wav;base64,".length()).replace("\\", "").trim());
					}
				}

			}
			statement.close();
		} catch (Exception e) {
			System.err.println(e);
		} finally {
			try {
				connection.close();
				connection = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return voices;
	}

	public Map<String, String> getVoices(DownloadAudioFilesDTO dto) {
		Map<String, String> voices = null;
		int minimumAge = getInt(dto.getMinimumAge());
		int maximumAge = getInt(dto.getMaximumAge());

		if (dto.getWordsSelected() != null && dto.getWordsSelected().trim().length() > 2) {
			String col = "id," + dto.getWordsSelected();
			if (col.trim().length() > 2) {
				Connection connection = DAOUtil.getInstance().getConnection();
				Statement statement = null;
				try {
					statement = connection.createStatement();
					String sql = "select " + col + " FROM \"AudioSubmission_Details\" where status  ='APPROVED'";
					if (dto.getGendersSelected() != null && dto.getGendersSelected().trim().length() > 2) {
						String genders = dto.getGendersSelected();
						StringTokenizer token = new StringTokenizer(genders, ",");
						String gendersWhere = "";
						while (token.hasMoreTokens()) {
							gendersWhere += "'" + token.nextToken() + "',";
						}

						gendersWhere = gendersWhere.substring(0, gendersWhere.length() - 1);

						sql += " and gender in (" + gendersWhere + ")";
					}

					if (dto.getNativeLanguageSelected() != null
							&& dto.getNativeLanguageSelected().trim().length() > 2) {

						String nativeLanguages = dto.getNativeLanguageSelected();

						StringTokenizer token = new StringTokenizer(nativeLanguages, ",");
						String nativeLanguagesWhere = "";

						while (token.hasMoreTokens()) {
							String temp = token.nextToken();
							if (temp.equals("Yes"))
								nativeLanguagesWhere += "'true',";
							if (temp.equals("No"))
								nativeLanguagesWhere += "'false',";
						}

						nativeLanguagesWhere = nativeLanguagesWhere.substring(0, nativeLanguagesWhere.length() - 1);

						sql += " and mothertoungedata in (" + nativeLanguagesWhere + ")";
					}

					if (minimumAge > 0) {
						int year = Calendar.getInstance().get(Calendar.YEAR);
						sql += " and cast(birthyear as int)  <=" + (year - minimumAge);
					}
					if (maximumAge > 0) {
						int year = Calendar.getInstance().get(Calendar.YEAR);
						sql += " and cast(birthyear as int)  >=" + (year - maximumAge);
					}
					System.out.println(sql);
					ResultSet rs = statement.executeQuery(sql);
					while (rs.next()) {
						ResultSetMetaData rsmd = rs.getMetaData();
						int id = rs.getInt(1);
						for (int j = 2; j < rsmd.getColumnCount() + 1; j++) {
							String voice = rs.getString(rsmd.getColumnName(j));
							if (voice != null) {
								if (voices == null)
									voices = new HashMap<String, String>();
								voices.put("CVMX-" + id + "_" + rsmd.getColumnName(j) + ".wav",
										voice.substring("data:audio/wav;base64,".length()).replace("\\", "").trim());
							}
						}
					}
					statement.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						connection.close();
						connection = null;
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return voices;
	}

	private int getInt(String data) {
		int integer = -1;
		if (data != null && data.trim().length() > 0) {
			String numberOnly = data.replaceAll("[^0-9]", "");
			if (numberOnly.trim().length() > 0) {
				integer = Integer.parseInt(numberOnly);
			}
		}
		return integer;
	}

}
