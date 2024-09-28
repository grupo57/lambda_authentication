
# Autenticação

## AWS Lambda
LambdaJwtCreatorHandler é a função lambda que faz a autenticação.
Ela recebe um Map<String,String> como request, simulando o Json e retorna um JWT como response.

### Tipo de cliente
Há três possibiliades:
- Autenticação de usuário do tipo Administrador
- Autenticação de usuário cliente que informou o cpf
- Autenticação de usuário cliente que não informou o cpf, anônimo

Em cada um desses casos, o sistema valida os dados passados (de acordo com o tipo) e cria o JWT com os dados para acesso
No caso de erro, é gerado uma exception.

### RDS

Como a validação é feita acessando a base de dados, os parâmetros de endereço, usuário e senha são passados através de variáveis de ambiente.
- db-user
- db-pwd
- db-url

### Futuro

Para a próxima versão será usado o AWS Secrets Manager, para gerenciar essas configurações.
