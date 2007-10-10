package stuff;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class JTableTestJFrame extends JFrame implements ActionListener {

    private static final class Address {
	private String mail;

	private boolean male;

	private String name;

	public Address() {
	    this("", "", true);
	}

	/**
	 * @param name
	 * @param mail
	 * @param male
	 */
	public Address(String name, String mail, boolean male) {
	    super();
	    this.name = name;
	    this.mail = mail;
	    this.male = male;
	}

	/**
	 * @return the mail
	 */
	public String getMail() {
	    return this.mail;
	}

	/**
	 * @return the name
	 */
	public String getName() {
	    return this.name;
	}

	/**
	 * @return the male
	 */
	public boolean isMale() {
	    return this.male;
	}

	/**
	 * @param mail
	 *                the mail to set
	 */
	public void setMail(String mail) {
	    this.mail = mail;
	}

	/**
	 * @param male
	 *                the male to set
	 */
	public void setMale(boolean male) {
	    this.male = male;
	}

	/**
	 * @param name
	 *                the name to set
	 */
	public void setName(String name) {
	    this.name = name;
	}
    }

    class AddressTableModel extends AbstractTableModel {
	/** */
	private static final long serialVersionUID = -5077784829028346274L;

	public Object[] longValues = { "Michael Kriese",
		"michael.kriese@student.uni-magdeburg.de", Boolean.TRUE };

	private final Vector<Address> list;

	String[] columns = { "Name", "eMail", "male" };

	public AddressTableModel(Vector<Address> list) {
	    this.list = list;
	    String tmp = "AddressFrame.Columns";
	    if (tmp != null) {
		String[] cols = tmp.split("#");
		if (cols.length == 3)
		    this.columns = cols;
	    }
	}

	public void addAddress() {
	    this.list.add(new Address());
	    fireTableDataChanged();
	}

	@Override
	public Class<?> getColumnClass(int col) {
	    switch (col) {
	    case 0:
		return String.class;
	    case 1:
		return String.class;
	    case 2:
		return Boolean.class;
	    default:
		return String.class;
	    }
	}

	public int getColumnCount() {
	    return this.columns.length;
	}

	@Override
	public String getColumnName(int column) {

	    return this.columns[column];
	}

	public Vector<Address> getList() {
	    return this.list;
	}

	public int getRowCount() {
	    return this.list.size();
	}

	public Object getValueAt(int row, int col) {
	    if (this.list.size() == 0)
		return null;
	    switch (col) {
	    case 0:
		return (this.list.get(row)).getName();
	    case 1:
		return (this.list.get(row)).getMail();
	    case 2:
		return Boolean.valueOf((this.list.get(row)).isMale());
	    default:
		return null;
	    }
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

	    return true;
	}

	public void removeAddress() {
	    int[] rows = JTableTestJFrame.this.listTable.getSelectedRows();

	    for (int element : rows)
		this.list.remove(element);

	    fireTableDataChanged();
	}

	@Override
	public void setValueAt(Object val, int row, int col) {
	    switch (col) {
	    case 0:
		(this.list.get(row)).setName((String) val);
		break;
	    case 1:
		(this.list.get(row)).setMail((String) val);
		break;
	    case 2:
		(this.list.get(row)).setMale(((Boolean) val).booleanValue());
		break;
	    }

	    fireTableCellUpdated(row, col);
	}

    }

    /** */
    private static final long serialVersionUID = 573258740324950961L;

    public static void main(String[] args) {
	Vector<Address> vec = new Vector<Address>();
	vec.add(new Address("Michael Kriese", "michael@kriese.info", true));
	vec.add(new Address("Peer Sterner", "pe@anipe.de", true));
	JFrame f = new JTableTestJFrame(null, vec);
	f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	f.setVisible(true);
    }

    private final JTable listTable;

    public JTableTestJFrame(JFrame owner, Vector<Address> addr) {
	super("AddressFrame");
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	setLayout(new BorderLayout(2, 2));
	setSize(400, 400);

	// ImageIcon ico = MenuMaker.getImage("Menu.Edit.Address");
	// if (ico != null)
	// setIconImage(ico.getImage());

	// Point p = owner.getLocation();
	// p.translate(-getWidth(), 0);
	// setLocation(p);

	this.listTable = new JTable(new AddressTableModel(addr));
	this.listTable.setPreferredScrollableViewportSize(new Dimension(350,
		200));
	add(new JScrollPane(this.listTable), BorderLayout.CENTER);

	initColumnSizes();

	generateEditTollbar();
    }

    public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();

	if (cmd.equals("AddressFrame.Menu.Add"))
	    ((AddressTableModel) this.listTable.getModel()).addAddress();
	else if (cmd.equals("AddressFrame.Menu.Delete"))
	    ((AddressTableModel) this.listTable.getModel()).removeAddress();
    }

    public Vector<Address> getAddress() {
	return ((AddressTableModel) this.listTable.getModel()).getList();
    }

    private void generateEditTollbar() {
	JToolBar toolbar;
	toolbar = new JToolBar("AddresFrame.Menu");
	//
	JButton btn = new JButton("AddressFrame.Menu.Add");
	btn.addActionListener(this);
	toolbar.add(btn);

	btn = new JButton("AddressFrame.Menu.Delete");
	btn.addActionListener(this);
	toolbar.add(btn);

	add(toolbar, BorderLayout.PAGE_START);
    }

    private void initColumnSizes() {
	AddressTableModel model = (AddressTableModel) this.listTable.getModel();
	TableColumn column = null;
	Component comp = null;
	int headerWidth = 0;
	int cellWidth = 0;
	Object[] longValues = model.longValues;
	TableCellRenderer headerRenderer = this.listTable.getTableHeader()
		.getDefaultRenderer();

	for (int i = 0; i < model.getColumnCount(); i++) {
	    column = this.listTable.getColumnModel().getColumn(i);

	    comp = headerRenderer.getTableCellRendererComponent(null, column
		    .getHeaderValue(), false, false, 0, 0);
	    headerWidth = comp.getPreferredSize().width;

	    comp = this.listTable.getDefaultRenderer(model.getColumnClass(i))
		    .getTableCellRendererComponent(this.listTable,
			    longValues[i], false, false, 0, i);
	    cellWidth = comp.getPreferredSize().width;

	    column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	}
    }

}
