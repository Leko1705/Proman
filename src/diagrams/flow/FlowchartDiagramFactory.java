package diagrams.flow;

import context.Context;
import diagram.Diagram;
import diagram.DiagramFactory;
import diagram.Diagrams;

import java.nio.file.Path;

public class FlowchartDiagramFactory implements DiagramFactory {

    @Override
    public String getDiagramKey() {
        return Diagrams.KEY_FLOW_CHART;
    }

    @Override
    public Diagram<?> createEmptyDiagram(Context<?, ?> context, int version) {
        return new FlowchartDiagram(context);
    }

    @Override
    public String getName() {
        return "Flow Chart";
    }

    @Override
    public Path getIconPath() {
        return Path.of("images/flowchart_diagram_icon.png");
    }
}
