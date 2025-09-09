# 💧 Simulador de Hidrômetro (POO em Java)

Este projeto implementa um **simulador de hidrômetro** utilizando Programação Orientada a Objetos em **Java**.  
Ele foi desenvolvido como parte de um exercício acadêmico e representa a **primeira versão** do sistema.

---

## 📌 Funcionalidades

- Configuração da **vazão de água** (em m³/h).  
- Simulação de fluxo de água em **tempo real**.  
- Conversão automática de **m³/h para m³/s**.  
- Exibição contínua em **display** com:
  - Fluxo atual (m³ por segundo).  
  - Total acumulado de água medida.  
- Funcionamento contínuo (24h) até ser interrompido.

---

## 📂 Estrutura do Projeto

```

src/
├── Main.java           # Classe principal para executar o simulador
├── Hidrometro.java     # Controla a simulação e coordena os componentes
├── AguaFluxo.java      # Responsável por simular a vazão de água
└── Display.java        # Exibe os valores de fluxo e acumulado

````

---

## ▶️ Como Executar

1. Compile os arquivos:

```bash
javac *.java
````

2. Execute o programa:

```bash
java Main
```

3. Exemplo de saída no console:

```
Simulador de Hidrômetro iniciado...
Fluxo atual: 0.0972 m³ | Total acumulado: 0.0972 m³
Fluxo atual: 0.0972 m³ | Total acumulado: 0.1944 m³
Fluxo atual: 0.0972 m³ | Total acumulado: 0.2916 m³
...
```

---

## 📅 Roadmap

* [x] Primeira versão: Simulação básica (configuração de vazão, display e fluxo).
* [ ] Segunda versão: Geração de imagens a cada m³.
---

## 👨‍💻 Autor - Osmar dos Santos Filho

* Projeto acadêmico desenvolvido em **POO - Java**.
* Criado para estudo de **modelagem UML** e simulação de sistemas.

