package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class BodiesTable extends JPanel {
	
	BodiesTable(Controller ctrl) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.black, 2),
				"Bodies",
				TitledBorder.LEFT, TitledBorder.TOP));
		JScrollPane bt = new JScrollPane(
				new JTable(new BodiesTableModel(ctrl)), 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(bt);
	}
	
	public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

		private List<Body> _bodies;
		private String[] _header = { "Id", "Mass", "Position", "Velocity", "Force" };
		private String[][] _data;
		
		BodiesTableModel(Controller ctrl) {
			_bodies = new ArrayList<>();
			ctrl.addObserver(this);
		}
		
		@Override
		public int getRowCount() {
			return _data.length;
			
		}
		
		@Override
		public int getColumnCount() {	
			return _header.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return _header[column];
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return _data[rowIndex][columnIndex];
		}
		
		// SimulatorObserver methods
		
				/*En los métodos como observador, cuando cambia el estado
			(por ejemplo en onAdvance, onRegister, onBodyAdded y
			onReset) es necesario en primer lugar actualizar el valor del
			campo _bodies y después llamar a
			fireTableStructureChanged() para notificar a la
			correspondiente JTable que hay cambios en la tabla (y por lo
			tanto es necesario redibujarla). Haz estas actualizaciones con
			invokeLater. */

		@Override
		public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
			_bodies = bodies;
		}

		@Override
		public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBodyAdded(List<Body> bodies, Body b) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAdvance(List<Body> bodies, double time) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeltaTimeChanged(double dt) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onForceLawsChanged(String fLawsDesc) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
