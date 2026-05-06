package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SaltoAnimal extends JFrame implements ActionListener {
    // Constante da gravidade (m/s²)
    private static final double GRAVITY = 9.8;

    // Componentes da interface
    private JTextField alturaInput, tempoInput;
    private JTextField alturaMaxOutput, tempoTotalOutput;
    private JButton calcularBtn, resetBtn, exemploBtn;
    private GraficoPanel painelGrafico;

    // Construtor da interface
    public SaltoAnimal() {
        setTitle("Salto do Animal - Cálculo e Gráfico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Label e input da altura no instante t
        JLabel alturaLabel = new JLabel("Altura no instante t (m) [0 < y < 10]:");
        alturaLabel.setBounds(30, 20, 240, 25);
        add(alturaLabel);

        alturaInput = new JTextField();
        alturaInput.setBounds(280, 20, 100, 25);
        add(alturaInput);

        // Label e input do tempo no instante t
        JLabel tempoLabel = new JLabel("Tempo no instante t (s) [0 < t < 10]:");
        tempoLabel.setBounds(30, 60, 240, 25);
        add(tempoLabel);

        tempoInput = new JTextField();
        tempoInput.setBounds(280, 60, 100, 25);
        add(tempoInput);

        // Botão calcular
        calcularBtn = new JButton("Calcular");
        calcularBtn.setBounds(420, 20, 120, 30);
        calcularBtn.addActionListener(this);
        add(calcularBtn);

        // Botão exemplo (exercício 44)
        exemploBtn = new JButton("Exemplo");
        exemploBtn.setBounds(420, 60, 120, 30);
        exemploBtn.addActionListener(this);
        add(exemploBtn);

        // Botão reset
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(550, 60, 120, 30);
        resetBtn.addActionListener(this);
        add(resetBtn);

        // Saída da altura máxima
        JLabel alturaMaxLabel = new JLabel("Altura máxima do salto (m):");
        alturaMaxLabel.setBounds(30, 110, 240, 25);
        add(alturaMaxLabel);

        alturaMaxOutput = new JTextField();
        alturaMaxOutput.setBounds(270, 110, 150, 25);
        alturaMaxOutput.setEditable(false);
        add(alturaMaxOutput);

        // Saída do tempo total
        JLabel tempoTotalLabel = new JLabel("Tempo total do salto (s):");
        tempoTotalLabel.setBounds(30, 150, 240, 25);
        add(tempoTotalLabel);

        tempoTotalOutput = new JTextField();
        tempoTotalOutput.setBounds(270, 150, 150, 25);
        tempoTotalOutput.setEditable(false);
        add(tempoTotalOutput);

        // Painel para o gráfico
        painelGrafico = new GraficoPanel();
        painelGrafico.setBounds(30, 200, 720, 330);
        painelGrafico.setBorder(BorderFactory.createTitledBorder("Altura x Tempo"));
        add(painelGrafico);

        setVisible(true);
    }

    // Ação dos botões
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calcularBtn) {
            try {
                double y = Double.parseDouble(alturaInput.getText());
                double t = Double.parseDouble(tempoInput.getText());
                calcularSalto(y, t);
            } catch (NumberFormatException ex) {
                mostrarErro("Digite valores numéricos válidos.");
            }
        }

        if (e.getSource() == exemploBtn) {
            // Valores do exercício 44: y = 0,544 m em t = 0,200 s
            double y = 0.544;
            double t = 0.200;
            alturaInput.setText(String.valueOf(y));
            tempoInput.setText(String.valueOf(t));
            calcularSalto(y, t);
        }

        if (e.getSource() == resetBtn) {
            alturaInput.setText("");
            tempoInput.setText("");
            alturaMaxOutput.setText("");
            tempoTotalOutput.setText("");
            painelGrafico.clear();
        }
    }

    private void calcularSalto(double y, double t) {
        // Restrições dos inputs
        if (y <= 0 || y >= 10) {
            mostrarErro("A altura deve ser maior que 0 e menor que 10 metros.");
            return;
        }
        if (t <= 0 || t >= 10) {
            mostrarErro("O tempo deve ser maior que 0 e menor que 10 segundos.");
            return;
        }

        // Cálculo da velocidade inicial usando y(t) = v0*t - 0.5*g*t^2
        double v0 = (y + 0.5 * GRAVITY * t * t) / t;
        if (v0 <= 0) {
            mostrarErro("Os valores informados não geram um salto válido.");
            return;
        }

        // Tempo até o topo do salto e altura máxima
        double tMax = v0 / GRAVITY;
        double yMax = (v0 * v0) / (2 * GRAVITY);

        // Tempo total de voo (sobe e desce)
        double tempoTotal = 2 * tMax;

        // Exibir resultados
        alturaMaxOutput.setText(String.format("%.3f", yMax));
        tempoTotalOutput.setText(String.format("%.3f", tempoTotal));

        // Gerar novo gráfico
        painelGrafico.setParametros(yMax, v0, tempoTotal);
    }

    // Método para erros
    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro de entrada", JOptionPane.ERROR_MESSAGE);
    }

    // Painel customizado para desenho do gráfico da altura vs tempo
    class GraficoPanel extends JPanel {
        private double yMax = 0;
        private double v0 = 0;
        private double tempoTotal = 0;
        private boolean desenhar = false;

        // Define os parâmetros para o gráfico
        public void setParametros(double yMax, double v0, double tempoTotal) {
            this.yMax = yMax;
            this.v0 = v0;
            this.tempoTotal = tempoTotal;
            this.desenhar = true;
            repaint();
        }

        public void clear() {
            desenhar = false;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Eixo X: tempo, Eixo Y: altura
            int margX = 50, margY = 30;
            int width = getWidth() - 2 * margX;
            int height = getHeight() - 2 * margY;

            // Desenha os eixos
            g.drawLine(margX, getHeight() - margY, margX + width, getHeight() - margY); // eixo X (tempo)
            g.drawLine(margX, getHeight() - margY, margX, margY); // eixo Y (altura)

            // Rótulos dos eixos
            g.drawString("Tempo (s)", margX + width / 2, getHeight() - 5);
            g.drawString("Altura (m)", 5, margY);

            if (!desenhar) return;

            // Plotar a trajetória altura vs tempo
            g.setColor(Color.RED);

            // Amostragem de pontos
            int N = 100; // quantidade de pontos
            double dt = tempoTotal / N;
            int prevX = margX, prevY = getHeight() - margY;

            for (int i = 0; i <= N; i++) {
                double t = i * dt;
                // y(t) = v0*t - 0.5*g*t^2
                double y = v0 * t - 0.5 * GRAVITY * t * t;

                // Normalizar para o painel
                int x = margX + (int) (t / tempoTotal * width);
                int yPanel = getHeight() - margY - (int) (y / yMax * height);

                // Não desenhar pontos fora
                if (y >= 0) {
                    g.fillOval(x - 2, yPanel - 2, 4, 4);
                    if (i > 0) {
                        g.drawLine(prevX, prevY, x, yPanel);
                    }
                    prevX = x;
                    prevY = yPanel;
                }
            }

            g.setColor(Color.BLACK);
        }
    }

    // Main para rodar o programa
    public static void main(String[] args) {
        // Tela usando o tema do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        new SaltoAnimal();
    }
}
