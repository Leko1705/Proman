package diagrams.clazz;

import context.Context;
import diagram.Diagram;
import diagram.DiagramFactory;
import diagram.Diagrams;

import java.nio.file.Path;

public class UMLClassDiagramFactory implements DiagramFactory {

    @Override
    public String getDiagramKey() {
        return Diagrams.KEY_UML_CLASS_DIAGRAM;
    }

    @Override
    public Diagram<?> createEmptyDiagram(Context<?, ?> context, int version) {
        return new ClassDiagram(context);
    }

    @Override
    public String getName() {
        return "UML Class Diagram";
    }

    @Override
    public Path getIconPath() {
        return Path.of("images/class_diagram_icon.png");
    }
}
