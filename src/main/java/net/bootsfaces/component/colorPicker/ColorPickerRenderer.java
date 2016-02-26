package net.bootsfaces.component.colorPicker;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;

import net.bootsfaces.C;
import net.bootsfaces.component.ajax.AJAXRenderer;
import net.bootsfaces.component.icon.Icon;
import net.bootsfaces.render.CoreRenderer;
import net.bootsfaces.render.H;
import net.bootsfaces.render.Tooltip;
import net.bootsfaces.utils.BsfUtils;

@FacesRenderer(componentFamily = C.BSFCOMPONENT, rendererType = "net.bootsfaces.component.ColorPickerRenderer")
public class ColorPickerRenderer extends CoreRenderer {

	@Override
	public void decode(FacesContext context, UIComponent component) {
		ColorPicker colorPicker = (ColorPicker) component;

		if (colorPicker.isDisabled() || colorPicker.isReadonly()) {
			return;
		}

		decodeBehaviors(context, colorPicker);

		String clientId = colorPicker.getClientId(context);
		String name = clientId;
		String submittedValue = (String) context.getExternalContext().getRequestParameterMap().get(name);

		if (submittedValue != null) {
			colorPicker.setSubmittedValue(submittedValue);
		}
		new AJAXRenderer().decode(context, component, name);
	}

	/**
	 * This method is called by the JSF framework to get the type-safe value of
	 * the attribute. Do not delete this method.
	 */
	@Override
	public Object getConvertedValue(FacesContext fc, UIComponent c, Object sval) throws ConverterException {
		Converter cnv = resolveConverter(fc, c);

		if (cnv != null) {
			return cnv.getAsObject(fc, c, (String) sval);
		} else {
			return sval;
		}
	}

	protected Converter resolveConverter(FacesContext context, UIComponent c) {
		if (!(c instanceof ValueHolder)) {
			return null;
		}

		Converter cnv = ((ValueHolder) c).getConverter();

		if (cnv != null) {
			return cnv;
		} else {
			ValueExpression ve = c.getValueExpression("value");

			if (ve != null) {
				Class<?> valType = ve.getType(context.getELContext());

				if (valType != null) {
					return context.getApplication().createConverter(valType);
				}
			}

			return null;
		}
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		ColorPicker colorPicker = (ColorPicker) component;

		ResponseWriter rw = context.getResponseWriter();
		String clientId = colorPicker.getClientId();

		int span = colorPicker.getSpan();
		if (span > 0) {
			rw.startElement("div", component);
			rw.writeAttribute("class", "col-md-" + span, "class");
		}

		// "Prepend" facet
		UIComponent prep = colorPicker.getFacet("prepend");
		// "Append" facet
		UIComponent app = colorPicker.getFacet("append");
		boolean prepend = (prep != null);
		boolean append = (app != null);

		String label = colorPicker.getLabel();
		{
			if (!colorPicker.isRenderLabel()) {
				label = null;
			}
		}

		// Define TYPE ( if null set default = text )
		// support for b:inputSecret
		String t =  colorPicker.getType();
		if (t == null)
			t = "text";

		rw.startElement("div", component);
		if (null != colorPicker.getDir()) {
			rw.writeAttribute("dir", colorPicker.getDir(), "dir");
		}

		Tooltip.generateTooltip(context, colorPicker, rw);
		rw.writeAttribute("id", clientId, "id");
		if (colorPicker.isInline()) {
			rw.writeAttribute("class", "form-inline", "class");

		} else {
			rw.writeAttribute("class", "form-group", "class");
		}

		if (label != null) {
			rw.startElement("label", component);
			rw.writeAttribute("for", "input_" + clientId, "for");
			generateErrorAndRequiredClass(colorPicker, rw, clientId);

			rw.writeText(label, null);
			rw.endElement("label");
		}

		if (append || prepend) {
			rw.startElement("div", component);
			rw.writeAttribute("class", "input-group", "class");
		}

		if (prepend) {
			if (prep.getClass().getName().endsWith("Button") || (prep.getChildCount() > 0
					&& prep.getChildren().get(0).getClass().getName().endsWith("Button"))) {
				rw.startElement("div", colorPicker);
				rw.writeAttribute("class", "input-group-btn", "class");
				prep.encodeAll(context);
				rw.endElement("div");
			} else {
				if (prep instanceof Icon)
					((Icon) prep).setAddon(true); // modifies the id of the icon
				rw.startElement("span", colorPicker);
				rw.writeAttribute("class", "input-group-addon", "class");
				prep.encodeAll(context);
				rw.endElement("span");
			}
		}

		// Input
		rw.startElement("input", colorPicker);
		rw.writeAttribute("id", "input_" + clientId, null);
		rw.writeAttribute("name", clientId, null);
		rw.writeAttribute("type", t, null);

		generateStyleClass(colorPicker, rw);

		String ph = colorPicker.getPlaceholder();
		if (ph != null) {
			rw.writeAttribute("placeholder", ph, null);
		}

		if (colorPicker.isDisabled()) {
			rw.writeAttribute("disabled", "disabled", null);
		}
		if (colorPicker.isReadonly()) {
			rw.writeAttribute("readonly", "readonly", null);
		}

		// Encode attributes (HTML 4 pass-through + DHTML)
		renderPassThruAttributes(context, component, H.INPUT_TEXT);

		String autocomplete = colorPicker.getAutocomplete();
		if ((autocomplete != null) && (autocomplete.equals("off"))) {
			rw.writeAttribute("autocomplete", "off", null);
		}

		String v = getValue2Render(context, component);
		rw.writeAttribute("value", v, null);

		// Render Ajax Capabilities
		AJAXRenderer.generateBootsFacesAJAXAndJavaScript(FacesContext.getCurrentInstance(), colorPicker, rw);

		rw.endElement("input");
		if (append) {
			if (app.getClass().getName().endsWith("Button")
					|| (app.getChildCount() > 0 && app.getChildren().get(0).getClass().getName().endsWith("Button"))) {
				rw.startElement("div", colorPicker);
				rw.writeAttribute("class", "input-group-btn", "class");
				app.encodeAll(context);
				rw.endElement("div");
			} else {
				if (app instanceof Icon)
					((Icon) app).setAddon(true);
				rw.startElement("span", colorPicker);
				rw.writeAttribute("class", "input-group-addon", "class");
				app.encodeAll(context);
				rw.endElement("span");
			}
		}

		if (append || prepend) {
			rw.endElement("div");
		} // input-group
		rw.endElement("div"); // form-group
		if (span > 0) {
			rw.endElement("div"); // span
		}

		Tooltip.activateTooltips(context, colorPicker);
		
		// build color picker init script
		rw.startElement("script", colorPicker);
		
		rw.writeText("$(function() {" +
					"$('#input_" + BsfUtils.EscapeJQuerySpecialCharsInSelector(clientId) + "').minicolors({" +
					(colorPicker.getAttributes().get("control") != null ? " control: '" + colorPicker.getAttributes().get("control")  + "'," : "")  +
					(colorPicker.getAttributes().get("format") != null ? " format: '" + colorPicker.getAttributes().get("format")  + "'," : "")  +
					(colorPicker.getAttributes().get("opacity") != null ? " opacity: " + colorPicker.getAttributes().get("opacity")  + "," : "")  +
					(colorPicker.getAttributes().get("position") != null ? " position: '" + colorPicker.getAttributes().get("position")  + "'," : "")  +
					" theme: 'bootstrap' " +
					"});" +
					"});", null);
		rw.endElement("script");
	}

	private void generateStyleClass(ColorPicker colorPicker, ResponseWriter rw) throws IOException {
		StringBuilder sb;
		String s;
		sb = new StringBuilder(20); // optimize int
		sb.append("form-control");

		String fsize = colorPicker.getFieldSize();

		if (fsize != null) {
			sb.append(" input-").append(fsize);
		}

		// styleClass and class support
		String sclass = colorPicker.getStyleClass();
		if (sclass != null) {
			sb.append(" ").append(sclass);
		}

		sb.append(" ").append(getErrorAndRequiredClass(colorPicker, colorPicker.getClientId()));
		s = sb.toString().trim();
		if (s != null && s.length() > 0) {
			rw.writeAttribute("class", s, "class");
		}
	}
}
