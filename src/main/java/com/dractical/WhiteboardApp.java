package com.dractical;

import javax.swing.*;
import java.awt.*;

public class WhiteboardApp extends JFrame {
    private final DrawingPanel canvas;
    private Color currentColor = Color.BLACK;

    public WhiteboardApp() {
        setTitle("Whiteboard App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new DrawingPanel();
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> canvas.clearCanvas());

        JButton colorButton = new JButton("Color");
        colorButton.addActionListener(e -> {
            currentColor = JColorChooser.showDialog(null, "Choose a color", currentColor);
            canvas.setCurrentColor(currentColor);
        });

        JButton eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(e -> canvas.setCurrentColor(canvas.getBackgroundColor()));

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> canvas.undo());

        JButton backgroundColorButton = new JButton("Background Color");
        backgroundColorButton.addActionListener(e -> {
            Color bgColor = JColorChooser.showDialog(null, "Choose background color", canvas.getBackgroundColor());
            if (bgColor != null) {
                canvas.setBackgroundColor(bgColor);
            }
        });

        int brushSize = 5;
        JSlider brushSizeSlider = new JSlider(1, 20, brushSize);
        brushSizeSlider.addChangeListener(e -> canvas.setBrushSize(brushSizeSlider.getValue()));
        JPanel controlPanel = new JPanel();
        controlPanel.add(clearButton);
        controlPanel.add(colorButton);
        controlPanel.add(eraserButton);
        controlPanel.add(undoButton);
        controlPanel.add(backgroundColorButton);
        controlPanel.add(new JLabel("Brush Size:"));
        controlPanel.add(brushSizeSlider);
        add(canvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WhiteboardApp::new);
    }
}