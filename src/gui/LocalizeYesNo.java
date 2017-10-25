/*
 * The MIT License
 *
 * Copyright 2017 Daniel.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package gui;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class LocalizeYesNo {
	
	// Since this example class is standalone from JMRI, I have to simulate "SystemType.isWindows() || SystemType.isLinux()"
	public static boolean isWindowsOrLinux = true;

	private static LocalizeYesNo localizeYesNo = null;
	private static final Object lock = new Object();

	private final String yes;
	private final String no;
	private final String ok;
	private final String cancel;

	// This class is a singleton
	private LocalizeYesNo() {
		JOptionPane optionPane = new JOptionPane(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = optionPane.createDialog(null, "Input Dialog");
		dialog.setModal(false);
		dialog.setVisible(true);
		if (isWindowsOrLinux) {
			yes = getButtonText(dialog, true);
			no = getButtonText(dialog, false);
		} else {
			yes = getButtonText(dialog, false);
			no = getButtonText(dialog, true);
		}
		dialog.setVisible(false);
		
		optionPane = new JOptionPane(null, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		dialog = optionPane.createDialog(null, "Input Dialog");
		dialog.setModal(false);
		dialog.setVisible(true);
		if (isWindowsOrLinux) {
			ok = getButtonText(dialog, true);
			cancel = getButtonText(dialog, false);
		} else {
			ok = getButtonText(dialog, false);
			cancel = getButtonText(dialog, true);
		}
		dialog.setVisible(false);
		
		if (yes == null || no == null || ok == null || cancel == null)
			throw new RuntimeException("localization of yes, no, ok and cancel failed");
	}
	
	private String getButtonText(Container container, boolean returnFirstButton) {
		boolean returnNextFoundButton = returnFirstButton;
		for (Component c : container.getComponents()) {
			if (c instanceof JButton) {
				if (returnNextFoundButton)
					return ((JButton)c).getText();
				returnNextFoundButton = true;
			}
			if (c instanceof Container) {
				String text = getButtonText((Container)c, returnNextFoundButton);
				if (text != null)
					return text;
			}
		}
		return null;
	}

	public static LocalizeYesNo getInstance() {
		synchronized(lock) {
			if (localizeYesNo == null)
				localizeYesNo = new LocalizeYesNo();
		}
		return localizeYesNo;
	}

	public String getYes() {
		return yes;
	}

	public String getNo() {
		return no;
	}

	public String getOK() {
		return ok;
	}

	public String getCancel() {
		return cancel;
	}

}
