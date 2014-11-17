package uk.co.revsys.jsont.camel;

import java.util.Map;
import org.apache.camel.Processor;
import uk.co.revsys.esb.component.MappedProcessorComponent;

public class JSONComponent extends MappedProcessorComponent {

    @Override
    protected void populateMappings(Map<String, Class<? extends Processor>> mappings) {
        mappings.put("path", JSONPathProcessor.class);
        mappings.put("transform", JSONTransformProcessor.class);
    }

    
}
