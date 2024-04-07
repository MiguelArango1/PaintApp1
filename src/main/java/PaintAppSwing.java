package com.example.paintapp1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class PaintAppSwing extends JFrame {

    private ArrayList<Shape> shapes = new ArrayList<>();
    private int currentSize =6 ;
    private Tool currentTool = Tool.LINEA; 
    private Point startPoint;

    public PaintAppSwing() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Paint Application");

        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel canvas = new JPanel() {
            
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                for (Shape shape : shapes) {
                    g2d.setColor(shape.color);
                    g2d.setStroke(new BasicStroke(shape.size));
                    switch (shape.type) {
                        case LINEA:
                            g2d.drawLine(shape.startPoint.x, shape.startPoint.y, shape.endPoint.x, shape.endPoint.y);
                            break;
                        case RECTANGULO:
                            g2d.drawRect(shape.startPoint.x, shape.startPoint.y, shape.width, shape.height);
                            break;
                        case CIRCULO:
                            g2d.drawOval(shape.startPoint.x, shape.startPoint.y, shape.width, shape.height);
                            break;
                    }
                }
            }
        };

        canvas.addMouseListener(new MouseAdapter() {
            
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            
            public void mouseReleased(MouseEvent e) {
                Point endPoint = e.getPoint();
                Shape shape = new Shape(startPoint, endPoint, currentSize, currentTool);
                shapes.add(shape);
                canvas.repaint();
            }
        });

        JButton clearButton = new JButton("BORRAR");
        clearButton.addActionListener(e -> {
            shapes.clear();
            canvas.repaint();
        });

        JButton saveButton = new JButton("GUARDAR");
        saveButton.addActionListener(e -> saveToFile());

        JButton loadButton = new JButton("CARGAR");
        loadButton.addActionListener(e -> {
            loadFromFile();
            canvas.repaint();
        });

        JComboBox<Tool> toolComboBox = new JComboBox<>(Tool.values());
        toolComboBox.addActionListener(e -> currentTool = (Tool) toolComboBox.getSelectedItem());

        JPanel topPanel = new JPanel();
        topPanel.add(clearButton);
        topPanel.add(saveButton);
        topPanel.add(loadButton);
        topPanel.add(new JLabel("SELECCIONAR:"));
        topPanel.add(toolComboBox);

        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(canvas, BorderLayout.CENTER);
    }

    private void saveToFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                oos.writeObject(shapes);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                shapes = (ArrayList<Shape>) ois.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaintAppSwing().setVisible(true));
    }

    static class Shape implements Serializable {
        Point startPoint;
        Point endPoint;
        Color color;
        int size;
        Tool type;
        int width;
        int height;

        public Shape(Point startPoint, Point endPoint, int size, Tool type) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.size = size;
            this.type = type;
            this.width = Math.abs(endPoint.x - startPoint.x);
            this.height = Math.abs(endPoint.y - startPoint.y);
        }
    }

    enum Tool {
        LINEA,
        RECTANGULO,
        CIRCULO
    }
}
