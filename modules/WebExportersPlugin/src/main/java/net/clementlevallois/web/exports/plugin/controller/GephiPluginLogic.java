/*
 * author: Clément Levallois
 */
package net.clementlevallois.web.exports.plugin.controller;

/*
 *
 * @author LEVALLOIS
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;

/**
 * Example of an action accessible from the "Plugins" menu un the menubar.
 * <p>
 * The annotations on the class defines the menu's name, position and class.
 *
 * @author Mathieu Bastian
 */
@ActionID(category = "File",
        id = "org.gephi.desktop.filters.TestAction")
@ActionRegistration(displayName = "#CTL_Retina")
@ActionReferences({
    @ActionReference(path = "Menu/File/Publish to the web", position = 3333)
})
@Messages("CTL_Retina=with Retina")
public final class GephiPluginLogic implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanelWebExport jPanelWebExport = new JPanelWebExport();
        DialogDescriptor dd = new DialogDescriptor(jPanelWebExport, "Publish to the web");
        Object response = DialogDisplayer.getDefault().notify(dd);
        if (response != NotifyDescriptor.OK_OPTION) {
            return;
        }
    }
}
