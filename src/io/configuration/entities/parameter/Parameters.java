package io.configuration.entities.parameter;

import io.configuration.entities.parameter.interfaces.IParameter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import io.configuration.entities.receiver.interfaces.IReceiver;

import java.util.ArrayList;
import java.util.List;

import static io.configuration.tools.XmlTools.*;

public class Parameters {
    
    private Parameters(){
    }

    public static void setParameters(Element element, IReceiver receiver) {
        List<Node> parameters = getTagsElements(element, Parameter.PARAMETER);

        for (int i = 0; i < parameters.size(); i++) {
            Element paramElement = (Element) parameters.get(i);

            boolean status = getElementStatus(element);

            if(status){
                String paramName = getTagText(paramElement, Parameter.NAME);
                String paramOrder = getTagText(paramElement, Parameter.ORDER);
                String paramType = getTagText(paramElement, Parameter.TYPE);
                Object paramValue = buildParameterValue(paramElement);

                IParameter parameter = new Parameter();
                parameter.setName(paramName);
                parameter.setOrder(paramOrder);
                parameter.setType(paramType);
                parameter.setValue(paramValue);

                receiver.setParameter(parameter);
            }
        }
    }

    private static Object buildParameterValue(Element paramTag) {
        Element valuesTag = getTagChild(paramTag, Parameter.VALUES);
        if(valuesTag != null)
            return buildParameterArrayValue(valuesTag);
        return getTagText(paramTag, Parameter.VALUE);
    }

    private static Object buildParameterArrayValue(Element valuesTag) {
        List<Node> values = getTagsElements(valuesTag, Parameter.VALUE);
        ArrayList<String> valuesList = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            Element value = (Element) values.get(i);
            boolean status = getElementStatus(value);
            if (status)
                valuesList.add(value.getTextContent());
        }

        return valuesList.toArray(new String[0]);
    }

}