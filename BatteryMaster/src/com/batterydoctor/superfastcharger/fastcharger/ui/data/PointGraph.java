package com.batterydoctor.superfastcharger.fastcharger.ui.data;

public final class PointGraph {
	public boolean isNote;// ?i?m c� ph?i note ko?
	public boolean isExis;// ?i?m c� t?n t?i kh�ng
	public int index;
	public PointGraph() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param n
	 *            : is notes
	 * @param e
	 *            : is exis
	 * @param in
	 *            : index
	 */
	public PointGraph(boolean n, boolean e, int in) {
		// TODO Auto-generated constructor stub
		this.isNote = n;
		this.isExis = e;
		this.index = in;
	}
}