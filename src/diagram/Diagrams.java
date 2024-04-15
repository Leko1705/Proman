package diagram;

import diagrams.clazz.UMLClassDiagramFactory;
import diagrams.state.UMLStateDiagramFactory;

import java.util.HashMap;
import java.util.Map;

public final class Diagrams {
    private static final Map<String, DiagramFactory> supportedFactories = new HashMap<>();
    private static void addDiagramFactory(String key, DiagramFactory factory){
        supportedFactories.put(key, factory);
    }


    static {
        init();
    }



    public static final String KEY_UML_CLASS_DIAGRAM = "uml.class.diagram";
    public static final String KEY_UML_STATE_DIAGRAM = "uml.state.diagram";



    private static void init(){
        addDiagramFactory(KEY_UML_CLASS_DIAGRAM, new UMLClassDiagramFactory());
        addDiagramFactory(KEY_UML_STATE_DIAGRAM, new UMLStateDiagramFactory());
    }

    private Diagrams(){
    }

    public static DiagramFactory getDiagramFactory(String key){
        return supportedFactories.get(key);
    }

    public static DiagramFactory[] getDiagramFactories(){
        return supportedFactories.values().toArray(new DiagramFactory[0]);
    }

}