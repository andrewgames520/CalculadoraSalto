import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SaltoAnimal extends JFrame implements ActionListener {
    // Constante da gravidade (m/s²)
    private static final double GRAVITY = 9.8;

    // Componentes da interface
    private JTextField alturaInput, tempoInput;
    private JTextField v0Output, vyOutput;
    private JButton calcularBtn, resetBtn;
    private GraficoPanel painelGrafico;

    // Construtor da interface
    public SaltoAnimal() {
        setTitle("Salto do Animal - Cálculo e Gráfico");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Label e input da altura máxima
        JLabel alturaLabel = new JLabel("Altura Máxima (m) [0 < Ymax < 10]:");
        alturaLabel.setBounds(30, 20, 220, 25);
        add(alturaLabel);

        alturaInput = new JTextField();
        alturaInput.setBounds(260, 20, 100, 25);
        add(alturaInput);

        // Label e input do tempo total (ida e volta)
        JLabel tempoLabel = new JLabel("Tempo Total do Salto (s) [0 < Tmax < 10]:");
        tempoLabel.setBounds(30, 60, 220, 25);
        add(tempoLabel);

        tempoInput = new JTextField();
        tempoInput.setBounds(260, 60, 100, 25);
        add(tempoInput);

        // Botão calcular
        calcularBtn = new JButton("Calcular");
        calcularBtn.setBounds(400, 20, 120, 30);
        calcularBtn.addActionListener(this);
        add(calcularBtn);

        // Botão reset
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(400, 60, 120, 30);
        resetBtn.addActionListener(this);
        add(resetBtn);

        // Saída da velocidade inicial
        JLabel v0Label = new JLabel("Velocidade Inicial (v₀) [m/s]:");
        v0Label.setBounds(30, 110, 200, 25);
        add(v0Label);

        v0Output = new JTextField();
        v0Output.setBounds(230, 110, 150, 25);
        v0Output.setEditable(false);
        add(v0Output);

        // Saída da Vy no topo
        JLabel vyLabel = new JLabel("Velocidade ao atingir Ymax (Vy) [m/s]:");
        vyLabel.setBounds(30, 150, 250, 25);
        add(vyLabel);

        vyOutput = new JTextField("0.0");
        vyOutput.setBounds(280, 150, 100, 25);
        vyOutput.setEditable(false);
        add(vyOutput);

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
                double Ymax = Double.parseDouble(alturaInput.getText());
                double Tmax = Double.parseDouble(tempoInput.getText());

                // Restrições dos inputs
                if (Ymax <= 0 || Ymax >= 10) {
                    mostrarErro("A altura máxima deve ser maior que 0 e menor que 10 metros.");
                    return;
                }
                if (Tmax <= 0 || Tmax >= 10) {
                    mostrarErro("O tempo total deve ser maior que 0 e menor que 10 segundos.");
                    return;
                }

                // Cálculo da velocidade inicial:
                // Tmax = tempo total de ida e volta => tempo até o topo = Tmax / 2
                // v0 = g * (Tmax/2)
                double v0 = GRAVITY * (Tmax / 2);

                // Vy no topo: sempre 0 (velocidade vertical anula na altura máxima)
                double vy = 0.0;

                // Exibir resultados
                v0Output.setText(String.format("%.2f", v0));
                vyOutput.setText(String.format("%.2f", vy));

                // Gerar novo gráfico
                painelGrafico.setParametros(Ymax, v0, Tmax);

            } catch (NumberFormatException ex) {
                mostrarErro("Digite valores numéricos válidos.");
            }
        }

        if (e.getSource() == resetBtn) {
            alturaInput.setText("");
            tempoInput.setText("");
            v0Output.setText("");
            vyOutput.setText("");
            painelGrafico.clear();
        }
    }

    // Método para erros
    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro de entrada", JOptionPane.ERROR_MESSAGE);
    }

    // Painel customizado para desenho do gráfico da altura vs tempo
    class GraficoPanel extends JPanel {
        private double Ymax = 0;
        private double v0 = 0;
        private double Tmax = 0;
        private boolean desenhar = false;

        // Define os parâmetros para o gráfico
        public void setParametros(double Ymax, double v0, double Tmax) {
            this.Ymax = Ymax;
            this.v0 = v0;
            this.Tmax = Tmax;
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
            double dt = Tmax / N;
            int prevX = margX, prevY = getHeight() - margY;

            for (int i = 0; i <= N; i++) {
                double t = i * dt;
                // y(t) = v0*t - 0.5*g*t^2
                double y = v0 * t - 0.5 * GRAVITY * t * t;

                // Normalizar para o painel
                int x = margX + (int) (t / Tmax * width);
                int yPanel = getHeight() - margY - (int) (y / Ymax * height);

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
