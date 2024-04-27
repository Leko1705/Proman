package diagrams.usecase;

import context.Context;
import diagram.Diagram;
import diagram.DiagramFactory;
import diagram.Diagrams;

import java.nio.file.Path;

public class UsecaseDiagramFactory implements DiagramFactory {

    @Override
    public String getDiagramKey() {
        return Diagrams.KEY_UML_USECASE_DIAGRAM;
    }

    @Override
    public Diagram<?> createEmptyDiagram(Context<?, ?> context, int version) {
        return new UsecaseDiagram(context);
    }

    @Override
    public String getName() {
        return "UML usecase diagram";
    }

    @Override
    public Path getIconPath() {
        return Path.of("images/usecase_diagram_icon.png");
    }
}
