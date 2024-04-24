package diagrams.entityRel;

import context.Context;
import diagram.Diagram;
import diagram.DiagramFactory;
import diagram.Diagrams;

import java.nio.file.Path;

public class ERDiagramFactory implements DiagramFactory {

    @Override
    public String getDiagramKey() {
        return Diagrams.KEY_ENTITY_RELATIONSHIP_DIAGRAM;
    }
    @Override
    public Diagram<?> createEmptyDiagram(Context<?, ?> context, int version) {
        return new ERDiagram(context);
    }

    @Override
    public String getName() {
        return "Entity Relationship Diagram";
    }

    @Override
    public Path getIconPath() {
        return Path.of("images/ER_diagram_icon.png");
    }
}
