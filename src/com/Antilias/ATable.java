package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class ATable extends JTable {

	/**
	 * 抗锯齿化
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		super.paint(g2);
	}

	public ATable() {
		// TODO Auto-generated constructor stub
	}

	public ATable(TableModel dm) {
		super(dm);
		// TODO Auto-generated constructor stub
	}

	public ATable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
		// TODO Auto-generated constructor stub
	}

	public ATable(int numRows, int numColumns) {
		super(numRows, numColumns);
		// TODO Auto-generated constructor stub
	}

	public ATable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
		// TODO Auto-generated constructor stub
	}

	public ATable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
		// TODO Auto-generated constructor stub
	}

	public ATable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
		// TODO Auto-generated constructor stub
	}

}
