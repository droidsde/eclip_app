package com.security.virusscanner.antivirus.free;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.database.SQLException;

public class LGD {

	private DataHelper d;

	private List<CusStruct> st1;
	private List<CusStruct> st2;

	public LGD(Context t) {
		d = new DataHelper(t);
		ret();
	}

	private void ret() {

		try {

			d.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			d.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}

		st1 = d.getStrings(0x0a786564);
		st2 = d.getStrings(0x464c457f);

		map();

	}

	private void map() {

		Envi.cusSt1 = st1;
		Envi.cusSt2 = st2;
		d.close();

	}

	public void rel() {
		st1.clear();
		st2.clear();

	}

}
