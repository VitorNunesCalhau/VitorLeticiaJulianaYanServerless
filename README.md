# Alexa Skill Informações de Clima
### Alunos:
Juliana Serra 

Letícia Lott

Vitor Nunes

Yan Nalon
## Contexto

Informações de Clima é um skill para Alexa desenvolvido para fornecer informações meteorológicas personalizadas. Este skill integra-se com a API da OpenWeatherMap para obter dados climáticos em tempo real e utiliza o AWS Lambda para processamento das requisições. Além disso, o DynamoDB é usado para persistir a cidade preferida do usuário, permitindo um acesso rápido e conveniente às informações meteorológicas.

A imagem abaixo ilustra o esquema de funcionamento da plataforma Alexa, onde a interação do usuário é processada para determinar a solicitação e obter a resposta adequada da lógica de aplicação hospedada na nuvem.

![Esquema de Funcionamento da Plataforma Alexa](alexa.png)

## Funcionalidades

### Consulta de Clima
A Skill de Informações de Clima permite que os usuários consultem as condições climáticas atuais em diferentes cidades. O usuário pode simplesmente perguntar sobre o clima em uma cidade específica, e a Alexa responderá com informações detalhadas como a descrição do tempo e a temperatura.

### Persistência de Cidade Preferida
Os usuários podem definir uma cidade como sua cidade preferida para consultas futuras. Após consultar o clima de uma cidade, a Alexa perguntará se o usuário deseja definir aquela cidade como preferida. Se o usuário confirmar, a cidade será armazenada no DynamoDB.

### Consultas Futuras Simplificadas
Uma vez que a cidade preferida está definida, o usuário pode simplesmente perguntar sobre o clima atual, e a Alexa responderá com as informações da cidade preferida, sem a necessidade de especificar a cidade novamente.

### Exemplo de Interação

**Usuário:** "Alexa, abrir aplicação Informações de Clima"  
**Alexa:** "Bem-vindo ao ClimaInformativo. Para qual cidade você deseja saber o clima?"

**Usuário:** "São Paulo"  
**Alexa:** "O clima em São Paulo é ensolarado com temperatura de 25°C. Você gostaria de definir São Paulo como sua cidade preferida para consultas futuras?"

**Usuário:** "Sim, por favor."  
**Alexa:** "Cidade São Paulo definida como preferida. Deseja saber o clima em outra cidade?"

**Usuário:** "Sim, qual é o clima no Rio de Janeiro?"  
**Alexa:** "O clima no Rio de Janeiro é parcialmente nublado com temperatura de 28°C."

## Tecnologias Utilizadas

- **Amazon Alexa:** Plataforma de voz da Amazon utilizada para criar a interface de usuário de voz.
- **AWS Lambda:** Serviço de computação que executa código em resposta a eventos, usado para processar as requisições da Alexa.
- **DynamoDB:** Serviço de banco de dados NoSQL da AWS usado para armazenar a cidade preferida do usuário.
- **OpenWeatherMap API:** API pública utilizada para obter informações climáticas.


## Contribuições

Contribuições são bem-vindas! Se você tiver sugestões de melhorias, reporte problemas ou faça um fork deste repositório para enviar pull requests.

---

Esperamos que você aproveite o uso da Skill Alexa informações de clima!
