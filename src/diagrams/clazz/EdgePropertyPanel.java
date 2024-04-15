package diagrams.clazz;

import diagrams.clazz.graph.edge.*;
import graph.*;
import res.R;
import utils.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.util.List;

public class EdgePropertyPanel
        extends JPanel {

    private static final Icon[] icons = {
            R.loadImage("association.png"),
            R.loadImage("dir_association.png"),
            R.loadImage("inheritance.png"),
            R.loadImage("implementation.png"),
            R.loadImage("dependency.png"),
            R.loadImage("aggregation.png"),
            R.loadImage("composition.png"),
    };

    private static final List<EdgeStyle> styles = List.of(
            new PlainLine(),
            new OpenArrow(new PlainLine()),
            new OutlineArrow(new PlainLine()),
            new OutlineArrow(new DashedLine()),
            new OpenArrow(new DashedLine()),
            new OutlineDiamond(new PlainLine()),
            new FilledDiamond(new PlainLine())
    );

    private final Connection connection;

    private final DiagramManager manager;

    private final JTextField centerInput = new JTextField(20);
    private final JTextField leftInput = new JTextField(20);
    private final JTextField rightInput = new JTextField(20);


    public EdgePropertyPanel(Connection connection, DiagramManager manager) {
        this.connection = connection;
        this.manager = manager;

        setLayout(new BorderLayout());

        JComboBox<Icon> comboBox = new JComboBox<>(icons);
        comboBox.setRenderer(new ComboBoxRenderer());
        comboBox.setBackground(Color.WHITE);
        comboBox.addActionListener(e -> {
            connection.setStyle(styles.get(comboBox.getSelectedIndex()));
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner();
        });

        int index = styles.indexOf(connection.getStyle());
        comboBox.setSelectedIndex(Math.max(0, index));

        add(comboBox, BorderLayout.NORTH);

        centerInput.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override public void insertUpdate(DocumentEvent e) {
                connection.setCenterText(centerInput.getText());
            }
            @Override public void removeUpdate(DocumentEvent e) {
                connection.setCenterText(centerInput.getText());
            }
        });
        centerInput.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        centerInput.setText(connection.getCenterText());

        leftInput.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override public void insertUpdate(DocumentEvent e) {
                connection.setLeftText(leftInput.getText());
            }
            @Override public void removeUpdate(DocumentEvent e) {
                connection.setLeftText(leftInput.getText());
            }
        });
        leftInput.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        leftInput.setText(connection.getLeftText());

        rightInput.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override public void insertUpdate(DocumentEvent e) {
                connection.setRightText(rightInput.getText());
            }
            @Override public void removeUpdate(DocumentEvent e) {
                connection.setRightText(rightInput.getText());
            }
        });
        rightInput.addActionListener(e -> KeyboardFocusManager.getCurrentKeyboardFocusManager().clearFocusOwner());
        rightInput.setText(connection.getRightText());

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(new JLabel("center text: "));
        center.add(centerInput);
        center.add(new JLabel("left text: "));
        center.add(leftInput);
        center.add(new JLabel("right text: "));
        center.add(rightInput);

        center.setMaximumSize(new Dimension(centerInput.getMaximumSize().width, 100));
        add(center, BorderLayout.CENTER);

        JButton changeDirectionButton = new JButton("change direction");
        changeDirectionButton.addActionListener(e -> connection.changeDirection());
        add(changeDirectionButton, BorderLayout.SOUTH);
    }



    private static class ComboBoxRenderer extends JLabel
            implements ListCellRenderer<Icon> {

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        public Component getListCellRendererComponent(
                JList list,
                Icon value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            setBackground(Color.WHITE);
            setForeground(Color.WHITE);

            setIcon(value);
            if (value != null) {
                setFont(list.getFont());
            }
            else {
                setText("source not found");
            }

            return this;
        }
    }


}
