/**
 * @version		$Id$
 * @copyright	(c)2007-2008 Michael Kriese & Peer Sterner
 * 
 * This file is part of SoPraLOP Project.
 *
 *  SoPraLOP Project is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  SoPraLOP Project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SoPraLOP Project; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * ChangeLog:
 * 
 * 20.05.2008 - Version 0.4
 * - neue Methode implementiert
 * 03.12.2007 - Version 0.3
 * - Methode check implementiert
 * 09.11.2007 - Version 0.2.1
 * - Aufruf von update erst nach problemSolved
 * 06.11.2007 - Version 0.2
 * - Neue Interfacemethoden implementiert
 * 01.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop.impl;

import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.LOPEditorListener;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * 
 * @author Michael Kriese
 * @version 0.4
 * @since 01.11.2007
 * 
 */
public class LOPEditorImpl implements LOPEditor {

    private boolean edited = false;
    private final List<LOPEditorListener> listeners;

    private LOP lop;

    public LOPEditorImpl(LOP lop) {
	this.lop = lop;
	this.listeners = new LinkedList<LOPEditorListener>();
	this.lop.addProblemListener(new LOPAdapter() {
	    @Override
	    public void problemSolved(LOP lop) {
		LOPEditorImpl.this.lop = lop;
		LOPEditorImpl.this.update();
	    }
	});
    }

    public void addListener(LOPEditorListener l) {
	this.listeners.add(l);
    }

    public void addVariable() {
	for (LOPEditorListener l : this.listeners)
	    l.addVariable(this.lop);
    }

    public void captureImage(final File file) {
	SwingUtilities.invokeLater(new Runnable() {

	    public void run() {
		for (LOPEditorListener l : LOPEditorImpl.this.listeners)
		    l.captureImage(LOPEditorImpl.this.lop, file);
	    }
	});

    }

    public void check() {
	for (LOPEditorListener l : this.listeners)
	    l.check(this.lop);
    }

    public void clear() {
	for (LOPEditorListener l : this.listeners)
	    l.clear(this.lop);
    }

    public LOP getLOP() {
	return this.lop;
    }

    public boolean isEdited() {
	return this.edited;
    }

    public void open(URL file) {
	boolean opened = true;
	for (LOPEditorListener l : this.listeners)
	    opened &= l.open(this.lop, file);

	if (opened)
	    solve();
    }

    public void removeListener(LOPEditorListener l) {
	this.listeners.add(l);
    }

    public void removeVariable() {
	for (LOPEditorListener l : this.listeners)
	    l.removeVariable(this.lop);
    }

    public void save(URL file) {
	for (LOPEditorListener l : this.listeners)
	    l.save(this.lop, file);
    }

    public void setEdited(boolean value) {
	this.edited = value;
    }

    public void solve() {
	for (LOPEditorListener l : this.listeners)
	    l.solve(this.lop);
    }

    public void take() {
	boolean opened = true;
	for (LOPEditorListener l : this.listeners)
	    opened &= l.take(this.lop);

	if (opened)
	    solve();
    }

    public void update() {
	for (LOPEditorListener l : this.listeners)
	    l.update(this.lop);
    }

}
