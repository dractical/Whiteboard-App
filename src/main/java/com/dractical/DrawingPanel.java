package com.dractical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class DrawingPanel extends JPanel {
    private BufferedImage image;
    private Graphics2D g2;
    private Color currentColor = Color.BLACK;
    private int brushSize = 5;
    private int prevX, prevY;
    private final Stack<BufferedImage> undoStack = new Stack<>();
    private Color backgroundColor = Color.WHITE;

    public DrawingPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(backgroundColor);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (g2 == null) {
                    initDrawing();
                }
                saveToUndoStack();
                prevX = e.getX();
                prevY = e.getY();
                draw(e.getX(), e.getY());
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                drawLine(prevX, prevY, x, y);
                prevX = x;
                prevY = y;
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeCanvas();
            }
        });
    }

    private void initDrawing() {
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clearCanvas();
    }

    public void clearCanvas() {
        g2.setPaint(backgroundColor);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        repaint();
    }

    private void draw(int x, int y) {
        g2.setColor(currentColor);
        g2.fillOval(x - brushSize / 2, y - brushSize / 2, brushSize, brushSize);
        repaint();
    }

    private void drawLine(int x1, int y1, int x2, int y2) {
        g2.setColor(currentColor);
        g2.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(x1, y1, x2, y2);
        repaint();
    }

    private void resizeCanvas() {
        if (image == null) {
            return;
        }

        BufferedImage newImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, null);
        g.dispose();

        image = newImage;
        g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        repaint();
    }

    private void saveToUndoStack() {
        BufferedImage savedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = savedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        undoStack.push(savedImage);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            image = undoStack.pop();
            g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

    public void setCurrentColor(Color color) {
        this.currentColor = color;
    }

    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
        clearCanvas();
    }
}