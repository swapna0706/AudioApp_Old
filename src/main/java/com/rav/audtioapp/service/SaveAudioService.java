package com.rav.audtioapp.service;

import com.rav.audtioapp.constants.param.ParamConstants;
import com.rav.audtioapp.dao.audio.SaveAudioDAO;
import com.rav.audtioapp.dao.param.ParamsDAO;
import com.rav.audtioapp.dto.SaveAudioDTO;
import com.rav.audtioapp.util.SendMailCommunication;

public class SaveAudioService {

	public String processRequest(SaveAudioDTO dto) {
		SaveAudioDAO dao = new SaveAudioDAO();
		int id = dao.insertNewAudio(dto);

		try {
			new Thread(new SendMailCommunication(new ParamsDAO().getParamValue(ParamConstants.STRATHY_EMAIL_ADDRESS),
					new ParamsDAO().getParamValue(ParamConstants.STRATHY_EMAIL_BODY).replaceAll("$ID$", "CVMX-" + id),
					new ParamsDAO().getParamValue(ParamConstants.STRATHY_EMAIL_SUBJECT).replaceAll("$ID$",
							"CVMX-" + id))).start();

			new Thread(new SendMailCommunication(dto.getEmailAddress(),
					new ParamsDAO().getParamValue(ParamConstants.STRATHY_EMAIL_TO_END_USER).replaceAll("$ID$",
							"CVMX-" + id),
					new ParamsDAO().getParamValue(ParamConstants.STRATHY_EMAIL_SUBJECT_TO_END_USER).replaceAll("$ID$",
							"CVMX-" + id))).start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "CVMX-" + id;
	}

}
