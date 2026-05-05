package core;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class SaltoTatu implements ActionListener {

    // Variáveis globais
    JFrame frame;
    JButton playBtn, resetBtn;
    JTextField initSpeedInput, angleInput, tempoInput,
            finaltempoOutput, timeOutput, speedOutput;
    Double initSpeed, angle, tempo;
    String playertempoDescription, playerTimeDescription,
            playerSpeedDescription;

    // Construtor
    SaltoTatu() {

        // Cria a caixa da calculadora.
        frame = new JFrame("Calcula Altura pulo");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(430, 400);

        frame.setLayout(null);

        // Cria os títulos e input para as variáveis
        // Velocidade inicial
        JLabel initSpeedText = createLabel(
                "Velocidade inicial (m/s):", 40, 25, 160, 30);
        initSpeedInput = createTextField(
                "", 190, 25, 70, 30, true, true);

        // Angulo
        JLabel angleText = createLabel("Angulo (graus):", 40, 60, 160, 30);
        angleInput = createTextField("", 190, 60, 70, 30, true, true);

        // Distância
        JLabel tempoText = createLabel("Distância (m):", 40, 95, 160, 30);
        tempoInput = createTextField("", 190, 95, 70, 30, true, true);

        // Botão para calcular
        playBtn = new JButton("Calcular");
        playBtn.setBounds(290, 60, 100, 30);
        playBtn.addActionListener(this);
        playBtn.setFocusable(false);

        // Cria as variáveis de saída
        playertempoDescription = "Distância a percorrer (m): ";
        finaltempoOutput = createTextField(playertempoDescription, 40, 150, 350, 30, false, false);

        playerTimeDescription = "Tempo de corrida (s): ";
        timeOutput = createTextField(playerTimeDescription, 40, 190, 350, 30, false, false);

        playerSpeedDescription = "Velocidade média do jogador (m/s): ";
        speedOutput = createTextField(playerSpeedDescription, 40, 230, 350, 30, false, false);

        // Botão para resetar
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(190, 290, 80, 30);
        resetBtn.addActionListener(this);
        resetBtn.setFocusable(false);

        // Adicionar elementos ao frame para mostrar na tela
        frame.add(initSpeedText);
        frame.add(initSpeedInput);
        frame.add(angleText);
        frame.add(angleInput);
        frame.add(tempoText);
        frame.add(tempoInput);
        frame.add(finaltempoOutput);
        frame.add(timeOutput);
        frame.add(speedOutput);
        frame.add(playBtn);
        frame.add(resetBtn);
        frame.setVisible(true);

    }

    // Função Principal que roda o programa
    public static void main(String[] args) {
        new SaltoTatu();
    }

    // Executa a função do botão "Calcular"
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playBtn) {

            try {
                // Transforma valores do tipo string para double
                initSpeed = Double.parseDouble(initSpeedInput.getText());
                angle = Double.parseDouble(angleInput.getText());
                tempo = Double.parseDouble(tempoInput.getText());

                // **** Restrições das variáveis ****
                if (initSpeed < 0) {
                    // Altura 
                    errorMsg("A altura deve ser positiva.", "Erro de entrada");
                    return;
                }
                if (initSpeed > 10) {
                    // altura não pode ser maior que 10, pois é impossivel algo chegar tão alto em um salto
                    errorMsg("A altura deve ser menor que 10m.", "Erro de entrada");
                    return;
                }

           
                if (Math.abs(tempo) > 125) {
                    errorMsg("A maior distância em módulo que os jogadores podem ter é 125m.", "Erro de Entrada");
                    return;
                }

                // Calcula o tempo que o jogador precisa para chagar até a bola
                Double time = calculateTimeToReachTheBall(initSpeed, angle);
                String playerTime = createVetorString(playerTimeDescription, time);
                timeOutput.setText(playerTime);

                // Calcula a distancia que o jogador deve correr para alcançar a bola antes de
                // tocar no chão
                Double playertempo = calculatetempoalturamax(initSpeed, angle, time);
                String playertempoString = createVetorString(playertempoDescription, playertempo);
                finaltempoOutput.setText(playertempoString);

                // Calcula a velocidade média do jogador
                Double speed = playertempo / time;
                String speedText = createVetorString(playerSpeedDescription, speed);
                speedOutput.setText(speedText);

                // Limite de velocidade de um jogador em módulo.
                if (Math.abs(speed) > 11) {
                    errorMsg("Um jogador pode correr até 11m/s (~40Km/h).", "Impossível!");
                }

            } catch (NumberFormatException ex) {
                // Se o valor digitado não for um número, mostra uma mensagem de erro.
                errorMsg("Por favor, digite um número válido.", "Erro de entrada");
            }
        }

        // Executa a função do botão "Reset"
        if (e.getSource() == resetBtn) {

            // Limpar as variáveis
            initSpeed = null;
            angle = null;
            tempo = null;

            // Limpar as caixas de texto
            initSpeedInput.setText("");
            tempoInput.setText("");

            timeOutput.setText("");
            finaltempoOutput.setText("");
            speedOutput.setText("");
        }
    }

    // Método: Cria as caixas de input
    private JTextField createTextField(String label, int x, int y, int w, int h, boolean edit, boolean focus) {
        JTextField textField = new JTextField(label);
        textField.setBounds(x, y, w, h);
        textField.setEditable(edit);
        textField.setFocusable(focus);

        return textField;
    }

    // Método: Cria as Labels(titulos)
    private JLabel createLabel(String label, int x, int y, int w, int h) {
        JLabel labelText = new JLabel(label);
        labelText.setBounds(x, y, w, h);

        return labelText;
    }

    // Método: Cria a frase de resposta a partir de uma descrição e um número
    private String createVetorString(String description, Double x) {
        String string = description + String.format("%.1f", x);
        return string;
    }

    // Método: Calcula o tempo que a bola demora para chegar ao solo, que é o mesmo
    // tempo que o jogador tem para chegar até a bola antes de tocar o solo.
    private Double calculateTimeToReachTheBall(Double v0, Double teta) {
        double time = 2 * v0 * Math.sin(Math.toRadians(teta)) / 9.8;
        return time;
    }

    // Método: Calcula o tempo em que o animal estara na maior alturar
    private Double calculatetempoaltmax(Double v0, Double teta, Double t) {
        double ballFinalPos = v0 * Math.cos(Math.toRadians(teta)) * t;
        double playertempo = ballFinalPos - tempo;

        return playertempo;
    }

    // Método: Mostra a mensagem de erro.
    private void errorMsg(String msg, String title) {
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE);
    }
}