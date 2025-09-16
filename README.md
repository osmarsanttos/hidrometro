# 💧 Simulador de Hidrômetro em Java (Swing)

Este projeto implementa um **simulador digital de hidrômetro** em **Java**, utilizando **POO** e **Swing** para interface gráfica.  
O objetivo é reproduzir o funcionamento de um medidor de água em tempo real, de forma contínua, sem depender de hardware físico.

---

## 🚀 Funcionalidades

- Simulação **ininterrupta** do fluxo de água.
- Exibição da **vazão (m³/h)** e do **volume acumulado (m³)** em tempo real.
- Interface gráfica em **Swing**, estilo display digital.
- Atualização automática a cada **1 segundo**.
- Encerramento apenas quando o usuário fecha a janela.

---

## 📸 Demonstração

A interface exibe:

Vazão: 100.00 m³/h
Volume acumulado: 0.027 m³


Os valores são atualizados automaticamente conforme o tempo passa.

---

## 🛠️ Tecnologias utilizadas

- **Java 8+**
- **Swing** (GUI)
- **POO** (Programação Orientada a Objetos)

---

## 📂 Estrutura do Código

- `FluxoAgua.java` → Classe que simula o fluxo de água.
- `HidrometroSwing.java` → Interface gráfica em Swing + controle da simulação.

---

## ▶️ Como executar

1. Compile os arquivos:

```bash
javac HidrometroSwing.java

Execute o programa:

java HidrometroSwing

A janela será aberta e o hidrômetro rodará sem parar até você fechar a aplicação.
