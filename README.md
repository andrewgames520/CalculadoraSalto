# 📌Simulador de aceleração em queda
---
Este projeto simula a análise do movimento vertical de um animal em salto, modelado como um caso de movimento uniformemente variado sob a ação da gravidade.
A partir de dados iniciais como deslocamento em um intervalo de tempo e aceleração gravitacional, o problema permite determinar características importantes do movimento.   

Considerando que um tatu salta verticalmente para cima e atinge uma altura de 0,544 m nos primeiros 0,200 s, o estudo propõe o cálculo de:

- Velocidade inicial de lançamento do animal ao deixar o solo (v0), utilizando as equações do movimento uniformemente variado.
- Velocidade do animal ao atingir a altura desejada (Vy).

- Altura máxima alcançada durante o salto (Ymax).

- Tempo em que a altura máxima é atingida (Tmax).
---
## 🧮Comportamento

- O programa faz os cálculos a partir de uma dada Altura e tempo, porém ambos os parâmetros são limitados a fim de manter a fidelidade com a realidade.
- Os cálculos são feitos utilizando fórmulas de conceitos físicos.
- Os resultados são apresentados em números.
- Um gráfico é apresentado demonstrando a relação da altura e do tempo.
- Animação do salto.
- Destaque do ponto de altura máxima.
---
## 🖥️Tecnologias Utilizadas    

- Linguagem: Java 8  
- Interface gráfica: Java Swing  
- Renderização gráfica: Java AWT (Graphics / Graphics2D)  
- Animação: javax.swing.Timer
- Eclipse IDE
---
## 📝Exercício:  

Fundamentos de Física
Capítulo 2

Um tatu assustado pula verticalmente para cima, subindo 0,544 m nos primeiros 0,200 s.

y = 0,544 m t = 0,200 s g = 9,8 m/s²

(a) Qual é a velocidade do animal ao deixar o solo?
y = v0·t - (1/2)·g·t²

0,544 = v0·0,200 - (1/2)·9,8·(0,200)²~ 0,544 = 0,200v0 - 4,9·0,04  
0,544 = 0,200v0 - 0,196  
0,544 + 0,196 = 0,200v0  
0,740 = 0,200v0  
v0 = 0,740 / 0,200  
v0 = 3,70 m/s  

(b) Qual é a velocidade na altura de 0,544 m?  
v = v0 - g·t

v = 3,70 - 9,8·0,200  
v = 3,70 - 1,96  
v = 1,74 m/s  

(c) Qual é a altura do salto?  
v² = v0² - 2·g·h  

0 = (3,70)² - 2·9,8·h  
0 = 13,69 - 19,6h  
19,6h = 13,69  
h = 13,69 / 19,6  
h = 0,699 m  

Bônus: tempo até a altura máxima  
t = v0 / g  

t = 3,70 / 9,8 t = 0,378 s  

---
Esboço da interface  
<img width="534" height="265" alt="ImagemInterface" src="https://github.com/user-attachments/assets/da39160a-a113-41da-a0d7-4903191b7d28" />


Esboço do Gráfico da interface  
<img width="744" height="305" alt="ImagemGrafico" src="https://github.com/user-attachments/assets/3bea321a-2a44-4cf2-aee6-e097096cba44" />


Esboço da guia completa  
<img width="763" height="597" alt="ImagemGuiaCompleto" src="https://github.com/user-attachments/assets/bd4c838f-4bdb-487d-88b3-2d6d056657b0" />

# 👨‍💻 Autor
Andrew Rodrigues
