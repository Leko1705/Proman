package diagrams.state;

import context.Context;
import diagram.Diagram;
import diagram.DiagramFactory;
import diagram.Diagrams;

import java.nio.file.Path;

public class UMLStateDiagramFactory implements DiagramFactory {

    @Override
    public String getDiagramKey() {
        return Diagrams.KEY_UML_STATE_DIAGRAM;
    }

    @Override
    public Diagram<?> createEmptyDiagram(Context<?, ?> context, int version) {
        return new StateDiagram(context);
    }

    @Override
    public String getName() {
        return "UML State Diagram";
    }

    @Override
    public Path getIconPath() {
        return Path.of("images/state_diagram_icon.png");
    }

}
