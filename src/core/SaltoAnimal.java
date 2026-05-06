package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SaltoAnimal extends JFrame implements ActionListener {

    public static final double GRAVITY = 9.8;

    private JTextField alturaInput, tempoInput;
    private JTextField v0Output, vTempoOutput, alturaMaxOutput, tempoMaxOutput;
    private JButton calcularBtn, resetBtn, exemploBtn;
    private GraficoPanel painelGrafico;

    public SaltoAnimal() {
        setTitle("Simulador de Salto Vertical");
        setSize(800, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel alturaLabel = new JLabel("Altura no instante t (m):");
        alturaLabel.setBounds(30, 20, 250, 25);
        add(alturaLabel);

        alturaInput = new JTextField();
        alturaInput.setBounds(260, 20, 100, 25);
        add(alturaInput);

        JLabel tempoLabel = new JLabel("Tempo no instante t (s):");
        tempoLabel.setBounds(30, 60, 250, 25);
        add(tempoLabel);

        tempoInput = new JTextField();
        tempoInput.setBounds(260, 60, 100, 25);
        add(tempoInput);

        calcularBtn = new JButton("Calcular");
        calcularBtn.setBounds(420, 20, 120, 30);
        calcularBtn.addActionListener(this);
        add(calcularBtn);

        exemploBtn = new JButton("Exemplo");
        exemploBtn.setBounds(420, 60, 120, 30);
        exemploBtn.addActionListener(this);
        add(exemploBtn);

        resetBtn = new JButton("Reset");
        resetBtn.setBounds(420, 100, 120, 30);
        resetBtn.addActionListener(this);
        add(resetBtn);

        JLabel v0Label = new JLabel("Velocidade inicial (m/s):");
        v0Label.setBounds(30, 110, 240, 25);
        add(v0Label);

        v0Output = new JTextField();
        v0Output.setBounds(260, 110, 150, 25);
        v0Output.setEditable(false);
        add(v0Output);

        JLabel vTempoLabel = new JLabel("Velocidade instantânea (m/s):");
        vTempoLabel.setBounds(30, 150, 240, 25);
        add(vTempoLabel);

        vTempoOutput = new JTextField();
        vTempoOutput.setBounds(260, 150, 150, 25);
        vTempoOutput.setEditable(false);
        add(vTempoOutput);

        JLabel alturaMaxLabel = new JLabel("Altura máxima (m):");
        alturaMaxLabel.setBounds(30, 190, 240, 25);
        add(alturaMaxLabel);

        alturaMaxOutput = new JTextField();
        alturaMaxOutput.setBounds(260, 190, 150, 25);
        alturaMaxOutput.setEditable(false);
        add(alturaMaxOutput);

        JLabel tempoMaxLabel = new JLabel("Tempo no topo (s):");
        tempoMaxLabel.setBounds(30, 230, 240, 25);
        add(tempoMaxLabel);

        tempoMaxOutput = new JTextField();
        tempoMaxOutput.setBounds(260, 230, 150, 25);
        tempoMaxOutput.setEditable(false);
        add(tempoMaxOutput);

        painelGrafico = new GraficoPanel();
        painelGrafico.setBounds(30, 270, 720, 300);
        painelGrafico.setBorder(BorderFactory.createTitledBorder("Altura x Tempo"));
        add(painelGrafico);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calcularBtn) {
            try {
                double y = Double.parseDouble(alturaInput.getText());
                double t = Double.parseDouble(tempoInput.getText());
                calcularSalto(y, t);
            } catch (Exception ex) {
                mostrarErro("Valores inválidos.");
            }
        }

        if (e.getSource() == exemploBtn) {
            double y = 0.544;
            double t = 0.200;
            alturaInput.setText("0.544");
            tempoInput.setText("0.200");
            calcularSalto(y, t);
        }

        if (e.getSource() == resetBtn) {
            alturaInput.setText("");
            tempoInput.setText("");
            v0Output.setText("");
            vTempoOutput.setText("");
            alturaMaxOutput.setText("");
            tempoMaxOutput.setText("");
            painelGrafico.reset();
        }
    }

    private void calcularSalto(double y, double t) {

        if (y <= 0 || t <= 0) {
            mostrarErro("Valores devem ser positivos.");
            return;
        }

        double v0 = (y + 0.5 * GRAVITY * t * t) / t;
        double tMax = v0 / GRAVITY;
        double yMax = (v0 * v0) / (2 * GRAVITY);

        v0Output.setText(String.format("%.3f", v0));
        alturaMaxOutput.setText(String.format("%.3f", yMax));
        tempoMaxOutput.setText(String.format("%.3f", tMax));

        painelGrafico.iniciarAnimacao(v0, 2 * tMax);
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    class GraficoPanel extends JPanel {

        private double v0, tempoTotal, yMax;
        private double tempoAtual;
        private Timer timer;

        public void iniciarAnimacao(double v0, double tempoTotal) {
            this.v0 = v0;
            this.tempoTotal = tempoTotal;
            this.yMax = (v0 * v0) / (2 * GRAVITY);
            this.tempoAtual = 0;

            if (timer != null) timer.stop();

            timer = new Timer(40, e -> {
                tempoAtual += 0.04;

                double v = v0 - GRAVITY * tempoAtual;
                vTempoOutput.setText(String.format("%.3f", v));

                repaint();

                double y = v0 * tempoAtual - 0.5 * GRAVITY * tempoAtual * tempoAtual;
                if (y < 0) timer.stop();
            });

            timer.start();
        }

        public void reset() {
            if (timer != null) timer.stop();
            tempoAtual = 0;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int margX = 50, margY = 30;
            int width = getWidth() - 2 * margX;
            int height = getHeight() - 2 * margY;

            g.drawLine(margX, getHeight() - margY, margX + width, getHeight() - margY);
            g.drawLine(margX, getHeight() - margY, margX, margY);

            if (yMax == 0) return;

            // Curva
            g.setColor(Color.LIGHT_GRAY);
            int N = 100;
            int prevX = margX;
            int prevY = getHeight() - margY;

            for (int i = 0; i <= N; i++) {
                double t = i * tempoTotal / N;
                double y = v0 * t - 0.5 * GRAVITY * t * t;

                int x = margX + (int)(t / tempoTotal * width);
                int yPanel = getHeight() - margY - (int)(y / yMax * height);

                if (i > 0) g.drawLine(prevX, prevY, x, yPanel);
                prevX = x;
                prevY = yPanel;
            }

            // Ponto animado
            double yAtual = v0 * tempoAtual - 0.5 * GRAVITY * tempoAtual * tempoAtual;

            if (yAtual >= 0) {
                int x = margX + (int)(tempoAtual / tempoTotal * width);
                int yPanel = getHeight() - margY - (int)(yAtual / yMax * height);

                g.setColor(Color.RED);
                g.fillOval(x - 6, yPanel - 6, 12, 12);
            }

            // Ponto máximo
            double tMax = v0 / GRAVITY;
            int xMax = margX + (int)(tMax / tempoTotal * width);
            int yMaxPanel = getHeight() - margY - height;

            g.setColor(Color.BLUE);
            g.fillOval(xMax - 5, yMaxPanel - 5, 10, 10);
        }
    }

    public static void main(String[] args) {
        new SaltoAnimal();
    }
}