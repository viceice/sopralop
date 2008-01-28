/**
 * @version		$Id$
 * @copyright	(c)2007 Michael Kriese & Peer Sterner
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
 * 28.01.2008 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.input;

import info.kriese.soPra.gui.InputPanel;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.lop.LOPSolution;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 28.01.2008
 * 
 */
public class SpecialCaseInputPanel extends JPanel implements SpecialCasesInput {

    /** */
    private static final long serialVersionUID = 1L;

    private final List<ButtonGroup> groups;

    private int optimalSolArea = 0, solArea = 0, targetFunction = 0,
	    solProjTo = 0;

    public SpecialCaseInputPanel() {

	setLayout(new GridLayout(0, 1));
	setBorder(BorderFactory.createEmptyBorder());

	this.groups = new LinkedList<ButtonGroup>();

	ButtonGroup bg;
	JRadioButton rb;
	JPanel pn;

	bg = new ButtonGroup();
	this.groups.add(bg);
	pn = createPanel(Lang.getString("Input.Panel.SolutionArea.Title"));

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.SolutionArea.IsEmpty"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.solArea = LOPSolution.SOLUTION_AREA_EMPTY;
	    }
	});

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.SolutionArea.IsLimited"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.solArea = LOPSolution.SOLUTION_AREA_LIMITED;
	    }
	});

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.SolutionArea.IsUnlimited"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.solArea = LOPSolution.SOLUTION_AREA_UNLIMITED;
	    }
	});

	// bg = new ButtonGroup();
	// this.groups.add(bg);
	// pn = createPanel(Lang
	// .getString("Input.Panel.SolutionProjectedTo.Title"));
	//
	// rb = createBtn(bg, pn, Lang
	// .getString("Input.Panel.SolutionProjectedTo.Nothing"));
	// rb.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// SpecialCaseInputPanel.this.solProjTo =
	// LOPSolution.SOLUTION_AREA_EMPTY;
	// }
	// });
	//
	// rb = createBtn(bg, pn, Lang
	// .getString("Input.Panel.SolutionProjectedTo.Ray"));
	// rb.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// SpecialCaseInputPanel.this.solProjTo =
	// LOPSolution.SOLUTION_AREA_LIMITED;
	// }
	// });
	//
	// rb = createBtn(bg, pn, Lang
	// .getString("Input.Panel.SolutionProjectedTo.Line"));
	// rb.addActionListener(new ActionListener() {
	// public void actionPerformed(ActionEvent e) {
	// SpecialCaseInputPanel.this.solProjTo =
	// LOPSolution.SOLUTION_AREA_UNLIMITED;
	// }
	// });

	bg = new ButtonGroup();
	this.groups.add(bg);
	pn = createPanel(Lang
		.getString("Input.Panel.OptimalSolutionArea.Title"));

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.OptimalSolutionArea.IsEmpty"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.optimalSolArea = LOPSolution.OPTIMAL_SOLUTION_AREA_EMPTY;
	    }
	});

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.OptimalSolutionArea.IsPoint"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.optimalSolArea = LOPSolution.OPTIMAL_SOLUTION_AREA_POINT;
	    }
	});

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.OptimalSolutionArea.IsMultiple"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.optimalSolArea = LOPSolution.OPTIMAL_SOLUTION_AREA_MULTIPLE;
	    }
	});

	bg = new ButtonGroup();
	this.groups.add(bg);
	pn = createPanel(Lang.getString("Input.Panel.TargetFunction.Title"),
		true);

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.TargetFunction.IsEmpty"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.targetFunction = LOPSolution.TARGET_FUNCTION_EMPTY;
	    }
	});

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.TargetFunction.IsLimited"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.targetFunction = LOPSolution.TARGET_FUNCTION_LIMITED;
	    }
	});

	rb = createBtn(bg, pn, Lang
		.getString("Input.Panel.TargetFunction.IsUnlimited"));
	rb.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SpecialCaseInputPanel.this.targetFunction = LOPSolution.TARGET_FUNCTION_UNLIMITED;
	    }
	});
    }

    public int getSpecialCase() {
	return this.optimalSolArea | this.solArea | this.targetFunction
		| this.solProjTo;
    }

    public void reset() {
	for (ButtonGroup grp : this.groups)
	    grp.clearSelection();

	this.optimalSolArea = 0;
	this.solArea = 0;
	this.targetFunction = 0;
	this.solProjTo = 0;
    }

    /**
     * @param bg
     * @param pn
     */
    private JRadioButton createBtn(ButtonGroup bg, JPanel pn, String title) {
	JRadioButton rb = new JRadioButton(title);
	pn.add(rb);
	bg.add(rb);

	return rb;
    }

    private JPanel createPanel(String title) {
	return createPanel(title, false);
    }

    private JPanel createPanel(String title, boolean last) {
	JPanel pn;
	pn = new JPanel();
	pn.setBorder(InputPanel.createBorder(title, last));
	pn.setLayout(new GridLayout(1, 0));
	add(pn);
	return pn;
    }

}
