# 💧 Simulador de Hidrômetro em Java (Swing)

Eu, Andrey William, apresento aqui minha contribuição ao projeto de Simulador de Hidrômetro do aluno Osmar Santos, ambos matriculados na disciplina de Padrões de Projetos

---

## 🚀 Funcionalidades

- Execução de até 5 hidrômetros configuráveis através de arquivos .json rodando simultaneamente e de maneira independente.
- Simulação **ininterrupta** do fluxo de água.
- Exibição do display do hidrômetro com **volume acumulado (m³)** a partir de uma simulação através de funções matemáticas.
- Interface gráfica em **Swing**, estilo display digital.
- Atualização automática em tempos configuráveis nos arquivos .json.
- Encerramento apenas quando o usuário fecha a janela.

---

## 🛠️ Tecnologias utilizadas

- **Java 8+**
- **Swing** (GUI)
- **POO** (Programação Orientada a Objetos)
- **Gson** <Leitura de Arquivos .json>

---

## 📂 Estrutura do Código

- `HidrometroSwing.java` → Classe principal do hidrômetro com a execução da main e a inicialização dos principais métodos utilizados.
- `FluxoAgua.java` → Classe que simula o acúmulo de água.
- `DisplayPanel.java` → Classe responsável por desenhar o painel do hidrômetro que será exibido.
- `Config.java` → Classe que implementa os métodos de leitura dos arquivos .json.
- `HidrometroConfig.java` → Classe intermediária que irá receber os valores lidos pela Config para atribuir ao hidrômetro.

---
## 🛠️ Como configurar

1. Navegue até o diretório /hidrometro/config

2. Você irá encontrar 4 arquivos config.json respectivos a cada hidrômetro que pode ser executado (do 1 ao 5).

3. Nos arquivos estão os 3 valores que podem ser configurados:
    * diametroEntrada → Representa o diâmetro do cano de entrada de água que irá chegar ao hidrômetro;
    * velmediaFluxoAgua → Representa a velocidade média de fluxo de água (uma média baseada em valores reais para simular o volume acumulado);
    * tempoSimulacao → O intervalo de tempo, em segundos, desejado para que a simulação apresente novos valores.

---

## ▶️ Como executar

1. Navegue até o diretório hidrometro;

2. Compile no terminal utilizando:
```bash
javac -cp "lib/gson-2.11.0.jar" -d bin src/*.java
```

3. Execute o programa:

```bash
java -cp "bin;lib/gson-2.11.0.jar" src.Cliente
```

4. Digite o número de hidrômetros que o usuário deseja que sejam executados.

5. O mesmo número de janelas com cada hidrômetro será exibido, de tal forma que o usuário poderá encerrar cada hidrômetro individualmente.