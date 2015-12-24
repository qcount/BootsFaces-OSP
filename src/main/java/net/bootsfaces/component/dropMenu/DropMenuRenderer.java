/**
 *  Copyright 2014-15 by Riccardo Massera (TheCoder4.Eu) and Stephan Rauh (http://www.beyondjava.net).
 *  
 *  This file is part of BootsFaces.
 *  
 *  BootsFaces is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  BootsFaces is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with BootsFaces. If not, see <http://www.gnu.org/licenses/>.
 */

package net.bootsfaces.component.dropMenu;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import net.bootsfaces.component.flyOutMenu.FlyOutMenu;
import net.bootsfaces.component.icon.IconRenderer;
import net.bootsfaces.render.A;
import net.bootsfaces.render.CoreRenderer;
import net.bootsfaces.render.Tooltip;

/** This class generates the HTML code of &lt;b:dropMenu /&gt;. */
@FacesRenderer(componentFamily = "net.bootsfaces.component", rendererType = "net.bootsfaces.component.dropMenu.DropMenu")
public class DropMenuRenderer extends CoreRenderer {

	/**
	 * This methods generates the HTML code of the current b:dropMenu.
	 * <code>encodeBegin</code> generates the start of the component. After the,
	 * the JSF framework calls <code>encodeChildren()</code> to generate the
	 * HTML code between the beginning and the end of the component. For
	 * instance, in the case of a panel component the content of the panel is
	 * generated by <code>encodeChildren()</code>. After that,
	 * <code>encodeEnd()</code> is called to generate the rest of the HTML code.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:dropMenu.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		DropMenu dropMenu = (DropMenu) component;
		ResponseWriter rw = context.getResponseWriter();
		String clientId = dropMenu.getClientId();

		boolean isFlyOutMenu = isFlyOutMenu(component);

		{
			rw.startElement("li", dropMenu);
			rw.writeAttribute("id", clientId, "id");
			rw.writeAttribute("class", getStyleClass(dropMenu, isFlyOutMenu), "class");
			if (dropMenu.getStyle() != null) {
				rw.writeAttribute("style", dropMenu.getStyle(), "style");
			}
			Tooltip.generateTooltip(context, dropMenu, rw);

			{
				rw.startElement("a", dropMenu);
				rw.writeAttribute("id", "dtL" + clientId, "id");
				if (isFlyOutMenu)
					rw.writeAttribute("class", "dropdown-submenu", "class");
				else
					rw.writeAttribute("class", "dropdown-toggle", "class");
				if (dropMenu.getStyle() != null) {
					rw.writeAttribute("style", dropMenu.getStyle(), "style");
				}
				if ("a".equals("button")) {
					rw.writeAttribute("type", "button", null);
				} else {
					rw.writeAttribute("href", "#", null);
				}
				rw.writeAttribute("role", "button", null);
				if (!isFlyOutMenu)
					rw.writeAttribute("data-toggle", "dropdown", null);

				// Encode value
				String value = (String) dropMenu.getAttributes().get("value");
				String icon = dropMenu.getIcon();
				String faicon = dropMenu.getIconAwesome();
				boolean fa = false; // flag to indicate wether the selected icon
									// set is
									// Font Awesome or not.
				if (faicon != null) {
					icon = faicon;
					fa = true;
				}
				if (icon != null) {
					Object ialign = dropMenu.getIconAlign();
					if (ialign != null && ialign.equals(A.RIGHT)) {
						rw.writeText(value + " ", null);
						IconRenderer.encodeIcon(rw, dropMenu, icon, fa);
					} else {
						IconRenderer.encodeIcon(rw, dropMenu, icon, fa);
						// !//R.encodeIcon(rw, this, icon, white);
						rw.writeText(" " + value, null);
					}
				} else {
					rw.writeText(value, null);
				}
				// Encode Caret
				if ((!isFlyOutMenu) && (!(dropMenu.getParent() instanceof DropMenu))) {
					rw.startElement("b", dropMenu);
					rw.writeAttribute("class", "caret", "class");
					rw.endElement("b");
				}
			}
			rw.endElement("a");

			encodeDropMenuStart(dropMenu, rw, "dtL" + clientId);
		}

	}

	private boolean isFlyOutMenu(UIComponent component) {
		while (component != null && (!(component instanceof UIViewRoot))) {
			if (component instanceof FlyOutMenu)
				return true;
			component = component.getParent();
		}
		return false;
	}

	private String getStyleClass(DropMenu dropMenu, boolean isFlyOutMenu) {
		String userClass = dropMenu.getStyleClass();
		if (null == userClass)
			userClass = "";
		else
			userClass += " ";
		String direction = dropMenu.getDrop();
		if (direction == null) {
			direction = "down";
		}
		if (!direction.equals("up") && !direction.equals("down")) {
			direction = "down";
		}
		if (isFlyOutMenu) {
			userClass += "dropdown-submenu" + " ";
			return userClass;
		}
		else if (dropMenu.getParent() instanceof DropMenu) {
			userClass += "dropdown-submenu" + " ";
		}
		return userClass + "drop" + direction;
	}

	/**
	 * Renders the Drop Menu.
	 * 
	 * @param c
	 * @param rw
	 * @param l
	 * @throws IOException
	 */
	public static void encodeDropMenuStart(DropMenu c, ResponseWriter rw, String l) throws IOException {
		rw.startElement("ul", c);
		if (c.getContentClass() != null)
			rw.writeAttribute("class", "dropdown-menu " + c.getContentClass(), "class");
		else
			rw.writeAttribute("class", "dropdown-menu", "class");
		if (null != c.getContentStyle())
			rw.writeAttribute("style", c.getContentStyle(), "style");
		rw.writeAttribute("role", "menu", null);
		rw.writeAttribute("aria-labelledby", l, null);
	}

	/**
	 * This methods generates the HTML code of the current b:dropMenu.
	 * <code>encodeBegin</code> generates the start of the component. After the,
	 * the JSF framework calls <code>encodeChildren()</code> to generate the
	 * HTML code between the beginning and the end of the component. For
	 * instance, in the case of a panel component the content of the panel is
	 * generated by <code>encodeChildren()</code>. After that,
	 * <code>encodeEnd()</code> is called to generate the rest of the HTML code.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:dropMenu.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		DropMenu dropMenu = (DropMenu) component;
		ResponseWriter rw = context.getResponseWriter();

		rw.endElement("ul");
		rw.endElement("li");

		Tooltip.activateTooltips(context, dropMenu);
	}

}
